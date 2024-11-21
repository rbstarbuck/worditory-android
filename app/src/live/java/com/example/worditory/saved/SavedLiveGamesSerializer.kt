package com.example.worditory.saved

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.game.LiveGameModel
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
        val newSavedGames = mutableListOf<LiveGameModel>()
        newSavedGames.addAll(savedGames.gamesList.filter { it.game.id != liveGame.game.id })
        newSavedGames.add(liveGame)
        newSavedGames.sortByDescending { it.timestamp }
        newSavedGames.sortByDescending { it.game.isPlayerTurn }

        SavedLiveGames.newBuilder()
            .addAllGames(newSavedGames)
            .build()
    }
}

suspend fun Context.setIsPlayerTurnOnSavedLiveGame(gameId: String) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()
        val newGame = oldGame.toBuilder()
            .setGame(
                oldGame.game.toBuilder()
                    .setIsPlayerTurn(true)
                    .build()
            ).build()

        addSavedLiveGame(newGame)
    }
}

suspend fun Context.setTimestampOnSavedLiveGame(gameId: String, timestamp: Long) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()
        val newGame = oldGame.toBuilder().setTimestamp(timestamp).build()

        addSavedLiveGame(newGame)
    }
}
