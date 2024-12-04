package com.example.worditory.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.worditory.R
import com.example.worditory.resourceid.getResourceId

@Composable
internal fun SavedFriendsView(
    viewModel: SavedFriendsViewModel,
    modifier: Modifier = Modifier,
    onAddFriendClick: () -> Unit
) {
    val context = LocalContext.current

    val savedFriendsData = remember { context.savedFriendsDataStore.data }
    val savedFriendsState = savedFriendsData.collectAsState(SavedFriends.newBuilder().build())
    val friends = savedFriendsState.value.friendsList

    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val itemWidth = this.maxWidth / 4.25f

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(
                count = friends.size + 1,
                key = { i -> if (i == 0) "addFriend" else friends[i - 1].uid }
            ) { i ->
                if (i == 0) {
                    SavedFriendsRowItemView(
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = itemWidth / 12f),
                        avatar = ImageVector.vectorResource(R.drawable.add_friend),
                        displayName = stringResource(R.string.add_friend),
                        shiftAvatar = false,
                        itemWidth = itemWidth
                    ) { onAddFriendClick() }
                } else {
                    val friend = friends[i - 1]
                    SavedFriendsRowItemView(
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = itemWidth / 12f),
                        avatar = ImageVector.vectorResource(getResourceId(friend.avatarId)),
                        displayName = friend.displayName,
                        shiftAvatar = true,
                        itemWidth = itemWidth
                    ) { }
                }
            }
        }
    }
}