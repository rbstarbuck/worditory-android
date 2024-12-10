package com.example.worditory.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.friends.FriendRepository

internal class PersistentNotificationService: Service() {
    private lateinit var friendRequestListener: FriendRepository.FriendRequestListener

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        friendRequestListener = FriendRepository.listenForFriendRequests(
            onRequestAdded = { uid, user ->
                Notifications.friendRequestReceived(
                    uid = uid,
                    displayName = user.displayName ?: "",
                    avatarId = user.avatarId ?: 0,
                    context = this
                )
            },
            onRequestRemoved = {}
        )
    }

    override fun onDestroy() {

        super.onDestroy()
    }
}