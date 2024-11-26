package com.example.worditory.game

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.word.PlayedWordRepoModel
import com.example.worditory.game.word.WordRepository
import com.example.worditory.R
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.notification.Notifications
import com.example.worditory.saved.SavedGamesService
import com.example.worditory.saved.addSavedLiveGame
import com.example.worditory.saved.removeSavedLiveGame
import com.example.worditory.saved.setGameOver
import com.example.worditory.user.UserRepoModel
import com.example.worditory.user.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class LiveGameViewModel(
    liveModel: LiveGameModel,
    navController: NavController,
    context: Context
): GameViewModelBase(
    model = liveModel.game,
    navController = navController,
    context = context,
    avatarIdPlayer2 = liveModel.opponent.avatarId,
    displayNamePlayer2 = if (liveModel.opponent.displayName == "") {
        context.getString(R.string.waiting)
    } else {
        liveModel.opponent.displayName
    }
) {
    private var playedWordCount = liveModel.playedWordCount
    private val isPlayer1 = liveModel.isPlayer1
    private var timestamp = liveModel.timestamp

    private val latestWordListener: WordRepository.LatestWordListener
    private val opponentListener: GameRepository.UserListener
    private val timestampListener: GameRepository.TimestampListener

    private var isOpponentOpeningTurn = false

    private val nextGameJob: Job

    private val liveModel: LiveGameModel
        get() = LiveGameModel.newBuilder()
            .setGame(model)
            .setIsPlayer1(isPlayer1)
            .setPlayedWordCount(playedWordCount)
            .setTimestamp(timestamp)
            .setIsGameOver(gameOverState != GameOver.State.IN_PROGRESS)
            .setOpponent(OpponentModel.newBuilder()
                .setDisplayName(scoreBoard.scorePlayer2.displayName.value)
                .setAvatarId(scoreBoard.scorePlayer2.avatarId.value)
            )
            .build()

    init {
        if (liveModel.opponent != null) {
            scoreBoard.scorePlayer2.avatarId.value
        }

        latestWordListener = WordRepository.listenForLatestWord(
            gameId = id,
            onNewWord = { word ->
                if (word.index!! >= playedWordCount) {
                    onNewWord(word, context)
                }
            },
            onError = {} // TODO(handle errors)
        )

        opponentListener = GameRepository.listenForOpponent(
            gameId = id,
            opponent = if (isPlayer1) Game.Player.PLAYER_2 else Game.Player.PLAYER_1,
            onOpponentChange = { onOpponentChange(it) },
            onError = {} // TODO(handle errors)
        )

        timestampListener = GameRepository.listenForTimestampChange(
            gameId = id,
            onTimestampChange = { timestamp = it },
            onError = {} // TODO(handle errors)
        )

        nextGameJob = viewModelScope.launch {
            SavedGamesService.savedGamesStateFlow.collect { savedGames ->
                val validNextGames = savedGames.filter {
                    it.isPlayerTurn && it.liveGame.game.id != id
                }

                nextGame = when {
                    validNextGames.isEmpty() -> null
                    else -> validNextGames.first().liveGame.game.id
                }
            }
        }

        viewModelScope.launch {
            delay(2500L)
            isOpponentOpeningTurn = false
        }
    }

    override fun onPlayButtonClick(context: Context): Boolean {
        val word = board.word.model

        if (super.onPlayButtonClick(context)) {
            WordRepository.playWord(id, word, board.model, playedWordCount++)
            saveGame(context)
            return true
        }

        return false
    }

    override fun onPassTurn(context: Context) {
        WordRepository.passTurn(id, playedWordCount++)

        super.onPassTurn(context)
    }

    override fun onResignGame(context: Context) {
        WordRepository.resignGame(id, playedWordCount++)

        super.onResignGame(context)
    }

    override fun onNextGameClick(gameId: String, context: Context) {
        onExitGame(context)
        navController.navigate(LiveScreen.LiveGame.buildRoute(gameId))
    }

    override fun updateScoreboard() {
        scoreBoard.score = board.computeScore()
        if (scoreBoard.decrementScoreToWin()) {
            GameRepository.decrementScoreToWin(id)
        }
    }

    override fun saveGame(context: Context) {
        viewModelScope.launch {
            context.addSavedLiveGame(liveModel)
        }
    }

    private fun onNewWord(word: PlayedWordRepoModel, context: Context) {
        if (word.passTurn) {
            passTurnDialog.show(
                onDismiss = {
                    scoreBoard.decrementScoreToWin()
                    ++playedWordCount
                    isPlayerTurn = true
                    saveGame(context)
                }
            )
        } else if (word.resignGame) {
            resignGameDialog.show(
                onDismiss = {
                    gameOverState = GameOver.State.WIN
                    onGameOver(context)
                    saveGame(context)
                }
            )
        } else {
            viewModelScope.launch {
                board.word.model = WordModel()

                board.word.withDrawPathTweenDuration(millis = word.tiles!!.size * 350) {
                    if (isOpponentOpeningTurn) {
                        delay(1500L)
                    }

                    for (repoTile in word.tiles) {
                        val tile = board.tiles[flipTileIndex(repoTile.index!!)]
                        board.word.onSelectTile(tile, Game.Player.PLAYER_2)
                    }

                    delay(word.tiles.size * 350L + 1000L)
                }

                for (repoTile in word.tiles) {
                    val tile = board.tiles[flipTileIndex(repoTile.index!!)]
                    val newLetter = repoTile.newLetter!!.asLetter()

                    board.letterBag.exchangeForLetter(
                        oldLetter = tile.letter,
                        newLetter = newLetter
                    )
                    tile.letter = newLetter
                }

                board.updateOwnershipsForWord(Game.Player.PLAYER_2)
                board.playWord(Game.Player.PLAYER_2)
                scoreBoard.score = board.computeScore()
                scoreBoard.decrementScoreToWin()
                ++playedWordCount
                if (checkForGameOver()) {
                    onGameOver(context)
                } else {
                    isPlayerTurn = true
                }

                saveGame(context)
            }
        }
    }

    private fun onOpponentChange(opponent: UserRepoModel) {
        if (opponent.avatarId != null) {
            scoreBoard.scorePlayer2.avatarId.value = opponent.avatarId
        }
        if (opponent.displayName != null) {
            scoreBoard.scorePlayer2.displayName.value = opponent.displayName
        }
    }

    override fun onGameOver(context: Context) {
        viewModelScope.launch {
            context.setGameOver(id, gameOverState)
        }

        GameRepository.setGameOver(id, gameOverState, isPlayer1)

        if (gameOverState == GameOver.State.WIN) {
            setBadgesOnGameWon(context)
        }
    }

    override fun onExitGame(context: Context) {
        super.onExitGame(context)

        WordRepository.removeListener(latestWordListener)
        GameRepository.removeListener(opponentListener)
        GameRepository.removeListener(timestampListener)

        if (gameOverState != GameOver.State.IN_PROGRESS) {
            viewModelScope.launch {
                context.removeSavedLiveGame(id)
            }
        }

        nextGameJob.cancel()
    }

    fun flipTileIndex(index: Int) = board.width * board.height - index - 1
}