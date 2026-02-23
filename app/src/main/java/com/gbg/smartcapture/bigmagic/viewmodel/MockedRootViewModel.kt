package com.gbg.smartcapture.bigmagic.viewmodel

import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.bigmagic.data.SettingsGroup
import com.gbg.smartcapture.bigmagic.data.SettingsManualCaptureToggleDelayType
import com.gbg.smartcapture.bigmagic.data.SettingsSwitch
import com.gbg.smartcapture.bigmagic.injections.Navigator
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyConfig
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError
import com.gbgroup.idscan.bento.enterprice.journey.Action
import com.gbgroup.idscan.bento.enterprice.response.ResponseJourney
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockedRootViewModel: IRootViewModel {
    override val credentialSetState: StateFlow<Set<String>>
        get() = MutableStateFlow(setOf<String>()).asStateFlow()
    override val manualCaptureToggleDelayState: StateFlow<SettingsManualCaptureToggleDelayType>
        get() = MutableStateFlow(SettingsManualCaptureToggleDelayType.OPTION_10_SECONDS).asStateFlow()
    override val faceCameraMovementStrictnessState: StateFlow<FaceCameraSettingsBuilder.MovementStrictness>
        get() = MutableStateFlow(FaceCameraSettingsBuilder.MovementStrictness.Strict).asStateFlow()
    override val selectedCredential: StateFlow<SampleAppCredential>
        get() = MutableStateFlow(SampleAppCredential("https://abc.123.com", "xyz", "098")).asStateFlow()

    override val tokenState: StateFlow<String> = MutableStateFlow("").asStateFlow()
    override val generalBaseUrlState: StateFlow<String> = MutableStateFlow("").asStateFlow()
    override val journeyCompleted: StateFlow<ResponseJourney?> = MutableStateFlow(null).asStateFlow()
    override val journeyError: StateFlow<CustomerJourneyError?> = MutableStateFlow(null).asStateFlow()

    private val dlFandB = JourneyDefinition(
        journeyDefinitionId = "5678-5678",
        name = "Driving Licence Front and Back",
        _channelType = 1,
        _capturingMedia = 1,
        lastUpdatedDateTime = "",
        isActive = true,
        stepList = listOf()
    )
    override val journeyListLoading: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
    override val journeyList: StateFlow<List<JourneyDefinition>?> = MutableStateFlow(
        listOf(
            JourneyDefinition(
                journeyDefinitionId = "1234-1234",
                name = "Journey 1",
                _channelType = 1,
                _capturingMedia = 1,
                lastUpdatedDateTime = "",
                isActive = true,
                stepList = listOf()
            ),
            dlFandB
        )
    ).asStateFlow()
    override val selectedJourney: StateFlow<JourneyDefinition?>
        get() = MutableStateFlow(dlFandB).asStateFlow()

    override fun selectJourney(journeyDefinition: JourneyDefinition) {
        
    }

    override fun selectCredential(credential: SampleAppCredential) {
        
    }

    override fun isValidUrl(url: String): Boolean {
        return true
    }

    override fun isValidUsername(username: String): Boolean {
        return true
    }

    override fun isValidPassword(password: String): Boolean {
        return true
    }

    override fun credentialError(credential: SampleAppCredential): Int? {
        return null
    }

    override fun credentialError(): Int? {
        return null
    }

    override fun isValidCredential(): Boolean {
        return true
    }
    override fun getJourneyInitError():Int? = null

    override fun isValidJourney(): Boolean {
        return true
    }

    override fun saveCredential(credential: SampleAppCredential): Boolean {
        return true
    }

    override fun removeCredential(credential: SampleAppCredential): Boolean {
        return true
    }

    override fun getJourneys() {
        
    }

    override fun getJourneyConfig(): CustomerJourneyConfig? {
        return null
    }

    override fun getNavigator(): Navigator {
        return Navigator(object : NavigatorInjection {
            override fun shouldInject(action: Action): Boolean {
                return false
            }

            override fun shouldInjectLoading(): Boolean {
                return false
            }

            override fun shouldInjectCancel(): Boolean {
                return false
            }
        })
    }

    override fun setManualCaptureToggleDelay(option: SettingsManualCaptureToggleDelayType) {
        
    }

    override fun setMovementStrictness(option: FaceCameraSettingsBuilder.MovementStrictness) {
        
    }

    override fun setSettingSwitch(
        switch: SettingsSwitch,
        value: Boolean
    ) {
        
    }

    override fun setJourneyCompleted(responseJourney: ResponseJourney) {
    }

    override fun setJourneyError(customerJourneyError: CustomerJourneyError) {
    }

    override fun setToken(token: String) {
    }

    override fun setGeneralBaseUrl(url: String) {
    }

    override fun initStorage() {
    }

    override val settings: SettingsGroup
        get() = SettingsGroup(
            enableTokenAuth = MutableStateFlow(false).asStateFlow(),
            manualCaptureToggle = MutableStateFlow(false).asStateFlow(),
            enableScreenshots = MutableStateFlow(true).asStateFlow(),
            enableTranslucentActivity = MutableStateFlow(false).asStateFlow(),
            enablePreScreenInjectionFront = MutableStateFlow(false).asStateFlow(),
            enablePreScreenInjectionBack = MutableStateFlow(true).asStateFlow(),
            enablePreScreenInjectionPassiveLiveness = MutableStateFlow(true).asStateFlow(),
            enablePreScreenInjectionAddressDocument = MutableStateFlow(false).asStateFlow(),
            enablePreScreenInjectionSelfie = MutableStateFlow(false).asStateFlow(),
            enablePreScreenInjectionNfc = MutableStateFlow(true).asStateFlow(),
            enablePreScreenInjectionLoading = MutableStateFlow(false).asStateFlow(),
            enablePreScreenInjectionCancel = MutableStateFlow(false).asStateFlow()
        )
}