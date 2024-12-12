package com.example.worditory.friends

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.worditory.database.DatabaseRepository
import com.example.worditory.friends.request.addFriendRequest
import com.example.worditory.friends.request.clearFriendRequests
import com.example.worditory.friends.request.removeFriendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class FriendService: Service() {
    private lateinit var friendRequestListener: FriendRepository.FriendRequestListener
    private lateinit var friendDataListener: FriendRepository.FriendDataListener

    private lateinit var listenerJob: Job

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val scope = CoroutineScope(Dispatchers.Default)

        listenerJob = scope.launch {
            clearFriendRequests()

            friendRequestListener = FriendRepository.listenForFriendRequests(
                onRequestAdded = { uid, user ->
                    DatabaseRepository.getServerTime { timestamp ->
                        scope.launch {
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
                    scope.launch {
                        removeFriendRequest(uid)
                    }
                }
            )

            friendDataListener = FriendRepository.listenForFriendDataChanges { uid, user ->
                if (user != null) {
                    DatabaseRepository.getServerTime { timestamp ->
                        scope.launch {
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
                    scope.launch {
                        removeSavedFriend(uid)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        listenerJob.cancel()
        FriendRepository.removeListener(friendRequestListener)
        FriendRepository.removeListener(friendDataListener)

        super.onDestroy()
    }
}