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
    private const val PLAYER_TURN_NOTIFICATION_ID = 0

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
            val name = context.getString(R.string.player_turn_channel_name)
            val descriptionText = context.getString(R.string.player_turn_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PLAYER_TURN_CHANNEL_ID, name, importance)
            channel.description = descriptionText

            val notificationManager = context.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
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

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, PLAYER_TURN_CHANNEL_ID)
            .setSmallIcon(R.drawable.your_turn)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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
            // notificationId is a unique int for each notification that you must define.
            notify(PLAYER_TURN_NOTIFICATION_ID, notification)
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