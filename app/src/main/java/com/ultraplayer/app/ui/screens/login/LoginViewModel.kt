package com.ultraplayer.app.ui.screens.login

import android.app.Application
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ultraplayer.app.data.MacStatus
import com.ultraplayer.app.data.SessionRepository
import com.ultraplayer.app.network.PanelClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

sealed class LoginUiState {
    data object Loading : LoginUiState()
    data class NotRegistered(val mac: String) : LoginUiState()
    data class Blocked(val status: MacStatus) : LoginUiState()
    data class Success(val status: MacStatus) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionRepository = SessionRepository(application)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _deviceMac = MutableStateFlow("")
    val deviceMac: StateFlow<String> = _deviceMac.asStateFlow()

    init {
        checkStatus()
    }

    /**
     * Dispositivos Android não têm mais acesso a um MAC de Wi-Fi de
     * verdade (bloqueado desde o Android 6 por privacidade), então usamos
     * o ANDROID_ID como identificador único e estável do aparelho,
     * formatado como um MAC (XX:XX:XX:XX:XX:XX) só pra ficar no formato
     * que o painel espera.
     */
    private fun resolveDeviceMac(): String {
        val androidId = try {
            Settings.Secure.getString(getApplication<Application>().contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            null
        } ?: UUID.randomUUID().toString().replace("-", "")

        val hex = androidId.filter { it.isLetterOrDigit() }.padEnd(12, '0').take(12).uppercase(Locale.US)
        return hex.chunked(2).joinToString(":")
    }

    fun checkStatus() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val mac = resolveDeviceMac()
            _deviceMac.value = mac
            try {
                val status = PanelClient.api.checkMac(mac)
                when {
                    !status.success || !status.registered -> _uiState.value = LoginUiState.NotRegistered(mac)
                    status.status?.lowercase(Locale.US) in listOf("bloqueado", "expirado", "blocked", "expired") ->
                        _uiState.value = LoginUiState.Blocked(status)
                    else -> {
                        sessionRepository.saveMacStatus(status)
                        _uiState.value = LoginUiState.Success(status)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Falha de conexão.")
            }
        }
    }
}
