package com.gbg.smartcapture.bigmagic.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Storage preferences to hold all the Sample App user settings
 */
class SettingsDataStore(private val context: Context) {

    companion object {
        private val KEY_SETTINGS_MANUAL_CAPTURE_TOGGLE_DELAY = stringPreferencesKey(
            SettingsManualCaptureToggleDelayType.DATASTORE_KEY
        )
        private val KEY_SETTINGS_MOVEMENT_STRICTNESS = stringPreferencesKey(
            FaceCameraSettingsBuilder.MovementStrictness.DATASTORE_KEY
        )
        private val KEY_SETTINGS_TOKEN = stringPreferencesKey("SETTINGS_TOKEN_AUTH")
        private val KEY_SETTINGS_GENERAL_BASE_URL = stringPreferencesKey("SETTINGS_GENERAL_BASE_URL")
    }

    fun getSwitchFlow(setting: SettingsSwitch): Flow<Boolean> =
        context.settingsDataStore.data.map { it[booleanPreferencesKey(setting.name)] ?: setting.defaultValue }

    suspend fun setSwitch(
        setting: SettingsSwitch,
        value: Boolean
    ) {
        context.settingsDataStore.edit { prefs ->
            prefs[booleanPreferencesKey(setting.name)] = value
        }
    }

    fun getManualCaptureToggleDelay(): Flow<SettingsManualCaptureToggleDelayType> =
        context.settingsDataStore.data.map { prefs ->
            SettingsManualCaptureToggleDelayType.getOption(
                prefs[KEY_SETTINGS_MANUAL_CAPTURE_TOGGLE_DELAY] ?: SettingsManualCaptureToggleDelayType.DEFAULT_OPTION.name
            )
        }

    suspend fun setManualCaptureToggleDelay(option: SettingsManualCaptureToggleDelayType) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_SETTINGS_MANUAL_CAPTURE_TOGGLE_DELAY] = option.name
        }
    }

    fun getMovementStrictness(): Flow<FaceCameraSettingsBuilder.MovementStrictness> =
        context.settingsDataStore.data.map { prefs ->
            FaceCameraSettingsBuilder.MovementStrictness.getOption(
                prefs[KEY_SETTINGS_MOVEMENT_STRICTNESS] ?: FaceCameraSettingsBuilder.MovementStrictness.DEFAULT_OPTION.name
            )
        }

    suspend fun setMovementStrictness(option: FaceCameraSettingsBuilder.MovementStrictness) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_SETTINGS_MOVEMENT_STRICTNESS] = option.name
        }
    }

    fun getToken(): Flow<String> =
        context.settingsDataStore.data.map { it[KEY_SETTINGS_TOKEN] ?: "" }

    suspend fun setToken(token: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_SETTINGS_TOKEN] = token
        }
    }

    fun getGeneralBaseUrl(): Flow<String> =
        context.settingsDataStore.data.map { it[KEY_SETTINGS_GENERAL_BASE_URL] ?: "" }

    suspend fun setGeneralBaseUrl(url: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_SETTINGS_GENERAL_BASE_URL] = url
        }
    }

}
