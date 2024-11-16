package com.example.worditory.game

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.game.board.tile.asLetter
import com.example.worditory.game.board.word.WordModel
import com.example.worditory.game.word.PlayedWordRepoModel
import com.example.worditory.game.word.WordRepository
import com.example.worditory.saved.addSavedLiveGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class LiveGameViewModel(
    liveModel: LiveGameModel,
    navController: NavController,
    context: Context
): GameViewModelBase(
    model = liveModel.game,
    navController = navController,
    context = context,
    avatarIdPlayer2 = liveModel.opponent?.avatarId ?: 0
) {
    private var playedWordCount = liveModel.playedWordCount
    private var isPlayer1 = liveModel.isPlayer1
    private val latestWordListener: WordRepository.LatestWordListener

    private val liveModel: LiveGameModel
        get() = LiveGameModel.newBuilder()
            .setIsPlayer1(isPlayer1)
            .setPlayedWordCount(playedWordCount)
            .setOpponent(OpponentModel.newBuilder()
                .setDisplayName("") // TODO(handle display name)
                .setAvatarId(scoreBoard.scorePlayer2.avatarId.value)
            )
            .setGame(GameModel.newBuilder()
                .setId(id)
                .setIsPlayerTurn(isPlayerTurn)
                .setScoreToWin(scoreBoard.scoreToWin)
                .setColorScheme(colorScheme.model)
                .setBoard(board.model)
                .build()
            ).build()

    init {
        if (liveModel.opponent != null) {
            scoreBoard.scorePlayer2.avatarId.value
        }
        latestWordListener = WordRepository.listenForLatestWord(
            gameId = id,
            onNewWord = { word ->
                if (word.index!! >= playedWordCount) {
                    onNewWord(word)
                }
            },
            onError = {}
        )
    }

    override fun onPlayButtonClick(context: Context): Boolean {
        val word = board.word.model

        if (super.onPlayButtonClick(context)) {
            WordRepository.playWord(id, word, board.model, playedWordCount++)
            return true
        }

        return false
    }

    override fun saveGame(context: Context) {
        viewModelScope.launch { context.addSavedLiveGame(liveModel) }
    }

    private fun onNewWord(word: PlayedWordRepoModel) {
        ++playedWordCount
        board.word.model = WordModel()

        viewModelScope.launch {
            board.word.withDrawPathTweenDuration(millis = word.tiles!!.size * 350) {
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
            updateScoreboard()
            isPlayerTurn = !checkForGameOver()
        }
    }

    override fun onExitGame(context: Context) {
        super.onExitGame(context)
        WordRepository.removeListener(id, latestWordListener)
    }

    fun flipTileIndex(index: Int) = board.width * board.height - index - 1
}