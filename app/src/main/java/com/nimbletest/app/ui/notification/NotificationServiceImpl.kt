package com.nimbletest.app.ui.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nimbletest.app.R
import com.nimbletest.app.ui.notification.NotificationKeys.NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface NotificationService {
    fun showBasicNotification()
}

@Singleton
class NotificationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationService {

    override fun showBasicNotification() = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val forgotNotification = createNotification {
            val title = getString(
                R.string.forgot_password_notification_title
            )
            val description = getString(
                R.string.forgot_password_notification_message
            )
            setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .build()
        }

        // Send the notification
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(System.currentTimeMillis().toInt(), forgotNotification)
    }
}

private fun Context.createNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ensureNotificationChannelExists()
    }
    return NotificationCompat.Builder(
        this,
        NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}

/**
 * Ensures that a notification channel is present if applicable
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun Context.ensureNotificationChannelExists() {

    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        getString(R.string.notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}