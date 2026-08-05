package com.ultraplayer.app.network

import com.ultraplayer.app.data.MacStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// TODO(ronnie): confirmar se essa é a URL base correta de onde o
// check_mac.php do painel Maximus fica hospedado. No app antigo (React
// Native) isso era configurado em outro lugar e não ficou claro no que foi
// migrado pra cá — ajusta esse valor se não bater.
private const val PANEL_BASE_URL = "https://renciaapp.manus.space/"

interface PanelApi {
    @GET("check_mac.php")
    suspend fun checkMac(@Query("mac") mac: String): MacStatus
}

object PanelClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: PanelApi by lazy {
        Retrofit.Builder()
            .baseUrl(PANEL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PanelApi::class.java)
    }
}
