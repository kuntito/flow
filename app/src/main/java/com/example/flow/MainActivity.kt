package com.example.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.example.flow.ui.theme.FlowTheme
import com.example.flow.ui.theme.colorKDB

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                colorKDB.toArgb()
            )
        )
        setContent {
            FlowTheme {
                Box(
                    modifier = Modifier
                        .background(color = colorKDB)
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {

                }
            }
        }
    }
}
