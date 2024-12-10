package com.example.worditory.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.friends.FriendRepository
import com.example.worditory.match.MatchRepository

internal class PersistentNotificationService: Service() {
    private lateinit var friendRequestListener: FriendRepository.FriendRequestListener
    private lateinit var challengeListener: MatchRepository.ChallengeListener

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        friendRequestListener = FriendRepository.listenForFriendRequests(
            onRequestAdded = { uid, user ->
                Notifications.friendRequestReceived(
                    uid = uid,
                    displayName = user.displayName ?: "User",
                    avatarId = user.avatarId ?: 0,
                    context = this
                )
            },
            onRequestRemoved = {}
        )

        challengeListener = MatchRepository.listenForChallenges { challenge ->
            Notifications.challengeReceived(
                gameId = challenge.gameId,
                displayName = challenge.user.displayName ?: "User",
                avatarId = challenge.user.avatarId ?: 0,
                context = this
            )
        }
    }

    override fun onDestroy() {
        FriendRepository.removeListener(friendRequestListener)
        MatchRepository.removeListener(challengeListener)

        super.onDestroy()
    }
}