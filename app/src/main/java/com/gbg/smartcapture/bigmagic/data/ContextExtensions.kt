package com.gbg.smartcapture.bigmagic.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val CREDENTIAL_DATASTORE_NAME = "smart_capture_datastore"
internal val Context.credentialDataStore by preferencesDataStore(CREDENTIAL_DATASTORE_NAME)

private const val SETTINGS_DATASTORE_NAME = "smart_capture_sample_app_settings"
internal val Context.settingsDataStore by preferencesDataStore(SETTINGS_DATASTORE_NAME)