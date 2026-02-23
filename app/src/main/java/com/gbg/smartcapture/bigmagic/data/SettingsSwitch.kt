package com.gbg.smartcapture.bigmagic.data

import com.gbg.smartcapture.bigmagic.R

/**
 * Don't change the enums here as they are used as keys in the datastore
 * Adding or removing is ok.
 */
enum class SettingsSwitch(
    val title: Int,
    val defaultValue: Boolean = false,
) {
    ENABLE_TOKEN_AUTH(R.string.settings_enable_token_auth),

    MANUAL_CAPTURE_TOGGLE(R.string.settings_manual_capture_toggle, true),
    ENABLE_SCREENSHOTS(R.string.settings_enable_screenshots, true),

    ENABLE_PRESCREEN_INJECTION_FRONT(R.string.settings_enable_front_side),
    ENABLE_PRESCREEN_INJECTION_BACK(R.string.settings_enable_back_side),
    ENABLE_PRESCREEN_INJECTION_PASSIVE_LIVENESS(R.string.settings_enable_passive_liveness),
    ENABLE_PRESCREEN_INJECTION_ADDRESS_DOCUMENT(R.string.settings_enable_address_document),

    ENABLE_PRESCREEN_INJECTION_SELFIE(R.string.settings_enable_selfie),
    ENABLE_PRESCREEN_INJECTION_NFC(R.string.settings_enable_nfc),
    ENABLE_PRESCREEN_INJECTION_LOADING(R.string.settings_enable_loading),
    ENABLE_PRESCREEN_INJECTION_CANCEL(R.string.settings_enable_cancel),
    ENABLE_TRANSLUCENT_ACTIVITY(R.string.settings_enable_translucent_activity)
}