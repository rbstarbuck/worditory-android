package com.example.worditory.friends

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.database.DatabaseRepository
import com.example.worditory.friends.request.addFriendRequest
import com.example.worditory.friends.request.clearFriendRequests
import com.example.worditory.friends.request.removeFriendRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class FriendService: Service() {
    private lateinit var friendRequestListener: FriendRepository.FriendRequestListener

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            clearFriendRequests()

            friendRequestListener = FriendRepository.listenForFriendRequests(
                onRequestAdded = { uidAndUser ->
                    val uid = uidAndUser.first
                    val user = uidAndUser.second

                    DatabaseRepository.getServerTime { timestamp ->
                        GlobalScope.launch {
                            addFriendRequest(
                                Friend.newBuilder()
                                    .setUid(uid)
                                    .setDisplayName(user.displayName ?: "")
                                    .setAvatarId(user.avatarId ?: 0)
                                    .setGamesPlayed(user.gamesPlayed ?: 0)
                                    .setGameWon(user.gamesWon ?: 0)
                                    .setRank(user.rank ?: 1500)
                                    .setTimestamp(timestamp)
                                    .build()
                            )
                        }
                    }
                },
                onRequestRemoved = { uid ->
                    GlobalScope.launch {
                        removeFriendRequest(uid)
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        FriendRepository.removeListener(friendRequestListener)

        super.onDestroy()
    }
}