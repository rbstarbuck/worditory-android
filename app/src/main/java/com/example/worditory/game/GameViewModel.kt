package com.example.worditory.game

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.badge.NewBadgesToDisplay
import com.example.worditory.audio.AudioPlayer
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.tile.Tile
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.dict.WordDictionary
import com.example.worditory.game.playbutton.PlayButtonViewModel
import com.example.worditory.game.scoreboard.ScoreBoardViewModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.gameover.GameOverViewModel
import com.example.worditory.game.hint.HintMaker
import com.example.worditory.game.menu.MenuViewModel
import com.example.worditory.game.tutorial.TutorialViewModel
import com.example.worditory.incrementGamesPlayed
import com.example.worditory.incrementGamesWon
import com.example.worditory.navigation.Screen
import com.example.worditory.badge.Badge
import com.example.worditory.badge.addDisplayedBadge
import com.example.worditory.saved.addSavedGame
import com.example.worditory.saved.removeSavedGame
import com.example.worditory.setHasShownTutorial
import com.example.worditory.setObscureWord
import com.example.worditory.setPlayed5LetterWord
import com.example.worditory.setPlayed6LetterWord
import com.example.worditory.setPlayed7LetterWord
import com.example.worditory.setPlayed8LetterWord
import com.example.worditory.setQWord
import com.example.worditory.setWonClassic
import com.example.worditory.setWonLightning
import com.example.worditory.setWonRapid
import com.example.worditory.setZWord
import kotlinx.coroutines.delay
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

    internal val hint = HintMaker(board)

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

    internal val tutorial = TutorialViewModel(board, scoreBoard)

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
        scoreBoard.score = board.computeScore()
        AudioPlayer.gameOn()
    }

    internal open fun onPlayButtonClick(context: Context): Boolean {
        if (isPlayerTurn) {
            val wordString = board.word.toString()
            if (WordDictionary.contains(wordString)) {
                AudioPlayer.wordPlayed(wordString.length)
                setBadgesOnWordPlayed(wordString, context)
                board.playWord(Game.Player.PLAYER_1)
                onWordPlayed(context)
                return true
            } else {
                isNotAWord = true
            }
        }
        return false
    }

    protected fun onWordPlayed(context: Context) {
        scoreBoard.score = board.computeScore()
        scoreBoard.decrementScoreToWin()
        isPlayerTurn = !checkForGameOver(context)
    }

    internal fun onMenuClick() {
        displayMenu = true
    }

    internal fun onDismissMenu() {
        displayMenu = false
    }

    internal fun onSound(enabled: Boolean, context: Context) {
        AudioPlayer.setEnabled(enabled, context)
    }

    internal fun onHint() {
        board.word.model = WordModel()
        viewModelScope.launch {
            delay(500L)
            board.word.model = hint.hint()
        }
    }

    internal open fun onPassTurn(context: Context) {
        board.word.model = WordModel()
        scoreBoard.decrementScoreToWin()
        isPlayerTurn = !isPlayerTurn
    }

    internal fun onTutorial() {
        tutorial.enabled = true
    }

    internal fun onExitGame(context: Context) {
        val currentGameOverState = gameOverState

        viewModelScope.launch {
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

    internal fun showTutorial(context: Context) {
        viewModelScope.launch {
            context.setHasShownTutorial()
            delay(500L)
            tutorial.enabled = true
        }
    }

    private fun checkForGameOver(context: Context): Boolean {
        val score = scoreBoard.score
        val toWin = scoreBoard.scoreToWin
        if (score.player1 >= toWin || score.player2 == 0) {
            gameOverState = GameOver.State.WIN
            AudioPlayer.gameOverWin()
            setBadgesOnGameWon(context)
            return true
        } else if (score.player2 >= toWin || score.player1 == 0) {
            gameOverState = GameOver.State.LOSE
            AudioPlayer.gameOverLose()
            return true
        }

        return false
    }

    internal fun setBadgesOnWordPlayed(word: String, context: Context) {
        val wordFrequency = WordDictionary.frequency(word)
        if (wordFrequency != null) {
            if (wordFrequency == 3) {
                viewModelScope.launch { context.setObscureWord(word) }
                NewBadgesToDisplay.add(Badge.PlayedObscureWord)
            }
            if (word.contains("Q")) {
                viewModelScope.launch { context.setQWord(word) }
                NewBadgesToDisplay.add(Badge.PlayedQWord)
            }
            if (word.contains("Z")) {
                viewModelScope.launch { context.setZWord(word) }
                NewBadgesToDisplay.add(Badge.PlayedZWord)
            }
            if (word.length == 5) {
                viewModelScope.launch { context.setPlayed5LetterWord(word) }
                NewBadgesToDisplay.add(Badge.Played5LetterWord)
            } else if (word.length == 6) {
                viewModelScope.launch {
                    context.setPlayed6LetterWord(word)
                    context.addDisplayedBadge(Badge.Played5LetterWord)
                }
                NewBadgesToDisplay.add(Badge.Played6LetterWord)
            } else if (word.length == 7) {
                viewModelScope.launch {
                    context.setPlayed7LetterWord(word)
                    context.addDisplayedBadge(Badge.Played5LetterWord)
                    context.addDisplayedBadge(Badge.Played6LetterWord)
                }
                NewBadgesToDisplay.add(Badge.Played7LetterWord)
            } else if (word.length > 7) {
                viewModelScope.launch {
                    context.setPlayed8LetterWord(word)
                    context.addDisplayedBadge(Badge.Played5LetterWord)
                    context.addDisplayedBadge(Badge.Played6LetterWord)
                    context.addDisplayedBadge(Badge.Played7LetterWord)
                }
                NewBadgesToDisplay.add(Badge.Played8LetterWord)
            }
        }
    }

    internal open fun setBadgesOnGameWon(context: Context) {
        if (boardWidth == 5 && boardHeight == 4 || boardWidth == 5 && boardHeight == 5) {
            viewModelScope.launch { context.setWonLightning() }
            NewBadgesToDisplay.add(Badge.WonLightning)
        } else if (boardWidth == 6 && boardHeight == 6 || boardWidth == 7 && boardHeight == 5) {
            viewModelScope.launch {
                context.setWonRapid()
                NewBadgesToDisplay.add(Badge.WonRapid)
            }
        } else if (boardWidth == 8) {
            viewModelScope.launch { context.setWonClassic() }
            NewBadgesToDisplay.add(Badge.WonClassic)
        }
    }
}
