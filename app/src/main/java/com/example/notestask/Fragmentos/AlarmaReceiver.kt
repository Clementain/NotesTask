package com.example.notestask.Fragmentos


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

var notID = 1

class AlarmaReceiver : BroadcastReceiver() {

    var titulo = "Pendiente"
    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let { datos(it) }
        val notificationUtils = Notificaciones(context)
        val notification = notificationUtils.getNotificationBuilder(titulo).build()
        notificationUtils.getManager().notify(notID++, notification)
    }

    private fun datos(info: String) {

        this.titulo = info
    }
}