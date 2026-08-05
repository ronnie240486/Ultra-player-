package com.ultraplayer.app.network

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class XtreamCategory(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
)

data class XtreamLiveStream(
    @SerializedName("stream_id") val streamId: Int,
    val name: String,
    @SerializedName("stream_icon") val streamIcon: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
)

data class XtreamMovie(
    @SerializedName("stream_id") val streamId: Int,
    val name: String,
    @SerializedName("stream_icon") val streamIcon: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("container_extension") val containerExtension: String? = null,
)

data class XtreamSeries(
    @SerializedName("series_id") val seriesId: Int,
    val name: String,
    val cover: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
)

interface XtreamApi {
    @GET("player_api.php")
    suspend fun getLiveCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_categories",
    ): List<XtreamCategory>

    @GET("player_api.php")
    suspend fun getLiveStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_streams",
    ): List<XtreamLiveStream>

    @GET("player_api.php")
    suspend fun getVodCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_categories",
    ): List<XtreamCategory>

    @GET("player_api.php")
    suspend fun getVodStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_streams",
    ): List<XtreamMovie>

    @GET("player_api.php")
    suspend fun getSeriesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series_categories",
    ): List<XtreamCategory>

    @GET("player_api.php")
    suspend fun getSeries(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series",
    ): List<XtreamSeries>
}

/** Um cliente Retrofit por servidor — cada linha Xtream pode ter um domínio diferente. */
object XtreamClientFactory {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val cache = mutableMapOf<String, XtreamApi>()

    fun forServer(server: String): XtreamApi {
        val baseUrl = if (server.endsWith("/")) server else "$server/"
        return cache.getOrPut(baseUrl) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(XtreamApi::class.java)
        }
    }

    /** Monta a URL de reprodução de um canal ao vivo. */
    fun liveStreamUrl(creds: com.ultraplayer.app.data.XtreamCreds, streamId: Int, ext: String = "m3u8"): String {
        return "${creds.server}/live/${creds.username}/${creds.password}/$streamId.$ext"
    }

    /** Monta a URL de reprodução de um filme. */
    fun movieStreamUrl(creds: com.ultraplayer.app.data.XtreamCreds, streamId: Int, ext: String = "mp4"): String {
        return "${creds.server}/movie/${creds.username}/${creds.password}/$streamId.$ext"
    }
}
