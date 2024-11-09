package com.example.worditory.game.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worditory.composable.Coordinates
import com.example.worditory.game.Game
import com.example.worditory.game.board.BoardViewModel
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.game.npc.NpcModel
import com.example.worditory.game.scoreboard.ScoreBoardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class TutorialViewModel(
    val board: BoardViewModel,
    val scoreBoard: ScoreBoardViewModel
): ViewModel() {
    private var boardModel = board.model

    internal val _enabledStateFlow = MutableStateFlow(false)
    internal val enabledStateFlow = _enabledStateFlow.asStateFlow()
    internal var enabled: Boolean
        get() = enabledStateFlow.value
        set(value) {
            if (value != enabled) {
                if (value) {
                    boardModel = board.model
                    board.word.model = WordModel()
                } else {
                    board.model = boardModel
                }
                _enabledStateFlow.value = value
                tutorialSegment1.enabled = value
            }
        }

    internal val tutorialSegment1 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment2 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment3 = TutorialSegmentViewModel(Coordinates.PlayButton)
    internal val tutorialSegment4 = TutorialSegmentViewModel(Coordinates.Board)
    internal val tutorialSegment5 = TutorialSegmentViewModel(Coordinates.MenuButton)
    internal val tutorialSegment6 = TutorialSegmentViewModel(Coordinates.PlayerScore)
    internal val tutorialSegment7 = TutorialSegmentViewModel(Coordinates.ScoreToWin)
    internal val tutorialSegment8 = TutorialSegmentViewModel(Coordinates.MenuButton)

    private val exampleWordNpc = NonPlayerCharacter(
        model = NpcModel.newBuilder()
            .setSpec(NpcModel.Spec.newBuilder()
                .setVocabularyLevel(NpcModel.Spec.VocabularyLevel.LOW)
                .setDefenseOffenseLevel(NpcModel.Spec.DefenseOffenseLevel.OFFENSIVE)
                .setOverallSkillLevel(NpcModel.Spec.OverallSkillLevel.ADVANCED)
                .build())
            .setAvatar(0)
            .build(),
        board = board,
        player = Game.Player.PLAYER_1
    )

    private var exampleWord = WordModel()
    private var currentScore = board.computeScore()
    private var currentScoreToWin = scoreBoard.scoreToWin

    internal fun onNextSegment() {
        if (tutorialSegment1.enabled) {
            tutorialSegment1.enabled = false
            tutorialSegment2.enabled = true
        } else if (tutorialSegment2.enabled) {
            tutorialSegment2.enabled = false
            displayExampleWord {
                tutorialSegment3.enabled = true
            }
        } else if (tutorialSegment3.enabled) {
            tutorialSegment3.enabled = false
            playExampleWord {
                tutorialSegment4.enabled = true
            }
        } else if (tutorialSegment4.enabled) {
            tutorialSegment4.enabled = false
            tutorialSegment5.enabled = true
        } else if (tutorialSegment5.enabled) {
            tutorialSegment5.enabled = false
            tutorialSegment6.enabled = true
        } else if (tutorialSegment6.enabled) {
            tutorialSegment6.enabled = false
            updatePlayerScore {
                tutorialSegment7.enabled = true
            }
        } else if (tutorialSegment7.enabled) {
            tutorialSegment7.enabled = false
            decrementScoreToWin {
                tutorialSegment8.enabled = true
            }
        } else if (tutorialSegment8.enabled) {
            tutorialSegment8.enabled = false
            exitTutorial()
        }
    }

    internal fun onPreviousSegment() {
        if (tutorialSegment1.enabled) {
            exitTutorial()
        } else if (tutorialSegment2.enabled) {
            tutorialSegment2.enabled = false
            tutorialSegment1.enabled = true
        } else if (tutorialSegment3.enabled) {
            tutorialSegment3.enabled = false
            rewindDisplayExampleWord()
            tutorialSegment2.enabled = true
        } else if (tutorialSegment4.enabled) {
            tutorialSegment4.enabled = false
            rewindPlayExampleWord()
            tutorialSegment3.enabled = true
        } else if (tutorialSegment5.enabled) {
            tutorialSegment5.enabled = false
            tutorialSegment4.enabled = true
        } else if (tutorialSegment6.enabled) {
            tutorialSegment6.enabled = false
            tutorialSegment5.enabled = true
        } else if (tutorialSegment7.enabled) {
            tutorialSegment7.enabled = false
            rewindUpdatePlayerScore()
            tutorialSegment6.enabled = true
        } else if (tutorialSegment8.enabled) {
            tutorialSegment8.enabled = false
            rewindDecrementScoreToWin()
            tutorialSegment7.enabled = true
        }
    }

    private fun displayExampleWord(onComplete: () -> Unit) {
        exampleWord = exampleWordNpc.findWordToPlay()
        viewModelScope.launch {
            delay(500)
            board.word.model = exampleWord
            delay(2500)
            onComplete()
        }
    }

    private fun rewindDisplayExampleWord() {
        board.word.model = WordModel()
    }

    internal fun playExampleWord(onComplete: () -> Unit) {
        currentScore = board.computeScore()
        viewModelScope.launch {
            delay(500)
            board.playWord(Game.Player.PLAYER_1)
            delay(2500)
            onComplete()
        }
    }

    private fun rewindPlayExampleWord() {
        board.model = boardModel
        board.word.model = exampleWord
    }

    private fun updatePlayerScore(onComplete: () -> Unit) {
        viewModelScope.launch {
            delay(500)
            scoreBoard.score = board.computeScore()
            delay(1500)
            onComplete()
        }
    }

    private fun rewindUpdatePlayerScore() {
        scoreBoard.score = currentScore
    }

    private fun decrementScoreToWin(onComplete: () -> Unit) {
        currentScoreToWin = scoreBoard.scoreToWin
        viewModelScope.launch {
            delay(500)
            scoreBoard.decrementScoreToWin()
            delay(1500)
            onComplete()
        }
    }

    private fun rewindDecrementScoreToWin() {
        scoreBoard.scoreToWin = currentScoreToWin
    }

    private fun exitTutorial() {
        if (enabled) {
            tutorialSegment1.enabled = false
            tutorialSegment2.enabled = false
            tutorialSegment3.enabled = false
            tutorialSegment4.enabled = false
            tutorialSegment5.enabled = false
            tutorialSegment6.enabled = false
            tutorialSegment7.enabled = false
            tutorialSegment8.enabled = false
            enabled = false
            rewindDecrementScoreToWin()
            rewindUpdatePlayerScore()
            rewindPlayExampleWord()
            rewindDisplayExampleWord()
        }
    }
}
