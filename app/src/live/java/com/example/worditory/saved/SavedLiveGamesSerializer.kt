package com.example.worditory.saved

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.game.GameModel
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.copy
import java.io.InputStream
import java.io.OutputStream

class SavedLiveGamesSerializer: Serializer<SavedLiveGames> {
    override val defaultValue: SavedLiveGames
        get() = SavedLiveGames.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedLiveGames {
        return SavedLiveGames.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedLiveGames, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.savedLiveGamesDataStore: DataStore<SavedLiveGames> by dataStore(
    fileName = "saved-live-games.pb",
    serializer = SavedLiveGamesSerializer()
)

suspend fun Context.removeSavedLiveGame(gameId: String) {
    savedLiveGamesDataStore.updateData { savedGames ->
        SavedLiveGames.newBuilder()
            .addAllGames(savedGames.gamesList.filter { it.game.id == gameId })
            .build()
    }
}

suspend fun Context.addSavedLiveGame(liveGame: LiveGameModel) {
    savedLiveGamesDataStore.updateData { savedGames ->
        SavedLiveGames.newBuilder()
            .addGames(liveGame)
            .addAllGames(savedGames.gamesList.filter { it.game.id != liveGame.game.id })
            .build()
    }
}

suspend fun Context.setIsPlayerTurnOnSavedLiveGame(gameId: String) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()
//        val newGame = LiveGameModel.newBuilder()
//            .setIsPlayer1(oldGame.isPlayer1)
//            .setOpponent(oldGame.opponent)
//            .setPlayedWordCount(oldGame.playedWordCount)
//            .setGame(GameModel.newBuilder()
//                .setIsPlayerTurn(true)
//                .setId(oldGame.game.id)
//                .setColorScheme(oldGame.game.colorScheme)
//                .setScoreToWin(oldGame.game.scoreToWin)
//                .setBoard(oldGame.game.board)
//                .build()
//            ).build()
        addSavedLiveGame(oldGame)
    }
}
