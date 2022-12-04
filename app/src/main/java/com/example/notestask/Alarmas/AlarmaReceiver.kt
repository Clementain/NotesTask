package com.example.notestask.Alarmas


import android.R
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


const val notificationID = 1001
const val channelID = "channel1"
const val titleExtra = "TitleExtra"
const val messageExtra = "messageExtra"

class AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification =
            NotificationCompat.Builder(context, channelID).setSmallIcon(R.drawable.alert_dark_frame)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra)).build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}