package com.example.worditory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worditory.auth.AuthenticationView
import com.example.worditory.badge.BadgesDialogView
import com.example.worditory.badge.BadgesRowView
import com.example.worditory.badge.NewBadgesView
import com.example.worditory.chooser.avatar.AvatarChooserDialog
import com.example.worditory.composable.WorditoryConfirmationDialogView
import com.example.worditory.composable.WorditoryInfoDialogView
import com.example.worditory.composable.WorditoryOutlinedButton
import com.example.worditory.friends.FriendCardView
import com.example.worditory.friends.SavedFriendsView
import com.example.worditory.friends.request.AcceptFriendRequestView
import com.example.worditory.friends.request.SendFriendRequestView
import com.example.worditory.header.HeaderView
import com.example.worditory.saved.SavedGamesView

@Composable
internal fun MainView(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderView(
                    modifier = Modifier.fillMaxWidth(),
                    onAvatarClick = {
                        viewModel.avatarChooser.enabled = true
                    }
                )

                BadgesRowView(
                    viewModel = viewModel.badgesRow,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(15.dp))
            }


            SavedGamesView(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel.savedGames,
                whenIsSavedLiveGame = { viewModel.requestNotificationPermission(context) },
                onDeleteClick = { gameId ->
                    viewModel.deleteSavedGame.show(
                        onConfirmed = { viewModel.deleteSavedGame(gameId, context) }
                    )
                }
            )

            SavedFriendsView(
                viewModel = viewModel.savedFriends,
                modifier = Modifier.fillMaxWidth(),
                onAddFriendClick = { viewModel.sendFriendRequest.enabled = true },
                onFriendClick = { friend ->
                    viewModel.friendCard.friend = friend
                    viewModel.friendCard.enabled = true
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WorditoryOutlinedButton(
                    onClick = { viewModel.onPlayLiveGameClick() },
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.play_live),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                WorditoryOutlinedButton(
                    onClick = { viewModel.onPlayGameClicked() },
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.play_against_ai),
                        color = colorResource(R.color.font_color_dark),
                        fontSize = 24.sp
                    )
                }
            }
        }

        BadgesDialogView(viewModel.badgesRow)

        AvatarChooserDialog(viewModel.avatarChooser)

        WorditoryConfirmationDialogView(
            viewModel = viewModel.deleteSavedGame,
            text = stringResource(R.string.delete_saved_game_question)
        )

        SendFriendRequestView(
            viewModel = viewModel.sendFriendRequest,
            onRequestSent = { viewModel.friendRequestSent.show() }
        )

        FriendCardView(viewModel = viewModel.friendCard)

        WorditoryInfoDialogView(
            viewModel = viewModel.friendRequestSent,
            text = stringResource(R.string.friend_request_sent)
        )

        NewBadgesView(viewModel.newBadges)

        AcceptFriendRequestView(viewModel.acceptFriendRequest)

        AuthenticationView(viewModel.authentication)

        WorditoryConfirmationDialogView(
            viewModel = viewModel.notificationPermission,
            text = stringResource(R.string.request_notification_permission),
            cancelButtonText = stringResource(R.string.no_thanks)
        )
    }
}