package com.example.worditory.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.saved.savedLiveGamesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class NotificationService: Service() {
    private var notifiers = emptyList<GameNotifier>()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val context = this

        GlobalScope.launch {
            savedLiveGamesDataStore.data.collect { savedGames ->
                notifiers = savedGames.gamesList.map { GameNotifier(it, context) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        for (notifier in notifiers) {
            notifier.removeListener()
        }

        notifiers = emptyList()
    }

    companion object {
        internal var notificationsEnabled = false
    }
}