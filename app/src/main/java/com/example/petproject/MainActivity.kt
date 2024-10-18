package com.example.petproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.petproject.presentation.main.MainScreenWrapper
import com.example.petproject.presentation.main.MainViewModel
import com.example.petproject.settings.SettingsStore
import com.example.petproject.settings.dataStore
import com.example.petproject.ui.theme.PetProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainViewModel by viewModels<MainViewModel>()

        enableEdgeToEdge()
        setContent {
            PetProjectTheme {
                MainScreenWrapper(viewModel = mainViewModel)
            }
        }

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


