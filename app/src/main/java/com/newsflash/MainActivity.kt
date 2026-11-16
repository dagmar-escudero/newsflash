package com.newsflash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

// Pantalla principal: activa la revision de noticias en segundo plano
// y permite lanzar una notificacion de prueba.
class MainActivity : AppCompatActivity() {

    private lateinit var estado: TextView

    // Pide permiso para mostrar notificaciones (obligatorio en Android 13+).
    private val permiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        estado.text = if (concedido) "Notificaciones activadas"
                      else "Permiso de notificaciones denegado"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        estado = findViewById(R.id.txtEstado)
        val btnActivar: Button = findViewById(R.id.btnActivar)
        val btnPrueba: Button = findViewById(R.id.btnPrueba)

        // Crea el canal de notificaciones al abrir la app.
        NotificationHelper.crearCanal(this)
        pedirPermisoNotificaciones()

        // Programa la tarea en segundo plano.
        btnActivar.setOnClickListener { programarRevision() }

        // Lanza una notificacion de ejemplo para la demostracion.
        btnPrueba.setOnClickListener {
            NotificationHelper.mostrarNoticia(
                this,
                "Ultima hora",
                "Nueva noticia disponible en NewsFlash."
            )
        }
    }

    // Configura WorkManager para revisar la API cada 15 minutos con internet.
    private fun programarRevision() {
        val restricciones = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // solo con internet
            .build()

        val tarea = PeriodicWorkRequestBuilder<NewsWorker>(15, TimeUnit.MINUTES)
            .setConstraints(restricciones)
            .build()

        // KEEP evita duplicar la tarea si ya estaba programada.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "revision_noticias",
            ExistingPeriodicWorkPolicy.KEEP,
            tarea
        )
        estado.text = "Revision de noticias activada (cada 15 min)"
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val concedido = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!concedido) permiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
