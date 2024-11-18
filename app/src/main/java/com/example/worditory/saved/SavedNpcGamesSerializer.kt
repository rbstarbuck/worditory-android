package com.example.worditory.saved

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.game.NpcGameModel
import java.io.InputStream
import java.io.OutputStream

class SavedNpcGamesSerializer: Serializer<SavedNpcGames> {
    override val defaultValue: SavedNpcGames
        get() = SavedNpcGames.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedNpcGames {
        return SavedNpcGames.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedNpcGames, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.savedNpcGamesDataStore: DataStore<SavedNpcGames> by dataStore(
    fileName = "saved-npc-games.pb",
    serializer = SavedNpcGamesSerializer()
)

suspend fun Context.removeSavedNpcGame(gameId: String) {
    savedNpcGamesDataStore.updateData { savedGames ->
        SavedNpcGames.newBuilder()
            .addAllGames(savedGames.gamesList.filter { it.game.id != gameId })
            .build()
    }
}

suspend fun Context.addSavedNpcGame(npcGame: NpcGameModel) {
    savedNpcGamesDataStore.updateData { savedGames ->
        val newSavedGames =
            (savedGames.gamesList.filter { it.game.id != npcGame.game.id } + npcGame)
            .sortedBy { !it.game.isPlayerTurn }
        SavedNpcGames.newBuilder()
            .addAllGames(newSavedGames)
            .build()
    }
}
