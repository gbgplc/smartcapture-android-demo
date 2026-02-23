package com.gbg.smartcapture.bigmagic.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CredentialStore(
    private val context: Context
) {

    companion object {
        private val CREDENTIALS_KEY = stringSetPreferencesKey("smart_capture_credentials")
    }

    fun getCredentials(): Flow<Set<String>> =
        context.credentialDataStore.data.map { preferences ->
            preferences[CREDENTIALS_KEY] ?: emptySet()
        }

    suspend fun addCredential(
        json: String,
    ) {
        context.credentialDataStore.edit { prefs ->
            val currentSet = prefs[CREDENTIALS_KEY] ?: emptySet()
            val updatedSet = currentSet.toMutableSet().apply {
                add(json)
            }
            prefs[CREDENTIALS_KEY] = updatedSet
        }
    }

    suspend fun removeCredential(
        context: Context,
        credentialJson: String
    ) {
        context.credentialDataStore.edit { prefs ->
            val currentSet = prefs[CREDENTIALS_KEY] ?: emptySet()
            val updatedSet = currentSet.toMutableSet().apply {
                remove(credentialJson)
            }
            prefs[CREDENTIALS_KEY] = updatedSet
        }
    }

}