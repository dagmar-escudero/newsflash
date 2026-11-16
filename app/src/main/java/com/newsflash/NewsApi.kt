package com.newsflash

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Servicio en la nube: consulta una API REST publica de noticias.
// Se usa Retrofit para hacer la peticion HTTP y Gson para leer el JSON.

// Modelo de una noticia (solo los campos que necesitamos del JSON).
data class Noticia(
    val title: String?,
    val description: String?
)

// Respuesta de la API: trae una lista de noticias.
data class RespuestaNoticias(
    val articles: List<Noticia>
)

// Define el endpoint de la API REST.
interface NewsApi {
    @GET("top-headlines/category/general/us.json")
    suspend fun obtenerTitulares(): RespuestaNoticias

    companion object {
        // API publica y gratuita (no necesita llave) para la demostracion.
        private const val BASE_URL = "https://saurav.tech/NewsAPI/"

        // Crea el cliente de Retrofit ya configurado.
        fun crear(): NewsApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}
