package com.ultraplayer.app.data

import com.google.gson.annotations.SerializedName

/** Resposta do endpoint check_mac.php do painel Maximus. */
data class MacStatus(
    val success: Boolean = false,
    val registered: Boolean = false,
    val mac: String? = null,
    val status: String? = null,
    @SerializedName("expire_date") val expireDate: String? = null,
    val playlists: List<Playlist>? = null,
    @SerializedName("logo_url") val logoUrl: String? = null,
    @SerializedName("bg_url") val bgUrl: String? = null,
    @SerializedName("banner_url") val bannerUrl: String? = null,
    @SerializedName("app_name") val appName: String? = null,
    @SerializedName("whatsapp_url") val whatsappUrl: String? = null,
    @SerializedName("reseller_contact") val resellerContact: String? = null,
    @SerializedName("reseller_whatsapp") val resellerWhatsapp: String? = null,
    val version: String? = null,
    @SerializedName("apk_link") val apkLink: String? = null,
    val message: String? = null,
)

data class Playlist(
    val name: String? = null,
    val url: String? = null,
    val type: String? = null,
)

/** Credenciais Xtream Codes extraídas da URL de uma playlist (server/username/password). */
data class XtreamCreds(
    val server: String,
    val username: String,
    val password: String,
)

/** Um perfil local (igual "Perfil 1", "Perfil 2" do app anterior). */
data class Profile(
    val id: String,
    val name: String,
    val avatarEmoji: String = "🙂",
)
