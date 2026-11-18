package com.example.sigebi.Modelo

import com.example.sigebi.InterfazApi.ApiService
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import kotlin.lazy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
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
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return synchronized(this) {
                val ahora = System.currentTimeMillis()

                cookies.removeAll {it.expiresAt <= ahora}
                cookies.filter { it.matches(url) }
            }
        }
    }

    private val clienteHttp = OkHttpClient.Builder().cookieJar(cookieJar).build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(
                jsonConfig.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(ApiService::class.java)
    }


}