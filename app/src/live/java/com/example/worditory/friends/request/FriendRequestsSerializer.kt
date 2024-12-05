package com.example.worditory.friends.request

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.worditory.friends.Friend
import com.example.worditory.friends.SavedFriends
import kotlinx.coroutines.flow.first
import java.io.InputStream
import java.io.OutputStream

internal class FriendRequestsSerializer: Serializer<SavedFriends> {
    override val defaultValue: SavedFriends
        get() = SavedFriends.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedFriends {
        return SavedFriends.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedFriends, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.friendRequestsDataStore: DataStore<SavedFriends> by dataStore(
    fileName = "friend-requests.pb",
    serializer = FriendRequestsSerializer()
)

internal suspend fun Context.addFriendRequest(friend: Friend) {
    friendRequestsDataStore.updateData { friendRequests ->
        val friends = mutableListOf<Friend>()
        friends.addAll(friendRequests.friendsList.filter { it.uid != friend.uid })
        friends.add(friend)
        friends.sortBy { it.timestamp }

        SavedFriends.newBuilder().addAllFriends(friends).build()
    }
}

internal suspend fun Context.removeFriendRequest(uid: String) {
    friendRequestsDataStore.updateData { friendRequests ->
        SavedFriends.newBuilder()
            .addAllFriends(friendRequests.friendsList.filter { it.uid != uid })
            .build()
    }
}

internal suspend fun Context.clearFriendRequests() {
    friendRequestsDataStore.updateData { SavedFriends.newBuilder().build() }
}

internal suspend fun Context.isFriendsWith(uid: String): Boolean =
    friendRequestsDataStore.data.first().friendsList.filter { it.uid == uid }.isNotEmpty()