package com.example.worditory.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.saved.savedLiveGamesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class NotificationService: Service() {
    private var notifiers = emptyList<GameNotifier>()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val context = this

        isWarmingUp = true
        GlobalScope.launch {
            savedLiveGamesDataStore.data.collect { savedGames ->
                notifiers = savedGames.gamesList.map { GameNotifier(it, context) }
            }
        }

        GlobalScope.launch{
            delay(3000L)
            isWarmingUp = false
        }
    }

    override fun onDestroy() {
        for (notifier in notifiers) {
            notifier.removeListeners()
        }

        notifiers = emptyList()

        super.onDestroy()
    }

    companion object {
        internal var isWarmingUp = true
            private set
    }
}