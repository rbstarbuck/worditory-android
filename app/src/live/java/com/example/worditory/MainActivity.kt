package com.example.worditory

import android.content.Intent
import android.os.Bundle
import com.example.worditory.notification.NotificationService
import com.example.worditory.notification.Notifications

class MainActivity: MainActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Notifications.createNotificationChannels(this)
    }

    override fun onStart() {
        super.onStart()

        stopService(Intent(this, NotificationService::class.java))
    }

    override fun onStop() {
        startService(Intent(this, NotificationService::class.java))

        super.onStop()
    }
}