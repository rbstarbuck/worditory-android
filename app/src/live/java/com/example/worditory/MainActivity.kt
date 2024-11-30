package com.example.worditory

import android.content.Intent
import android.os.Bundle
import com.example.worditory.notification.NotificationService
import com.example.worditory.notification.Notifications
import com.example.worditory.saved.SavedGamesService

class MainActivity: MainActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Notifications.createChannels(this)
    }

    override fun onStart() {
        super.onStart()
        stopService(Intent(this, NotificationService::class.java))
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this, SavedGamesService::class.java))
    }

    override fun onPause() {
        stopService(Intent(this, SavedGamesService::class.java))
        super.onPause()
    }

    override fun onStop() {
        startService(Intent(this, NotificationService::class.java))
        super.onStop()
    }
}