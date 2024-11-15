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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class LiveGameViewModel(
    liveModel: LiveGameModel,
    navController: NavController,
    player1AvatarIsFlow: Flow<Int>,
    player2AvatarIdFlow: Flow<Int>
): GameViewModelBase(liveModel.game, navController, player1AvatarIsFlow, player2AvatarIdFlow) {
    private var playedWordCount = liveModel.playedWordCount
    private var isPlayer1 = liveModel.isPlayer1

    private val liveModel: LiveGameModel
        get() = LiveGameModel.newBuilder()
            .setIsPlayer1(isPlayer1)
            .setPlayedWordCount(playedWordCount)
            .setGame(GameModel.newBuilder()
                .setId(id)
                .setIsPlayerTurn(isPlayerTurn)
                .setScoreToWin(scoreBoard.scoreToWin)
                .setColorScheme(colorScheme.model)
                .setBoard(board.model)
                .build()
            ).build()

    init {
        WordRepository.listenForLatestWord(
            gameId = id,
            onNewWord = { onNewWord(it) },
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
        if (word.index!! >= playedWordCount) {
            val tiles = checkNotNull(word.tiles)

            ++playedWordCount
            board.word.model = WordModel()

            viewModelScope.launch {
                board.word.withDrawPathTweenDuration(millis = tiles.size * 350) {
                    for (repoTile in tiles) {
                        val tile = board.tiles[repoTile.index!!]
                        board.word.onSelectTile(tile, Game.Player.PLAYER_1)
                    }
                }

                for (repoTile in tiles) {
                    val tileIndex = if (isPlayer1) {
                        repoTile.index
                    } else {
                        board.width * board.height - repoTile.index!! - 1
                    }
                    val tile = board.tiles[repoTile.index!!]
                    val newLetter = repoTile.newLetter!!.asLetter()

                    tile.letter = newLetter
                    board.letterBag.exchangeForLetter(
                        oldLetter = tile.letter,
                        newLetter = newLetter
                    )
                }

                board.updateOwnershipsForWord(Game.Player.PLAYER_1)
                board.playWord(Game.Player.PLAYER_1)
                onWordPlayed()
            }

        }
    }
}