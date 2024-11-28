package com.example.worditory.saved

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGameModel
import com.example.worditory.game.gameover.GameOver
import com.example.worditory.incrementGamesPlayed
import com.example.worditory.incrementGamesWon
import com.example.worditory.user.UserRepository
import kotlinx.coroutines.flow.first
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
        SavedLiveGames.newBuilder()
            .addAllGames(savedGames.gamesList.filter { it.game.id != liveGame.game.id })
            .addGames(liveGame)
            .build()
    }
}

internal suspend fun Context.setGameOver(gameId: String, gameOverState: GameOver.State) {
    val savedGames = savedLiveGamesDataStore.data.first()
    val oldGameList = savedGames.gamesList.filter { it.game.id == gameId }

    if (oldGameList.isNotEmpty()) {
        val oldGame = oldGameList.first()

        if (!oldGame.isGameOver) {
            val newGame = oldGame.toBuilder().setIsGameOver(true).build()
            addSavedLiveGame(newGame)

            incrementGamesPlayed()
            UserRepository.incrementGamesPlayed()
            if (gameOverState == GameOver.State.WIN) {
                incrementGamesWon()
                UserRepository.incrementGamesWon()
            }
        }
    }
}
