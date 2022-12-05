package com.example.notestask.Fragmentos


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


var notificationID = 1
const val tituloExtra2 = "Tarea Pendiente"
const val mensajeExtra2 = "messageExtra"

class AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationUtils = Notificaciones(context)
        val notification = notificationUtils.getNotificationBuilder(tituloExtra2).build()
        notificationUtils.getManager().notify(notificationID++, notification)
    }
}