package com.newsflash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

// Se encarga de crear el canal y mostrar la notificacion push en el dispositivo.
object NotificationHelper {

    private const val ID_CANAL = "canal_noticias"
    private const val NOMBRE_CANAL = "Alertas de noticias"

    // Crea el canal de notificaciones (obligatorio desde Android 8).
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL,
                NOMBRE_CANAL,
                NotificationManager.IMPORTANCE_HIGH // para que aparezca como aviso
            )
            canal.description = "Avisos de ultima hora de NewsFlash"
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    // Arma y muestra la notificacion con el titulo de la noticia.
    fun mostrarNoticia(context: Context, titulo: String, texto: String) {
        val notificacion = NotificationCompat.Builder(context, ID_CANAL)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // se cierra al tocarla
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(1, notificacion)
    }
}
