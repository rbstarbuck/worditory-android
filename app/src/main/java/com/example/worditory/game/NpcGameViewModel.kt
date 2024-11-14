package com.example.worditory.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.badge.Badge
import com.example.worditory.badge.NewBadgesToDisplay
import com.example.worditory.audio.AudioPlayer
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.game.npc.NonPlayerCharacter
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.ADVANCED
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.BEGINNER
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.INTERMEDIATE
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.SUPER_ADVANCED
import com.example.worditory.game.npc.NpcModel.Spec.OverallSkillLevel.UNRECOGNIZED
import com.example.worditory.game.npc.addBeatenOpponent
import com.example.worditory.incrementGamesPlayed
import com.example.worditory.incrementGamesWon
import com.example.worditory.saved.addSavedNpcGame
import com.example.worditory.saved.removeSavedNpcGame
import com.example.worditory.setWonAgainsIntermediate
import com.example.worditory.setWonAgainstAdvanced
import com.example.worditory.setWonAgainstBeginner
import com.example.worditory.setWonAgainstSuperAdvanced
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import kotlin.random.Random

internal class NpcGameViewModel(
    npcModel: NpcGameModel,
    context: Context,
    navController: NavController,
    player1AvatarIdFlow: Flow<Int>,
    player2AvatarIdFlow: Flow<Int>
): GameViewModelBase(npcModel.game, navController, player1AvatarIdFlow, player2AvatarIdFlow) {
    private val opponent = npcModel.opponent

    private val nonPlayerCharacter = NonPlayerCharacter(npcModel.opponent, board)

    init {
        if (!isPlayerTurn) {
            playNpcWord(context)
        }
    }

    val npcModel: NpcGameModel
        get() = NpcGameModel.newBuilder()
            .setGame(model)
            .setOpponent(opponent)
            .build()

    override fun onPlayButtonClick(context: Context): Boolean {
        if (super.onPlayButtonClick(context)) {
            isPlayerTurn = false
            if (gameOverState == GameOver.State.IN_PROGRESS) {
                playNpcWord(context)
                return true
            }
        }
        return false
    }

    override fun onPassTurn(context: Context) {
        super.onPassTurn(context)
        playNpcWord(context)
    }

    override fun setBadgesOnGameWon(context: Context) {
        super.setBadgesOnGameWon(context)

        viewModelScope.launch {
            when (opponent.spec.overallSkillLevel) {
                BEGINNER -> {
                    viewModelScope.launch { context.setWonAgainstBeginner() }
                    NewBadgesToDisplay.add(Badge.WonAgainstBeginner)
                }
                INTERMEDIATE -> {
                    viewModelScope.launch { context.setWonAgainsIntermediate() }
                    NewBadgesToDisplay.add(Badge.WonAgainstIntermediate)
                }
                ADVANCED -> {
                    viewModelScope.launch { context.setWonAgainstAdvanced() }
                    NewBadgesToDisplay.add(Badge.WonAgainstAdvanced)
                }
                SUPER_ADVANCED -> {
                    viewModelScope.launch { context.setWonAgainstSuperAdvanced() }
                    NewBadgesToDisplay.add(Badge.WonAgainstSuperAdvanced)
                }
                UNRECOGNIZED -> throw InvalidParameterException("Unrecognized overall skill level")
            }

            context.addBeatenOpponent(opponent.spec)
        }
    }

    private fun playNpcWord(context: Context) {
        val npcWord = nonPlayerCharacter.findWordToPlay()

        viewModelScope.launch {
            delay(Random.nextLong(from = 1500L, until = 2500L))

            board.word.withDrawPathTweenDuration(millis = npcWord.tiles.size * 350) {
                for (tile in npcWord.tiles) {
                    board.word.onSelectTile(tile, Game.Player.PLAYER_2)
                }

                delay(4000L)
            }

            AudioPlayer.wordPlayed(npcWord.tiles.size)
            board.playWord(Game.Player.PLAYER_2)
            onWordPlayed(context)
        }
    }

    override fun saveGame(context: Context) {
        val currentGameOverState = gameOverState

        viewModelScope.launch {
            if (currentGameOverState == GameOver.State.IN_PROGRESS) {
                context.addSavedNpcGame(npcModel)
            } else {
                context.removeSavedNpcGame(id)
                context.incrementGamesPlayed()
                if (currentGameOverState == GameOver.State.WIN) {
                    context.incrementGamesWon()
                }
            }
        }
    }
}
