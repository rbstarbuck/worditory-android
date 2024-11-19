package com.example.worditory.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.badge.Badge
import com.example.worditory.badge.NewBadgesToDisplay
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
import kotlinx.coroutines.launch
import java.security.InvalidParameterException
import kotlin.random.Random

internal class NpcGameViewModel(
    npcModel: NpcGameModel,
    navController: NavController,
    context: Context
): GameViewModelBase(
    model = npcModel.game,
    navController = navController,
    context = context,
    avatarIdPlayer2 = npcModel.opponent.avatar,
    displayNamePlayer2 = context.getString(
        NonPlayerCharacter.avatarIdToDisplayNameResId(npcModel.opponent.avatar)
    )
) {
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

            board.updateLettersForWord()
            board.updateOwnershipsForWord(Game.Player.PLAYER_2)
            board.playWord(Game.Player.PLAYER_2)
            updateScoreboard()
            isPlayerTurn = !checkForGameOver()
            saveGame(context)
        }
    }

    override fun saveGame(context: Context) {
        viewModelScope.launch {
            when (gameOverState) {
                GameOver.State.IN_PROGRESS -> context.addSavedNpcGame(npcModel)
                else -> context.removeSavedNpcGame(id)
            }
        }
    }
}
