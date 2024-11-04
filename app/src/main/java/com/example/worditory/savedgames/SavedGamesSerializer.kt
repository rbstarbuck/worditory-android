package com.example.worditory.savedgames

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.SavedGames
import com.example.worditory.game.GameModel
import java.io.InputStream
import java.io.OutputStream

class SavedGamesSerializer: Serializer<SavedGames> {
    override val defaultValue: SavedGames
        get() = SavedGames.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedGames {
        return SavedGames.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedGames, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.savedGamesDataStore: DataStore<SavedGames> by dataStore(
    fileName = "saved-games.pb",
    serializer = SavedGamesSerializer()
)

suspend fun Context.removeSavedGame(gameId: Long) {
    savedGamesDataStore.updateData { savedGames ->
        SavedGames.newBuilder()
            .addAllGames(savedGames.gamesList.filter { it.id != gameId })
            .build()
    }
}

suspend fun Context.addSavedGame(game: GameModel) {
    savedGamesDataStore.updateData { savedGames ->
        SavedGames.newBuilder()
            .addGames(game)
            .addAllGames(savedGames.gamesList)
            .build()
    }
}
