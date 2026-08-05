package com.ultraplayer.app.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ultraplayer.app.ui.theme.AccentCyan

@Composable
fun HomeScreen() {
    // TODO: montar as fileiras de conteúdo (canais, filmes, séries) igual
    // a Home do app anterior — esse é só o ponto de partida pra confirmar
    // que o login -> navegação está funcionando de ponta a ponta.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Login funcionou! Próximo passo: montar a Home de verdade.", color = AccentCyan)
    }
}
