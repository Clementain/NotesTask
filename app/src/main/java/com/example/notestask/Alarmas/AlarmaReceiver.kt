package com.example.notestask.Alarmas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notestask.MainActivity
import com.example.notestask.R

class AlarmaReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

    }
    private fun mostrarNotificaciones(context: Context,intent: Intent){
        Log.d("DIF-ALARMA", "NOTIFICACION creando")
        val intenTap=Intent(context,MainActivity::class.java)
        intenTap.flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
        intenTap.putExtra("idT",1002)
        val pendingIntent=PendingIntent.getActivity(context,0,intenTap,PendingIntent.FLAG_UPDATE_CURRENT)
        val builder:NotificationCompat.Builder=NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Titulo Recordatorio")
            .setContentText("Te Recuerdo que Tienes Algo Pendiente")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(1001, builder.build())

    }
    companion object{
        val CHANNEL_ID = "RECORDATORIOS-TAREAS"
        fun createNotificationChannel(ctx: Context, intent: Intent?) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "Recordatorios"
                val description = "Recordatorios de Tareas"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = ctx.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
          }

    }
}