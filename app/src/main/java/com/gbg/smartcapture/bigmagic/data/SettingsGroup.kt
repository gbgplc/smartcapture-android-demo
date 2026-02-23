package com.gbg.smartcapture.bigmagic.data

import kotlinx.coroutines.flow.StateFlow

data class SettingsGroup(
    val enableTokenAuth: StateFlow<Boolean>,
    val manualCaptureToggle: StateFlow<Boolean>,
    val enableScreenshots: StateFlow<Boolean>,
    val enableTranslucentActivity: StateFlow<Boolean>,

    val enablePreScreenInjectionFront: StateFlow<Boolean>,
    val enablePreScreenInjectionBack: StateFlow<Boolean>,
    val enablePreScreenInjectionPassiveLiveness: StateFlow<Boolean>,
    val enablePreScreenInjectionAddressDocument: StateFlow<Boolean>,

    val enablePreScreenInjectionSelfie: StateFlow<Boolean>,
    val enablePreScreenInjectionNfc: StateFlow<Boolean>,
    val enablePreScreenInjectionLoading: StateFlow<Boolean>,
    val enablePreScreenInjectionCancel: StateFlow<Boolean>,
)