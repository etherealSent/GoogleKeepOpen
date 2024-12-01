package com.example.petproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.petproject.settings.SettingsStore
import com.example.petproject.ui.theme.PetProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetProjectTheme {
                PetNavGraph()
            }
        }
        windowManager
        val settingsStore: SettingsStore by lazy {
            SettingsStore(baseContext)
        }
    }
}

@Composable
fun SettingsScreen(text: String) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "ha")
            }
        }
    }
}


