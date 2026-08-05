package com.ultraplayer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ultraplayer.app.ui.navigation.UltraPlayerNavGraph
import com.ultraplayer.app.ui.theme.Black
import com.ultraplayer.app.ui.theme.UltraPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UltraPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Black,
                ) {
                    UltraPlayerNavGraph()
                }
            }
        }
    }
}
