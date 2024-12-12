package com.example.worditory.notification

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.saved.savedLiveGamesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class NotificationService: Service() {
    private var notifiers = emptyList<GameNotifier>()

    private var isWarmingUp = true

    private val isActivityRunning: () -> Boolean = {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        isWarmingUp || if (manager.runningAppProcesses.isNotEmpty()) {
            manager.runningAppProcesses[0].importance == IMPORTANCE_FOREGROUND
        } else {
            false
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        isWarmingUp = true

        GlobalScope.launch {
            savedLiveGamesDataStore.data.collect { savedGames ->
                clearNotifiers()
                notifiers = savedGames.gamesList.map {
                    GameNotifier(it, isActivityRunning, this@NotificationService)
                }
            }
        }

        GlobalScope.launch{
            delay(3000L)
            isWarmingUp = false
        }
    }

    override fun onDestroy() {
        clearNotifiers()

        super.onDestroy()
    }

    private fun clearNotifiers() {
        for (notifier in notifiers) {
            notifier.removeListeners()
        }

        notifiers = emptyList()
    }
}