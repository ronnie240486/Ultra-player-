package com.ultraplayer.app.data

import android.net.Uri

/**
 * A playlist do painel Maximus vem como uma URL tipo:
 * http://servidor.com:8080/get.php?username=USER&password=SENHA&type=m3u_plus&output=mpegts
 * A gente extrai server/username/password disso pra montar chamadas
 * diretas na API Xtream Codes (player_api.php).
 */
fun parsePlaylistUrl(url: String): XtreamCreds? {
    return try {
        val uri = Uri.parse(url)
        val username = uri.getQueryParameter("username") ?: return null
        val password = uri.getQueryParameter("password") ?: return null
        val scheme = uri.scheme ?: "http"
        val host = uri.host ?: return null
        val port = uri.port
        val server = if (port != -1) "$scheme://$host:$port" else "$scheme://$host"
        XtreamCreds(server = server, username = username, password = password)
    } catch (e: Exception) {
        null
    }
}
