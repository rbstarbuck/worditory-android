package com.example.worditory

import android.content.Intent
import android.os.Bundle
import com.example.worditory.notification.NotificationService
import com.example.worditory.notification.Notifications
import com.example.worditory.saved.SavedGamesService

class MainActivity: MainActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Notifications.createNotificationChannels(this)
    }

    override fun onStart() {
        super.onStart()

        startService(Intent(this, SavedGamesService::class.java))
        stopService(Intent(this, NotificationService::class.java))
    }

    override fun onStop() {
        stopService(Intent(this, NotificationService::class.java))
        startService(Intent(this, NotificationService::class.java))

        super.onStop()
    }
}