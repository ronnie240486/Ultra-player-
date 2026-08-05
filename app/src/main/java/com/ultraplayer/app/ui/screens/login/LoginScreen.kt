package com.ultraplayer.app.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ultraplayer.app.ui.theme.AccentCyan
import com.ultraplayer.app.ui.theme.TextSecondary
import com.ultraplayer.app.ui.theme.WarningOrange

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val deviceMac by viewModel.deviceMac.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoggedIn()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Ultra Player",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = AccentCyan,
        )

        Text(
            text = "ID do dispositivo",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 32.dp, bottom = 4.dp),
        )
        Text(
            text = deviceMac.ifBlank { "..." },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        when (val state = uiState) {
            is LoginUiState.Loading -> {
                CircularProgressIndicator(
                    color = AccentCyan,
                    modifier = Modifier.padding(top = 32.dp),
                )
            }
            is LoginUiState.NotRegistered -> {
                Text(
                    text = "Esse dispositivo ainda não está cadastrado. Envie o ID acima pro seu revendedor pra liberar o acesso.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
            is LoginUiState.Blocked -> {
                Text(
                    text = state.status.message ?: "Acesso bloqueado ou expirado.",
                    fontSize = 14.sp,
                    color = WarningOrange,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
            is LoginUiState.Error -> {
                Text(
                    text = state.message,
                    fontSize = 14.sp,
                    color = WarningOrange,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
            is LoginUiState.Success -> {
                // Navegação acontece via LaunchedEffect acima.
            }
        }

        Button(
            onClick = { viewModel.checkStatus() },
            modifier = Modifier.padding(top = 32.dp),
        ) {
            Text("Verificar novamente")
        }
    }
}
