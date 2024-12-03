package com.example.worditory.friends

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

internal class SavedFriendsSerializer: Serializer<SavedFriends> {
    override val defaultValue: SavedFriends
        get() = SavedFriends.newBuilder().build()

    override suspend fun readFrom(input: InputStream): SavedFriends {
        return SavedFriends.parseFrom(input)
    }

    override suspend fun writeTo(t: SavedFriends, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.savedFriendsDataStore: DataStore<SavedFriends> by dataStore(
    fileName = "saved-friends.pb",
    serializer = SavedFriendsSerializer()
)

internal suspend fun Context.addSavedFriend(friend: Friend) {
    savedFriendsDataStore.updateData { savedFriends ->
        val friends = mutableListOf<Friend>()
        friends.addAll(savedFriends.friendsList.filter { it.uid != friend.uid })
        friends.add(friend)
        friends.sortByDescending { it.timestamp }

        SavedFriends.newBuilder().addAllFriends(friends).build()
    }
}

internal suspend fun Context.removeSavedFriend(uid: String) {
    savedFriendsDataStore.updateData { savedFriends ->
        SavedFriends.newBuilder()
            .addAllFriends(savedFriends.friendsList.filter { it.uid != uid })
            .build()
    }
}