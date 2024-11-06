package com.example.worditory.game

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.playbutton.PlayButtonViewModel
import com.example.worditory.game.scoreboard.ScoreBoardViewModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.gameover.GameOverViewModel
import com.example.worditory.game.menu.MenuViewModel
import com.example.worditory.getPlayerAvatarId
import com.example.worditory.incrementGamesPlayed
import com.example.worditory.incrementGamesWon
import com.example.worditory.navigation.Screen
import com.example.worditory.saved.addSavedGame
import com.example.worditory.saved.removeSavedGame
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class GameViewModel(
    model: GameModel,
    val navController: NavController,
    playerAvatarIdFlow: Flow<Int>
): ViewModel() {
    internal val id = model.id
    internal val boardWidth = model.board.width
    internal val boardHeight = model.board.height
    internal val opponent = model.opponent
    internal val colorScheme = Tile.ColorScheme.from(model.colorScheme)

    private val _isPlayerTurnStateFlow = MutableStateFlow(model.isPlayerTurn)
    internal val isPlayerTurnStateFlow = _isPlayerTurnStateFlow.asStateFlow()
    internal var isPlayerTurn: Boolean
        get() = isPlayerTurnStateFlow.value
        set(value) {
            _isPlayerTurnStateFlow.value = value
        }

    private val _isNotAWordStateFlow = MutableStateFlow(false)
    internal val isNotAWordStateFlow = _isNotAWordStateFlow.asStateFlow()
    internal var isNotAWord: Boolean
        get() = isNotAWordStateFlow.value
        set(value) {
            _isNotAWordStateFlow.value = value
        }

    private val _displayMenuStateFlow = MutableStateFlow(false)
    internal val displayMenuStateFlow = _displayMenuStateFlow.asStateFlow()
    private var displayMenu: Boolean
        get() = displayMenuStateFlow.value
        set(value) {
            _displayMenuStateFlow.value = value
        }

    private val _gameOverStateFlow = MutableStateFlow(GameOver.State.IN_PROGRESS)
    internal val gameOverStateFlow = _gameOverStateFlow.asStateFlow()
    internal var gameOverState: GameOver.State
        get() = gameOverStateFlow.value
        set(value) {
            _gameOverStateFlow.value = value
        }

    internal val scoreBoard = ScoreBoardViewModel(
        initialScoreToWin = boardWidth * boardHeight,
        currentScoreToWin = model.scoreToWin,
        playerAvatarIdFlow,
        MutableStateFlow(model.opponent.avatar),
        colorScheme
    )

    internal val board = BoardViewModel(
        model.board,
        colorScheme,
        isPlayerTurnStateFlow,
        onWordChanged = { isNotAWord = false }
    )

    internal val playButton = PlayButtonViewModel(
        board.word.modelStateFlow,
        isNotAWordStateFlow,
        isPlayerTurnStateFlow
    )

    internal val menu = MenuViewModel(isPlayerTurnStateFlow)

    internal val gameOverWin = GameOverViewModel(
        navController = navController,
        gameOverStateFlow = gameOverStateFlow,
        targetState = GameOver.State.WIN
    )

    internal val gameOverLose = GameOverViewModel(
        navController = navController,
        gameOverStateFlow = gameOverStateFlow,
        targetState = GameOver.State.LOSE
    )

    internal val model: GameModel
        get() = GameModel.newBuilder()
            .setId(id)
            .setBoard(board.model)
            .setOpponent(opponent)
            .setColorScheme(colorScheme.model)
            .setIsPlayerTurn(isPlayerTurn)
            .setScoreToWin(scoreBoard.scoreToWin)
            .build()

    init {
        scoreBoard.score = board.getScore()
    }

    internal open fun onPlayButtonClick(): Boolean {
        if (isPlayerTurn) {
            val wordString = board.word.toString()
            if (WordDictionary.contains(wordString)) {
                board.playWord(Game.Player.PLAYER_1)
                onWordPlayed()
                return true
            } else {
                isNotAWord = true
            }
        }
        return false
    }

    protected fun onWordPlayed() {
        updateScore()
        isPlayerTurn = !checkForGameOver()
    }

    internal fun onMenuClick() {
        displayMenu = true
    }

    internal fun onDismissMenu() {
        displayMenu = false
    }

    internal fun onExitGame(context: Context) {
        val currentGameOverState = gameOverState

        GlobalScope.launch {
            if (currentGameOverState == GameOver.State.IN_PROGRESS) {
                context.addSavedGame(model)
            } else {
                context.removeSavedGame(id)
                context.incrementGamesPlayed()
                if (currentGameOverState == GameOver.State.WIN) {
                    context.incrementGamesWon()
                }
            }
        }

        navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Main.route) {
                inclusive = true
            }
        }
    }

    private fun updateScore() {
        scoreBoard.score = board.getScore()
        if (scoreBoard.score.player1 + 1 < scoreBoard.scoreToWin
            && scoreBoard.score.player2 + 1 < scoreBoard.scoreToWin
        ) {
            --scoreBoard.scoreToWin
        }
    }

    private fun checkForGameOver(): Boolean {
        val score = scoreBoard.score
        val toWin = scoreBoard.scoreToWin
        if (score.player1 >= toWin || score.player2 == 0) {
            gameOverState = GameOver.State.WIN
            return true
        } else if (score.player2 >= toWin || score.player1 == 0) {
            gameOverState = GameOver.State.LOSE
            return true
        }

        return false
    }
}
