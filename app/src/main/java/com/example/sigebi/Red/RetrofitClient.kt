package com.example.sigebi.Red

import com.example.sigebi.InterfazApi.ApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL =
        "https://bibliotek.grupoc.io/bibliotek_api/"

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val cookieJar = object : CookieJar {
        private val cookies = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookiesNuevas: List<Cookie>) {
            synchronized(this) {
                cookies.removeAll { antigua -> cookiesNuevas.any { nueva ->
                    antigua.name == nueva.name &&
                    antigua.domain == nueva.domain &&
                    antigua.path == nueva.path
                    }
                }

                cookies.addAll(cookiesNuevas)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return synchronized(this) {
                val ahora = System.currentTimeMillis()

                cookies.removeAll {it.expiresAt <= ahora}
                cookies.filter { it.matches(url) }
            }
        }

        fun limpiar(){
            synchronized(this) { cookies.clear() }
        }
    }

    private val clienteHttp = OkHttpClient.Builder().cookieJar(cookieJar).build()

    val apiService: ApiService by lazy {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }

    fun limpiarSesion() {
        cookieJar.limpiar()
    }

    fun mensajeDeError(respuesta: Response<*>): String? {
        return try {
            val texto = respuesta.errorBody()?.string() ?: return null
            jsonConfig.parseToJsonElement(texto).jsonObject["mensaje"]?.jsonPrimitive?.content
        } catch (e: Exception) {
            null
        }
    }
}