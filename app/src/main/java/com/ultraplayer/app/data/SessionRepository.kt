package com.ultraplayer.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ultraplayer_session")

class SessionRepository(private val context: Context) {
    private val gson = Gson()

    private val macStatusKey = stringPreferencesKey("mac_status_json")
    private val activePlaylistIndexKey = intPreferencesKey("active_playlist_index")

    /** Salva o status retornado pelo painel depois de um login com sucesso. */
    suspend fun saveMacStatus(status: MacStatus) {
        context.dataStore.edit { prefs ->
            prefs[macStatusKey] = gson.toJson(status)
        }
    }

    /** Lê a sessão salva (ou null se nunca logou / limpou os dados). */
    val macStatusFlow: Flow<MacStatus?> = context.dataStore.data.map { prefs ->
        prefs[macStatusKey]?.let { json ->
            try {
                gson.fromJson(json, MacStatus::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getMacStatus(): MacStatus? = macStatusFlow.first()

    suspend fun setActivePlaylistIndex(index: Int) {
        context.dataStore.edit { prefs ->
            prefs[activePlaylistIndexKey] = index
        }
    }

    suspend fun getActivePlaylistIndex(): Int {
        return context.dataStore.data.map { it[activePlaylistIndexKey] ?: 0 }.first()
    }

    /** Retorna as credenciais Xtream prontas pra uso, da playlist ativa. */
    suspend fun getXtreamCreds(): XtreamCreds? {
        val status = getMacStatus() ?: return null
        val playlists = status.playlists ?: return null
        val index = getActivePlaylistIndex().coerceIn(0, (playlists.size - 1).coerceAtLeast(0))
        val playlist = playlists.getOrNull(index) ?: playlists.firstOrNull() ?: return null
        val url = playlist.url ?: return null
        return parsePlaylistUrl(url)
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
