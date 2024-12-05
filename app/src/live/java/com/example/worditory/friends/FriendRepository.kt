package com.example.worditory.friends

import android.content.Context
import com.example.worditory.database.DbKey
import com.example.worditory.user.UserRepoModel
import com.example.worditory.user.sanitizeEmail
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal object FriendRepository {
    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    internal fun listenForFriendRequests(
        onRequestAdded: (Pair<String, UserRepoModel>) -> Unit,
        onRequestRemoved: (String) -> Unit
    ): FriendRequestListener {
        val listener = FriendRequestListener(onRequestAdded, onRequestRemoved)
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.FRIEND_REQUESTS)
                .child(currentUser.uid)
                .addChildEventListener(listener)
        }

        return listener
    }

    internal fun removeListener(listener: FriendRequestListener) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.FRIEND_REQUESTS)
                .child(currentUser.uid)
                .removeEventListener(listener)
        }
    }

    internal fun syncLocalSavedFriendsWithServer(scope: CoroutineScope, context: Context) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.FRIENDS)
                .child(currentUser.uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    scope.launch {
                        val localFriends = context.savedFriendsDataStore.data.first().friendsList
                        val localFriendUids = localFriends.map { it.uid }.toSet()
                        val serverFriendUids = mutableSetOf<String>()

                        for (child in snapshot.children) {
                            val serverFriendUid = child.key!!
                            serverFriendUids.add(serverFriendUid)

                            if (!localFriendUids.contains(serverFriendUid)) {
                                val timestamp = child.getValue(Long::class.java)!!

                                database
                                    .child(DbKey.USERS)
                                    .child(serverFriendUid)
                                    .get()
                                    .addOnSuccessListener { snapshot ->
                                        val serverFriend =
                                            snapshot.getValue(UserRepoModel::class.java)!!

                                        val localFriend = Friend.newBuilder()
                                            .setUid(serverFriendUid)
                                            .setDisplayName(serverFriend.displayName ?: "")
                                            .setAvatarId(serverFriend.avatarId ?: 0)
                                            .setRank(serverFriend.rank ?: 1500)
                                            .setGamesPlayed(serverFriend.gamesPlayed ?: 0)
                                            .setGameWon(serverFriend.gamesWon ?: 0)
                                            .setTimestamp(timestamp)
                                            .build()

                                        scope.launch {
                                            context.addSavedFriend(localFriend)
                                        }
                                    }
                            }
                        }

                        for (localFriendUid in localFriendUids) {
                            if (!serverFriendUids.contains(localFriendUid)) {
                                context.removeSavedFriend(localFriendUid)
                            }
                        }
                    }
                }
        }
    }

    internal fun sendFriendRequestFromGame(gameId: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.GAMES).child(gameId).get().addOnSuccessListener { snapshot ->
                val player1Uid = snapshot.child(DbKey.Games.PLAYER_1).getValue(String::class.java)!!
                val player2Uid = snapshot.child(DbKey.Games.PLAYER_2).getValue(String::class.java)!!

                val opponentUid = when (currentUser.uid) {
                    player1Uid -> player2Uid
                    player2Uid -> player1Uid
                    else -> throw IllegalArgumentException("User is not in game")
                }

                sendFriendRequest(opponentUid)
            }
        }
    }

    internal fun sendFriendRequestFromEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (OnFailure) -> Unit
    ) {
        database
            .child(DbKey.EMAIL_TO_UID)
            .child(sanitizeEmail(email))
            .get()
            .addOnSuccessListener { snapshot ->
                val uid = snapshot.getValue(String::class.java)

                if (uid == null) {
                    onError(OnFailure(OnFailure.Reason.EMAIL_NOT_REGISTERED))
                } else {
                    val currentUser = auth.currentUser

                    if (currentUser != null) {
                        database
                            .child(DbKey.FRIENDS)
                            .child(currentUser.uid)
                            .child(uid)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val existingFriend = snapshot.getValue(Long::class.java)

                                if (existingFriend == null) {
                                    sendFriendRequest(uid)
                                    onSuccess()
                                } else {
                                    onError(OnFailure(OnFailure.Reason.USER_IS_ALREADY_A_FRIEND))
                                }
                            }
                    }
                }
            }
    }

    private fun sendFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.FRIEND_REQUESTS).child(uid).child(currentUser.uid).setValue(true)
        }
    }

    internal fun acceptFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database
                .child(DbKey.FRIENDS)
                .child(currentUser.uid)
                .child(uid)
                .setValue(ServerValue.TIMESTAMP)

            database
                .child(DbKey.FRIENDS)
                .child(uid)
                .child(currentUser.uid)
                .setValue(ServerValue.TIMESTAMP)

            deleteFriendRequest(uid)
        }
    }

    internal fun deleteFriendRequest(uid: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.FRIEND_REQUESTS).child(currentUser.uid).child(uid).removeValue()
        }
    }

    internal fun ifOpponentIsFriend(gameId: String, isFriend: (Boolean) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            database.child(DbKey.GAMES).child(gameId).get().addOnSuccessListener { snapshot ->
                val player1Uid = snapshot.child(DbKey.Games.PLAYER_1).getValue(String::class.java)!!
                val player2Uid = snapshot.child(DbKey.Games.PLAYER_2).getValue(String::class.java)!!

                val opponentUid = when (currentUser.uid) {
                    player1Uid -> player2Uid
                    player2Uid -> player1Uid
                    else -> throw IllegalArgumentException("User is not in game")
                }

                database
                    .child(DbKey.FRIENDS)
                    .child(currentUser.uid)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        var isFriend = false

                        for (child in snapshot.children) {
                            val uid = child.key

                            if (uid != null && uid == opponentUid) {
                                isFriend = true
                                break
                            }
                        }

                        isFriend(isFriend)
                    }
            }
        }
    }

    internal class FriendRequestListener(
        private val onRequestAdded: (Pair<String, UserRepoModel>) -> Unit,
        private val onRequestRemoved: (String) -> Unit
    ): ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val uid = snapshot.key

            if (uid != null) {
                database.child(DbKey.USERS).child(uid).get().addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(UserRepoModel::class.java)

                    if (user != null) {
                        onRequestAdded(Pair(uid, user))
                    }
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val uid = snapshot.key

            if (uid != null) {
                onRequestRemoved(uid)
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    internal class OnFailure(
        internal val reason: Reason,
        internal val error: DatabaseError? = null
    ) {
        enum class Reason {
            EMAIL_NOT_REGISTERED,
            USER_IS_ALREADY_A_FRIEND,
            CANCELLED
        }
    }
}