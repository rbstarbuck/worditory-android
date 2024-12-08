package com.example.worditory

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.worditory.auth.AuthenticationViewModel
import com.example.worditory.challenge.ChallengeConfirmationDialogViewModel
import com.example.worditory.composable.WorditoryConfirmationDialogViewModel
import com.example.worditory.composable.WorditoryInfoDialogViewModel
import com.example.worditory.friends.FriendCardViewModel
import com.example.worditory.friends.FriendRepository
import com.example.worditory.friends.FriendService
import com.example.worditory.friends.SavedFriendsViewModel
import com.example.worditory.friends.request.AcceptFriendRequestViewModel
import com.example.worditory.friends.request.SendFriendRequestViewModel
import com.example.worditory.game.GameRepository
import com.example.worditory.game.LiveGame
import com.example.worditory.match.MatchRepository
import com.example.worditory.navigation.LiveScreen
import com.example.worditory.notification.Notifications
import com.example.worditory.saved.SavedGamesRepository
import com.example.worditory.saved.addSavedLiveGame
import com.example.worditory.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class MainViewModel(
    navController: NavController,
    context: Context
): MainViewModelBase(navController, context) {
    internal var challengeListener: MatchRepository.ChallengeListener? = null

    internal val authentication = AuthenticationViewModel()

    internal val notificationPermission = WorditoryConfirmationDialogViewModel()

    internal val savedFriends = SavedFriendsViewModel()
    internal val sendFriendRequest = SendFriendRequestViewModel()
    internal val friendRequestSent = WorditoryInfoDialogViewModel()
    internal val friendAlreadyExists = WorditoryInfoDialogViewModel()
    internal val acceptFriendRequest = AcceptFriendRequestViewModel()
    internal val friendCard = FriendCardViewModel(navController)
    internal val challengeConfirmation = ChallengeConfirmationDialogViewModel()
    internal val inviteFriendConfirmation = WorditoryConfirmationDialogViewModel()
    internal val deleteFriendConfirmation = WorditoryConfirmationDialogViewModel()

    init {
        if (Firebase.auth.currentUser == null) {
            authentication.authenticate {
                authentication.dismiss()
                onAuthenticated(context)
                UserRepository.ifAvatarIsNotSet {
                    avatarChooser.enabled = true
                }
            }
        } else {
            onAuthenticated(context)
        }
    }

    override fun onPlayGameClicked() {
        if (challengeListener != null) {
            MatchRepository.removeListener(challengeListener!!)
        }

        super.onPlayGameClicked()
    }

    internal fun onPlayLiveGameClick() {
        authentication.authenticate {
            if (challengeListener != null) {
                MatchRepository.removeListener(challengeListener!!)
            }

            navController.navigate(LiveScreen.BoardSizeChooser.route)
        }
    }

    internal fun requestNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModelScope.launch {
                if (context.shouldAskForNotificationPermission().first()) {
                    notificationPermission.show(
                        onConfirmed = {
                            Notifications.requestPermission(context)
                        }
                    )
                    context.hasAskedForNotificationPermission()
                }
            }
        }
    }

    internal fun handleFriendRequestError(
        error: FriendRepository.OnFailure,
        context: Context
    ) {
        sendFriendRequest.enabled = false

        when (error.reason) {
            FriendRepository.OnFailure.Reason.EMAIL_NOT_REGISTERED -> {
                inviteFriendConfirmation.show(
                    onConfirmed = {
                        FriendRepository.inviteFriend(error.email!!, context)
                    },
                    onCancelled = { sendFriendRequest.enabled = true }
                )
            }
            FriendRepository.OnFailure.Reason.USER_IS_ALREADY_A_FRIEND -> {
                friendAlreadyExists.show(
                    onDismiss = { sendFriendRequest.enabled = true }
                )
            }
            else -> {}
        }
    }

    private fun onAuthenticated(context: Context) {
        SavedGamesRepository.syncLocalSavedGamesWithServer(viewModelScope, context)
        FriendRepository.syncLocalSavedFriendsWithServer(viewModelScope, context)
        context.startService(Intent(context, FriendService::class.java))

        challengeListener = MatchRepository.listenForChallenges { challenge ->
            challengeConfirmation.show(
                user = challenge.user,
                onConfirmed = {
                    MatchRepository.acceptChallenge(
                        gameId = challenge.gameId,
                        opponentUid = challenge.userId,
                        onSuccess = { match ->
                            viewModelScope.launch {
                                val liveGame = LiveGame.newLiveModel(match)
                                context.addSavedLiveGame(liveGame)
                                navController.navigate(LiveScreen.LiveGame.buildRoute(match.gameId))
                            }
                        },
                        onFailure = {}
                    )
                },
                onCancelled = {
                    MatchRepository.deleteChallenge(challenge.userId)
                    GameRepository.declineChallenge(challenge.gameId)
                }
            )
        }
    }
}
