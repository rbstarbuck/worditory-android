package com.example.worditory.saved

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SavedGamesService: Service() {
    private val savedGameUpdaters = mutableListOf<SavedGameUpdater>()
    private lateinit var job: Job

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Default + job)

        scope.launch {
            savedLiveGamesDataStore.data.collect { liveGames ->
                clear()
                savedGameUpdaters.addAll(liveGames.gamesList.map { liveGame ->
                    SavedGameUpdater(
                        liveGame = liveGame,
                        onDataChange = { onDataChange() }
                    )}
                )
                onDataChange()
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        clear()

        super.onDestroy()
    }

    private fun onDataChange() {
        savedGameUpdaters.sortBy { it.data.timestamp }
        savedGameUpdaters.sortByDescending { it.data.isPlayerTurn }
        _savedGamesStateFlow.value = savedGameUpdaters.map { it.data }
    }

    private fun clear() {
        for (savedGame in savedGameUpdaters) {
            savedGame.removeListeners()
        }
        savedGameUpdaters.clear()
    }

    companion object {
        private val _savedGamesStateFlow = MutableStateFlow(emptyList<SavedGameData>())
        internal val savedGamesStateFlow = _savedGamesStateFlow.asStateFlow()
    }
}