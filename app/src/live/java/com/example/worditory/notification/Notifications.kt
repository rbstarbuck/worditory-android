package com.example.worditory.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.DisplayMetrics
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.worditory.MainActivity
import com.example.worditory.R
import com.example.worditory.getActivity
import com.example.worditory.resourceid.getResourceId

private const val POST_NOTIFICATIONS_REQUEST_CODE = 0

private const val PLAYER_TURN_GROUP = "com.rbstarbuck.worditory.live.notification.PLAYER_TURN"
private const val FRIEND_REQUEST_GROUP = "com.rbstarbuck.worditory.live.notification.FRIEND_REQUEST"

private const val PLAYER_TURN_CHANNEL_ID = "playerTurn"
private const val TIMEOUT_CHANNEL_ID = "timeout"
private const val FRIEND_REQUEST_CHANNEL_ID = "friendRequest"

private const val PLAYER_TURN_NOTIFICATION_ID = 0
private const val FRIEND_REQUEST_NOTIFICATION_ID = 1

internal object Notifications {
    internal fun requestPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getActivity()?.requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_NOTIFICATIONS_REQUEST_CODE
            )
        }
    }

    internal fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager

            val playerTurnChannel = NotificationChannel(
                /* id = */ PLAYER_TURN_CHANNEL_ID,
                /* name = */ context.getString(R.string.player_turn_channel_name),
                /* importance = */ NotificationManager.IMPORTANCE_LOW
            )
            playerTurnChannel.description =
                context.getString(R.string.player_turn_channel_description)

            val timeoutChannel = NotificationChannel(
                /* id = */ TIMEOUT_CHANNEL_ID,
                /* name = */ context.getString(R.string.timeout_channel_name),
                /* importance = */ NotificationManager.IMPORTANCE_DEFAULT
            )
            timeoutChannel.description = context.getString(R.string.timeout_channel_description)

            val friendRequestChannel = NotificationChannel(
                /* id = */ FRIEND_REQUEST_CHANNEL_ID,
                /* name = */ context.getString(R.string.friend_request_channel_name),
                /* importance = */ NotificationManager.IMPORTANCE_LOW
            )
            timeoutChannel.description =
                context.getString(R.string.friend_request_channel_description)

            notificationManager.createNotificationChannel(playerTurnChannel)
            notificationManager.createNotificationChannel(timeoutChannel)
            notificationManager.createNotificationChannel(friendRequestChannel)
        }
    }

    internal fun isPlayerTurn(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        playedWord: String,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText = opponentName +
                " " + context.getString(R.string.player_turn_notification_text) +
                " " + playedWord

        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.player_turn_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun passedTurn(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText = opponentName +
                " " + context.getString(R.string.passed_turn_notification_text)

        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.player_turn_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun resignedGame(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText = opponentName +
                " " + context.getString(R.string.resigned_game_notification_text)

        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.game_over_win)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.resigned_game_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun claimedVictory(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText = opponentName +
                " " + context.getString(R.string.claimed_victory_notification_text)

        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.game_over_lose)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.claimed_victory_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun timeoutImminent(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText = opponentName +
                " " +
                context.getString(R.string.timeout_notification_text)

        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, TIMEOUT_CHANNEL_ID)
            .setSmallIcon(R.drawable.timeout_imminent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle(context.getString(R.string.timeout_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, TIMEOUT_CHANNEL_ID)
            .setSmallIcon(R.drawable.timeout_imminent)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun canClaimVictory(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText =
                context.getString(R.string.claim_victory_notification_text) + " " + opponentName


        val intent = Intent(
            /* action = */ Intent.ACTION_VIEW,
            /* uri = */ Uri.parse("https://rbstarbuck.com/livegame/$gameId"),
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.claim_victory)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.claim_victory_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(PLAYER_TURN_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setGroup(PLAYER_TURN_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(PLAYER_TURN_NOTIFICATION_ID, groupSummary)
        }
    }

    internal fun friendRequestReceived(
        uid: String,
        displayName: String,
        avatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(avatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText =
            displayName +
                    " " +
                    context.getString(R.string.friend_request_received_notification_text)

        val intent = Intent(
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, FRIEND_REQUEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.add_friend)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.friend_request_received_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(FRIEND_REQUEST_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, FRIEND_REQUEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.add_friend)
            .setGroup(FRIEND_REQUEST_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(uid.hashCode(), notification)
            notify(FRIEND_REQUEST_NOTIFICATION_ID, groupSummary)
        }
    }


    internal fun challengeReceived(
        gameId: String,
        displayName: String,
        avatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(avatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText =
            displayName +
                    " " +
                    context.getString(R.string.challenge_received_notification_text)

        val intent = Intent(
            /* packageContext = */ context,
            /* cls = */ MainActivity::class.java
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, FRIEND_REQUEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.challenge)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.challenge_received_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setGroup(FRIEND_REQUEST_GROUP)
            .build()

        val groupSummary = NotificationCompat.Builder(context, FRIEND_REQUEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.add_friend)
            .setGroup(FRIEND_REQUEST_GROUP)
            .setGroupSummary(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notify(gameId.hashCode(), notification)
            notify(FRIEND_REQUEST_NOTIFICATION_ID, groupSummary)
        }
    }



    internal fun cancel(id: String, context: Context) {
        val notificationManager = context.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.cancel(id.hashCode())
    }

    private fun largeIconSize(context: Context) = when(context.resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> 18
        DisplayMetrics.DENSITY_MEDIUM -> 24
        DisplayMetrics.DENSITY_HIGH -> 36
        DisplayMetrics.DENSITY_XHIGH -> 48
        DisplayMetrics.DENSITY_XXHIGH -> 72
        else -> 96
    }
}