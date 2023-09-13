package com.korashiGroup.salamtakPatient.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.korashiGroup.salamtakPatient.MainActivity
import com.korashiGroup.salamtakPatient.R

class NotificationApp {

    private var context: Context

    private var Notificationmangercompat: NotificationManagerCompat
    private var notificationManager: NotificationManager

    companion object {
        var instance: NotificationApp? = null

    }

    constructor(context: Context) {
        this.context = context
        instance = this
        Notificationmangercompat = NotificationManagerCompat.from(context)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(
        notification_id: String,
        notificatio_name: String
    ) { //   create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                notification_id,
                notificatio_name, NotificationManager.IMPORTANCE_LOW
            )
            channel.setShowBadge(true)
            // channel.description = getString(R.string.cancel_description)
            notificationManager.createNotificationChannel(channel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun triggerNotification(channel_id: String, title: String, conenttext: String) {

        val intent = Intent(context, MainActivity::class.java)
        val openurl=Intent(Intent.ACTION_VIEW)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        //val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // خصائس النوتيفيكشن
        val notificationBuilder = NotificationCompat.Builder(context, channel_id)
            .setSmallIcon(R.drawable.ic_notifications)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_logo
                )
            )
            .setContentTitle(title)
            .setContentText(conenttext)
            .setStyle(NotificationCompat.BigTextStyle().bigText(conenttext))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            .setChannelId(channel_id)


        Notificationmangercompat.notify(
            context.resources.getInteger(R.integer.notificationid),
            notificationBuilder.build()
        )
    }

    fun cancelNotification(notification_id: Int) {
        notificationManager.cancel(notification_id)
    }
}


//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun triggerNotification() {
//
//        val notificationmanger = getSystemService(NotificationManager::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel(notificationmanger)
//        }
//        val intent = Intent(this, MainActivity::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        // خصائس النوتيفيكشن
//        val notificationBuilder =
//            NotificationCompat.Builder(this, getString(R.string.Notification_channel_Id))
//                .setSmallIcon(R.drawable.ic_notyfy)
//                .setLargeIcon(
//                    BitmapFactory.decodeResource(
//                        resources,
//                        R.drawable.ic_launcher_background
//                    )
//                )
//                .setContentTitle("Notification title")
//                .setContentText(" this my notification updated")
//                .setStyle(
//                    NotificationCompat.BigTextStyle().bigText(" this my notification updated")
//                )
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setChannelId(getString(R.string.Notification_channel_Id))
//                .setAutoCancel(true)
//        val notificationmangercompat = NotificationManagerCompat.from(this)
//        notificationmangercompat.notify(resources.getInteger(R.integer.notificationid), notificationBuilder.build())
//    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createChannel(notificationmanger: NotificationManager) { //   create notification channel
//        val channel = NotificationChannel(
//            getString(R.string.Notification_channel_Id),
//            getString(R.string.Notification_channel_Name), IMPORTANCE_LOW
//        )
//        channel.setShowBadge(true)
//        channel.description = getString(R.string.cancel_description)
//        notificationmanger.createNotificationChannel(channel)
//
//    }
