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

internal object Notifications {
    private const val POST_NOTIFICATIONS_REQUEST_CODE = 0
    private const val PLAYER_TURN_CHANNEL_ID = "playerTurn"
    private const val TIMEOUT_CHANNEL_ID = "timeout"
    private const val CLAIM_VICTORY_CHANNEl_ID = "claimVictory"
    private const val PLAYER_TURN_NOTIFICATION_ID = 0
    private const val TIMEOUT_NOTIFICATION_ID = 1
    private const val CLAIM_VICTORY_NOTIFICATION_ID = 2

    internal fun requestPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getActivity()?.requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_NOTIFICATIONS_REQUEST_CODE
            )
        }
    }

    internal fun createNotificationChannels(context: Context) {
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

            notificationManager.createNotificationChannel(playerTurnChannel)
            notificationManager.createNotificationChannel(timeoutChannel)
        }
    }

    internal fun notifyIsPlayerTurn(
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
                " " +
                context.getString(R.string.player_turn_notification_text) +
                " " +
                playedWord

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
            .setSmallIcon(R.drawable.your_turn)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.player_turn_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@with
            }

            notify(PLAYER_TURN_NOTIFICATION_ID, notification)
        }
    }

    internal fun notifyTimeoutImminent(
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
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@with
            }

            notify(TIMEOUT_NOTIFICATION_ID, notification)
        }
    }

    internal fun notifyCanClaimVictory(
        gameId: String,
        opponentName: String,
        opponentAvatarId: Int,
        context: Context
    ) {
        val largeIconSize = largeIconSize(context)
        val largeIcon = context.getDrawable(getResourceId(opponentAvatarId))
            ?.toBitmap(largeIconSize, largeIconSize)

        val contentText =
                context.getString(R.string.claim_victory_notification_text) +
                        " " +
                        opponentName


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

        var notification = NotificationCompat.Builder(context, CLAIM_VICTORY_CHANNEl_ID)
            .setSmallIcon(R.drawable.claim_victory)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.claim_victory_notification_title))
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@with
            }

            notify(CLAIM_VICTORY_NOTIFICATION_ID, notification)
        }
    }
}

private fun largeIconSize(context: Context) = when(context.resources.displayMetrics.densityDpi) {
    DisplayMetrics.DENSITY_LOW -> 18
    DisplayMetrics.DENSITY_MEDIUM -> 24
    DisplayMetrics.DENSITY_HIGH -> 36
    DisplayMetrics.DENSITY_XHIGH -> 48
    DisplayMetrics.DENSITY_XXHIGH -> 72
    else -> 96
}