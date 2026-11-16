package com.newsflash

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

// Tarea en segundo plano (WorkManager). Se ejecuta de forma automatica,
// incluso con la app cerrada, para revisar si hay una noticia nueva.
class NewsWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Consulta la API REST de noticias (servicio en la nube).
            val respuesta = NewsApi.crear().obtenerTitulares()
            val ultima = respuesta.articles.firstOrNull()
                ?: return Result.success()

            val titulo = ultima.title ?: "Noticia de ultima hora"
            val texto = ultima.description ?: "Toca para leer mas."

            // 2. Compara con la ultima noticia avisada para no repetir.
            val prefs = applicationContext
                .getSharedPreferences("newsflash", Context.MODE_PRIVATE)
            val anterior = prefs.getString("ultimo_titulo", "")

            // 3. Si la noticia es nueva, dispara la notificacion push.
            if (titulo != anterior) {
                NotificationHelper.mostrarNoticia(applicationContext, titulo, texto)
                prefs.edit().putString("ultimo_titulo", titulo).apply()
            }

            Result.success()
        } catch (e: Exception) {
            // Si falla la red, se reintenta mas tarde.
            Result.retry()
        }
    }
}
