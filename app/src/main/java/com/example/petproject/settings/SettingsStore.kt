package com.example.petproject.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ConcurrentHashMap

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingsStore")
val KEY_TEXT = stringPreferencesKey("key_text")

class SettingsStore(private val context: Context) {
    val text: Flow<String> = context.dataStore.data
        .map {
            it[KEY_TEXT] ?: ""
        }

    suspend fun saveText(text: String) {
        context.dataStore.edit {
            it[KEY_TEXT] = text
        }
    }

}