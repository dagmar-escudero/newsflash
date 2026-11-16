# NewsFlash — Alerta de Noticias (AVS.03.05.01)

Aplicación Android (Kotlin) que avisa a los suscriptores cuando hay una noticia
de última hora, usando una **tarea en segundo plano** y **notificaciones push**,
incluso con la app cerrada.

## Cómo funciona
1. **Tarea en segundo plano (WorkManager):** `NewsWorker` se ejecuta de forma
   periódica (cada 15 min) aunque la app esté cerrada.
2. **Servicio en la nube (API REST):** consulta una API pública de noticias con
   **Retrofit** (`NewsApi`).
3. **Notificación push:** si detecta una noticia nueva, `NotificationHelper`
   muestra la notificación en el dispositivo.

## Clases principales
- `NewsApi.kt` — conexión con la API REST de noticias (Retrofit).
- `NewsWorker.kt` — tarea en segundo plano que revisa si hay noticias nuevas.
- `NotificationHelper.kt` — crea el canal y dispara la notificación.
- `MainActivity.kt` — activa el servicio y pide el permiso de notificaciones.

## Permisos
- `INTERNET` — para consultar la API.
- `POST_NOTIFICATIONS` — para mostrar notificaciones (Android 13+).

## Cómo probar
1. Abre el proyecto en Android Studio.
2. Ejecuta la app en un emulador o dispositivo.
3. Pulsa **"Activar revisión en segundo plano"** para programar la tarea.
4. Pulsa **"Enviar notificación de prueba"** para ver la notificación al instante.
