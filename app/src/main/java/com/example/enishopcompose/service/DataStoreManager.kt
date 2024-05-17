package com.example.enishopcompose.service

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object DataStoreManager {

    val BACKGROUND_COLOR_KEY = intPreferencesKey("background_color")

    fun getBackgroundColor(context: Context): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[BACKGROUND_COLOR_KEY] ?: Color.White.toArgb()
        }
    }

    suspend fun setBackgroundColor(context: Context, color: Int) {
        context.dataStore.edit { preferences ->
            preferences[BACKGROUND_COLOR_KEY] = color
        }
    }
}