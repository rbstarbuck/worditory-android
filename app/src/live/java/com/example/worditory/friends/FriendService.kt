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
    private lateinit var friendDataListener: FriendRepository.FriendDataListener

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            clearFriendRequests()

            friendRequestListener = FriendRepository.listenForFriendRequests(
                onRequestAdded = { uid, user ->
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

            friendDataListener = FriendRepository.listenForFriendDataChanges { uid, user ->
                if (user != null) {
                    DatabaseRepository.getServerTime { timestamp ->
                        GlobalScope.launch {
                            addSavedFriend(
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
                } else {
                    GlobalScope.launch {
                        removeSavedFriend(uid)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        FriendRepository.removeListener(friendRequestListener)
        FriendRepository.removeListener(friendDataListener)

        super.onDestroy()
    }
}