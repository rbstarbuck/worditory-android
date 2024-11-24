package com.example.worditory

import android.content.Intent
import android.os.Bundle
import com.example.worditory.notification.NotificationService
import com.example.worditory.notification.Notifications

class MainActivity: MainActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Notifications.createNotificationChannels(this)
        startService(Intent(this, NotificationService::class.java))
    }

    override fun onStart() {
        super.onStart()

        NotificationService.notificationsEnabled = false
    }

    override fun onStop() {
        NotificationService.notificationsEnabled = true

        super.onStop()
    }
}