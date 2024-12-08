package com.example.worditory.match

import com.example.worditory.game.board.BoardRepoModel
import com.example.worditory.boardHeight
import com.example.worditory.boardWidth
import com.example.worditory.database.DbKey
import com.example.worditory.generateKey
import com.example.worditory.game.GameRepoModel
import com.example.worditory.game.board.LetterBag
import com.example.worditory.game.board.tile.asCharCode
import com.example.worditory.match.OnMatchFailure.Reason
import com.example.worditory.user.UserRepoModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.ServerValue
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

internal object MatchRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun listenForChallenges(
        onChallenge: (Challenge) -> Unit
    ): ChallengeListener {
        val listener = ChallengeListener(onChallenge)
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.CHALLENGES).child(currentUser.uid).addChildEventListener(listener)
        }

        return listener
    }

    internal fun removeListener(listener: ChallengeListener) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.CHALLENGES).child(currentUser.uid).removeEventListener(listener)
        }
    }

    internal fun makeMatch(
        gameType: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        database.child(DbKey.MATCHES).child(gameType).runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val match = currentData.getValue(MatchRepoModel::class.java)
                val userId = auth.currentUser?.uid

                if (userId == null) {
                    onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED))
                } else if (match == null) {
                    currentData.value = MatchRepoModel("", "")
                    onFailure(OnMatchFailure(Reason.GAME_TYPE_NOT_INITIALIZED))
                } else if (match.userId == userId) {
                    onFailure(OnMatchFailure(Reason.USER_ALREADY_IN_WAITING_ROOM, match.gameId))
                } else if (match.gameId == "") {
                    val gameId = generateKey()
                    createGame(
                        gameId = gameId,
                        gameType = gameType,
                        challenger = "",
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                    currentData.value = MatchRepoModel(gameId, userId)
                } else {
                    loadGame(
                        gameId = match.gameId!!,
                        opponentUserId = match.userId!!,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                    currentData.value = MatchRepoModel("", "")
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) { }
        })
    }

    internal fun challengeFriend(
        friendUid: String,
        gameType: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED))
        } else {
            val challengeLock = challengeLock(currentUser.uid, friendUid)

            database
                .child(DbKey.CHALLENGE_LOCKS)
                .child(challengeLock)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        if (currentData.value == null) {
                            currentData.value = true
                            val gameId = generateKey()
                            createGame(
                                gameId = gameId,
                                gameType = gameType,
                                challenger = friendUid,
                                onSuccess = { onMatchSuccess ->
                                    database
                                        .child(DbKey.CHALLENGES)
                                        .child(friendUid)
                                        .child(currentUser.uid)
                                        .setValue(gameId)
                                    onSuccess(onMatchSuccess)
                                },
                                onFailure = onFailure
                            )
                        } else {
                            // TODO(handle case where user sends two challenges to same person)
                            database
                                .child(DbKey.CHALLENGES)
                                .child(currentUser.uid)
                                .child(friendUid)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            val gameId = snapshot.getValue(String::class.java)!!
                                            acceptChallenge(
                                                gameId = gameId,
                                                opponentUid = friendUid,
                                                onSuccess = onSuccess,
                                                onFailure = onFailure
                                            )

                                            database
                                                .child(DbKey.CHALLENGES)
                                                .child(currentUser.uid)
                                                .child(friendUid)
                                                .removeEventListener(this)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        onFailure(OnMatchFailure(Reason.CANCELLED))
                                    }
                                })
                        }

                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {}
                })
        }
    }

    internal fun acceptChallenge(
        gameId: String,
        opponentUid: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        deleteChallenge(opponentUid)
        loadGame(
            gameId = gameId,
            opponentUserId = opponentUid,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    internal fun deleteChallenge(opponentUid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.CHALLENGE_LOCKS)
                .child(challengeLock(currentUser.uid, opponentUid))
                .removeValue()

            database
                .child(DbKey.CHALLENGES)
                .child(currentUser.uid)
                .child(opponentUid)
                .removeValue()

            database
                .child(DbKey.CHALLENGES)
                .child(opponentUid)
                .child(currentUser.uid)
                .removeValue()
        }
    }

    private fun createGame(
        gameId: String,
        gameType: String,
        challenger: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED))
        } else {
            val scoreToWin = gameType.boardWidth() * gameType.boardHeight()

            val game = GameRepoModel(
                gameType = gameType,
                player1 = userId,
                scoreToWin = scoreToWin
            )
            val board = createBoard(gameType)

            onSuccess(
                OnMatchSuccess(
                    gameId = gameId,
                    userID = userId,
                    isPlayer1 = true,
                    scoreToWin = scoreToWin,
                    wordCount = 0,
                    challenger = challenger,
                    timestamp = 0,
                    game = game,
                    board = board,
                    opponent = null
                )
            )

            val gameTask = database.child(DbKey.GAMES).child(gameId).setValue(game)
            val boardTask = database.child(DbKey.BOARDS).child(gameId).setValue(board)

            Tasks.whenAll(gameTask, boardTask)
                .addOnSuccessListener {
                    database.child(DbKey.PLAYER_GAMES).child(userId).child(gameId).setValue(true)
                }.addOnFailureListener {
                    onFailure(OnMatchFailure(Reason.DATABASE_WRITE_ERROR, gameId))
                }
        }
    }

    private fun loadGame(
        gameId: String,
        opponentUserId: String,
        onSuccess: (OnMatchSuccess) -> Unit,
        onFailure: (OnMatchFailure) -> Unit
    ) {
        val gameTask = database.child(DbKey.GAMES).child(gameId).get()
        val boardTask = database.child(DbKey.BOARDS).child(gameId).get()
        val opponentTask = database.child(DbKey.USERS).child(opponentUserId).get()

        Tasks.whenAllComplete(gameTask, boardTask, opponentTask).addOnCompleteListener { taskList ->
            val gameSnapshot = taskList.result[0].result as DataSnapshot
            val game = gameSnapshot.getValue(GameRepoModel::class.java)

            val boardSnapshot = taskList.result[1].result as DataSnapshot
            val board = boardSnapshot.getValue(BoardRepoModel::class.java)

            val opponentSnapshot = taskList.result[2].result as DataSnapshot
            val opponent = opponentSnapshot.getValue(UserRepoModel::class.java)

            val userId = auth.currentUser?.uid

            if (userId == null) {
                onFailure(OnMatchFailure(Reason.USER_NOT_AUTHENTICATED, gameId))
            } else if (game == null || board == null) {
                onFailure(OnMatchFailure(Reason.DATABASE_READ_ERROR, gameId))
            } else if (userId == game.player1) {
                onFailure(OnMatchFailure(Reason.USER_ALREADY_IN_WAITING_ROOM, gameId))
            } else {
                onSuccess(
                    OnMatchSuccess(
                        gameId = gameId,
                        userID = userId,
                        isPlayer1 = false,
                        scoreToWin = game.scoreToWin!!,
                        wordCount = 0,
                        challenger = "",
                        timestamp = game.timestamp as Long,
                        game = game.copy(player2 = userId),
                        board = board,
                        opponent = opponent
                    )
                )

                database
                    .child(DbKey.GAMES)
                    .child(gameId)
                    .child(DbKey.Games.PLAYER_2)
                    .setValue(userId)

                database
                    .child(DbKey.GAMES)
                    .child(gameId)
                    .child(DbKey.Games.TIMESTAMP)
                    .setValue(ServerValue.TIMESTAMP)

                database
                    .child(DbKey.PLAYER_GAMES)
                    .child(userId)
                    .child(gameId)
                    .setValue(true)
            }
        }
    }

    private fun createBoard(gameType: String): BoardRepoModel {
        val letterBag = LetterBag()
        val letters = mutableListOf<Int>()
        val numLetters = gameType.boardWidth() * gameType.boardHeight()

        for (i in 0..<numLetters) {
            letters.add(letterBag.takeLetter().asCharCode())
        }

        return BoardRepoModel(letters)
    }

    internal class ChallengeListener(
        private val onChallenge: (Challenge) -> Unit
    ): ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val userId = snapshot.key!!
            val gameId = snapshot.getValue(String::class.java)!!

            database
                .child(DbKey.USERS)
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(UserRepoModel::class.java)!!
                    onChallenge(Challenge(gameId, userId, user))
                }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    internal data class Challenge(
        internal val gameId: String,
        internal val userId: String,
        internal val user: UserRepoModel
    )
}

internal class OnMatchSuccess(
    internal val gameId: String,
    internal val userID: String,
    internal val isPlayer1: Boolean,
    internal val scoreToWin: Int,
    internal val wordCount: Int,
    internal val challenger: String,
    internal val timestamp: Long,
    internal val game: GameRepoModel,
    internal val board: BoardRepoModel,
    internal val opponent: UserRepoModel?
)

internal class OnMatchFailure(
    internal val reason: Reason,
    internal val gameId: String? = null
) {
    internal enum class Reason {
        USER_ALREADY_IN_WAITING_ROOM,
        USER_NOT_AUTHENTICATED,
        GAME_TYPE_NOT_INITIALIZED,
        DATABASE_READ_ERROR,
        DATABASE_WRITE_ERROR,
        CANCELLED
    }
}

private fun challengeLock(userId: String, opponentUid: String) =
    if (userId < opponentUid) {
        userId + opponentUid
    } else {
        opponentUid + userId
    }

private val gameTypes = listOf("size5x4", "size5x5", "size7x5", "size6x6", "size8x6", "size8x8")
internal fun randomGameType() = gameTypes.random()