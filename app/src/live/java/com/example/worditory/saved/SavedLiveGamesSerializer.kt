package com.example.worditory.saved

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.incrementGamesPlayed
import com.example.worditory.incrementGamesWon
import java.io.InputStream
import java.io.OutputStream

internal class SavedLiveGamesSerializer: Serializer<SavedLiveGames> {
    override val defaultValue: SavedLiveGames
        get() = SavedLiveGames.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedLiveGames {
        return SavedLiveGames.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedLiveGames, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.savedLiveGamesDataStore: DataStore<SavedLiveGames> by dataStore(
    fileName = "saved-live-games.pb",
    serializer = SavedLiveGamesSerializer()
)

internal suspend fun Context.removeSavedLiveGame(gameId: String) {
    savedLiveGamesDataStore.updateData { savedGames ->
        SavedLiveGames.newBuilder()
            .addAllGames(savedGames.gamesList.filter { it.game.id != gameId })
            .build()
    }
}

internal suspend fun Context.addSavedLiveGame(liveGame: LiveGameModel) {
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

internal suspend fun Context.setGameOver(gameId: String, gameOverState: GameOver.State) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()

        if (!oldGame.isGameOver) {
            val newGame = oldGame.toBuilder().setIsGameOver(true).build()
            addSavedLiveGame(newGame)

            incrementGamesPlayed()
            if (gameOverState == GameOver.State.WIN) {
                incrementGamesWon()
            }
        }
    }
}

internal suspend fun Context.setIsPlayerTurnOnSavedLiveGame(gameId: String) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()

        if (!oldGame.game.isPlayerTurn) {
            val newGame = oldGame.toBuilder()
                .setGame(
                    oldGame.game.toBuilder()
                        .setIsPlayerTurn(true)
                ).build()

            addSavedLiveGame(newGame)
        }
    }
}

internal suspend fun Context.setTimestampOnSavedLiveGame(gameId: String, timestamp: Long) {
    savedLiveGamesDataStore.data.collect { savedGames ->
        val oldGame = savedGames.gamesList.filter { it.game.id == gameId }.first()

        if (oldGame.timestamp != timestamp) {
            val newGame = oldGame.toBuilder().setTimestamp(timestamp).build()
            addSavedLiveGame(newGame)
        }
    }
}
