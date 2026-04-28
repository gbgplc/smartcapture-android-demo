package com.gbg.smartcapture.bigmagic.viewmodel

import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.bigmagic.data.SettingsGroup
import com.gbg.smartcapture.bigmagic.data.SettingsManualCaptureToggleDelayType
import com.gbg.smartcapture.bigmagic.data.SettingsSwitch
import com.gbg.smartcapture.bigmagic.injections.Navigator
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyConfig
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError
import com.gbgroup.idscan.bento.enterprice.response.ResponseJourney
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import kotlinx.coroutines.flow.StateFlow

//todo we can redesign this viewmodel to be a bit easier to read and pass to places by breaking it
// down into sub-classes. Ie we still have one root view model, but within it be have 3-4 classes
// that store their own stateflow and have their own methods (credentials, settings, journeys, etc).
// We can then pass those subclasses to methods instead of passing a bunch of params. Low priority.

interface IRootViewModel {
    //todo we can eventually refactor this to be a credential set. Passing JSON versions of
    // credentials in the app and converting to and from JSON extra times makes the code less
    // readable and harder to refactor. Low priority.
    val credentialSetState: StateFlow<Set<String>>
    val tokenState: StateFlow<String>
    val generalBaseUrlState: StateFlow<String>
    val manualCaptureToggleDelayState: StateFlow<SettingsManualCaptureToggleDelayType>
    val faceCameraMovementStrictnessState: StateFlow<FaceCameraSettingsBuilder.MovementStrictness>
    val selectedCredential: StateFlow<SampleAppCredential>
    val journeyListLoading: StateFlow<Boolean>
    val journeyList: StateFlow<List<JourneyDefinition>?>
    val selectedJourney: StateFlow<JourneyDefinition?>
    val journeyCompleted: StateFlow<ResponseJourney?>
    val journeyError: StateFlow<CustomerJourneyError?>
    fun selectJourney(journeyDefinition: JourneyDefinition)
    fun setJourneyCompleted(responseJourney: ResponseJourney)
    fun setJourneyError(customerJourneyError: CustomerJourneyError)
    fun selectCredential(credential: SampleAppCredential)
    fun isValidUrl(url: String): Boolean
    fun isValidUsername(username: String): Boolean
    fun isValidPassword(password: String): Boolean
    fun credentialError(credential: SampleAppCredential): Int?
    fun credentialError(): Int?
    fun isValidCredential(): Boolean
    fun getJourneyInitError(): Int?
    fun isValidJourney(): Boolean
    fun saveCredential(credential: SampleAppCredential): Boolean
    fun removeCredential(credential: SampleAppCredential): Boolean
    fun getJourneys()
    fun getJourneyConfig(): CustomerJourneyConfig?
    fun getNavigator(): Navigator
    fun setManualCaptureToggleDelay(option: SettingsManualCaptureToggleDelayType)
    fun setMovementStrictness(option: FaceCameraSettingsBuilder.MovementStrictness)
    fun setToken(token: String)
    fun setGeneralBaseUrl(url: String)
    fun setSettingSwitch(switch: SettingsSwitch, value: Boolean)
    val settings: SettingsGroup
}