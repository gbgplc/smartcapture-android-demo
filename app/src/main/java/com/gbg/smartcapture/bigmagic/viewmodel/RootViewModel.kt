package com.gbg.smartcapture.bigmagic.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.data.CredentialStore
import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.bigmagic.data.SettingsDataStore
import com.gbg.smartcapture.bigmagic.data.SettingsGroup
import com.gbg.smartcapture.bigmagic.data.SettingsManualCaptureToggleDelayType
import com.gbg.smartcapture.bigmagic.data.SettingsSwitch
import com.gbg.smartcapture.bigmagic.injections.Navigator
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyConfig
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError
import com.gbg.smartcapture.mjcs.scanner.DocumentScannerConfig
import com.gbgroup.idscan.bento.enterprice.Credentials
import com.gbgroup.idscan.bento.enterprice.EnterpriseService
import com.gbgroup.idscan.bento.enterprice.journey.Action
import com.gbgroup.idscan.bento.enterprice.listeners.OnJourneyDefinitionCallback
import com.gbgroup.idscan.bento.enterprice.response.ResponseJourney
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RootViewModel(application: Application): IRootViewModel, AndroidViewModel(application) {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 4
        private const val DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS = 5000L
    }

    private val settingStore: SettingsDataStore = SettingsDataStore(getApplication())

    private val credentialStore = CredentialStore(getApplication())

    override val credentialSetState: StateFlow<Set<String>> = credentialStore.getCredentials().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
        initialValue = emptySet()
    )

    override val manualCaptureToggleDelayState: StateFlow<SettingsManualCaptureToggleDelayType> = settingStore.getManualCaptureToggleDelay().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
        initialValue = SettingsManualCaptureToggleDelayType.DEFAULT_OPTION
    )

    override val faceCameraMovementStrictnessState: StateFlow<FaceCameraSettingsBuilder.MovementStrictness> = settingStore.getMovementStrictness().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
        initialValue = FaceCameraSettingsBuilder.MovementStrictness.DEFAULT_OPTION
    )

    override val tokenState: StateFlow<String> = settingStore.getToken().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
        initialValue = ""
    )

    override val generalBaseUrlState: StateFlow<String> = settingStore.getGeneralBaseUrl().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
        initialValue = ""
    )

    private val _selectedCredential: MutableStateFlow<SampleAppCredential> = MutableStateFlow(SampleAppCredential())
    override val selectedCredential: StateFlow<SampleAppCredential> = _selectedCredential

    private val _journeyListLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val journeyListLoading: StateFlow<Boolean> = _journeyListLoading

    private val _journeyList: MutableStateFlow<List<JourneyDefinition>?> = MutableStateFlow(null)
    override val journeyList: StateFlow<List<JourneyDefinition>?> = _journeyList

    private val _selectedJourney: MutableStateFlow<JourneyDefinition?> = MutableStateFlow(null)
    override val selectedJourney: StateFlow<JourneyDefinition?> = _selectedJourney

    private val _journeyCompleted: MutableStateFlow<ResponseJourney?> = MutableStateFlow(null)
    override val journeyCompleted: StateFlow<ResponseJourney?> = _journeyCompleted

    private val _journeyError: MutableStateFlow<CustomerJourneyError?> = MutableStateFlow(null)
    override val journeyError: StateFlow<CustomerJourneyError?> = _journeyError

    // Important init collecting so that when code checks if in Token mode the value is correct
    // before and without need the UI does a collect as state
    override fun initStorage() {
        viewModelScope.launch {
            settings.enableTokenAuth.collect {}
        }
        viewModelScope.launch {
            tokenState.collect {}
        }
        viewModelScope.launch {
            generalBaseUrlState.collect {}
        }
    }

    override fun selectJourney(journeyDefinition: JourneyDefinition) {
        _selectedJourney.value = journeyDefinition
    }

    override fun selectCredential(credential: SampleAppCredential) {
        _selectedCredential.value = credential
        // clear journey after changing the credential so user must load again
        _journeyList.value = null
        _selectedJourney.value = null
        //todo this would be a faster way of doing it, but might lead to some issues if the
        // journeys fail to load or alongside tokens. Needs a bit more work to refine.
//        if (isValidCredential()) {
//            getJourneys()
//        }
    }

    override fun isValidUrl(url: String): Boolean =
        android.util.Patterns.WEB_URL.matcher(url).matches()

    override fun isValidUsername(username: String): Boolean =
        username.isNotBlank()

    override fun isValidPassword(password: String): Boolean =
        password.isNotBlank() && password.length >= MIN_PASSWORD_LENGTH

    override fun credentialError(): Int? = credentialError(_selectedCredential.value)

    override fun credentialError(credential: SampleAppCredential): Int? =
        if (!isValidUrl(credential.baseUrl)) {
            R.string.credential_invalid_base_url
        } else if (!isValidUsername(credential.username)) {
            R.string.credential_invalid_username
        } else if (!isValidPassword(credential.password)) {
            R.string.credential_invalid_password
        } else {
            null
        }

    override fun isValidCredential(): Boolean =
        credentialError() == null

    fun tokenError(): Int? =
        getTokenError(
            tokenState.value,
            generalBaseUrlState.value
        )

    private fun getTokenError(
        token: String,
        baseUrl: String
    ): Int? =
        if (token.isBlank()) {
            R.string.settings_invalid_token
        } else if (!isValidUrl(baseUrl)) {
            R.string.settings_invalid_base_url
        } else {
            null
        }

    override fun getJourneyInitError(): Int? {
        val enableTokenAuth = settings.enableTokenAuth.value
        return if (enableTokenAuth) {
            tokenError()
        } else {
            credentialError()
        }
    }

    override fun isValidJourney(): Boolean =
        _selectedJourney.value != null

    override fun saveCredential(credential: SampleAppCredential): Boolean {
        val credentialJson = credential.toJson()
        if (credentialJson.isNotBlank()) {
            viewModelScope.launch {
                credentialStore.addCredential(
                    credentialJson
                )
            }
            return true
        }
        return false
    }

    override fun removeCredential(credential: SampleAppCredential): Boolean {
        val credentialJson = credential.toJson()
        if (credentialJson.isNotBlank()) {
            viewModelScope.launch {
                credentialStore.removeCredential(
                    getApplication(),
                    credentialJson
                )
            }

            if (credential == _selectedCredential.value) {
                selectCredential(SampleAppCredential())
            }
            return true
        }
        return false
    }

    override fun getJourneys() {
        _journeyListLoading.value = true
        val enterpriseService = EnterpriseService.Builder(getApplication()).apply {
            baseUrl(getBaseUrl())
            credentials(
                getServiceCredential()
            )
            loggingEnabled(true)
        }.build()
        enterpriseService.getPossibleJourneyDefinitions(definitionCallback)
    }

    // Get the selected credential base URL or the general base URL according to the Token switch
    private fun getBaseUrl(
        useToken: Boolean = settings.enableTokenAuth.value
    ): String =
        if (useToken) {
            generalBaseUrlState.value
        } else {
            _selectedCredential.value.baseUrl
        }

    private fun getServiceCredential(
        useToken: Boolean = settings.enableTokenAuth.value
    ): Credentials =
        if (useToken) {
            Credentials(
                token = tokenState.value
            )
        } else {
            Credentials(
                _selectedCredential.value.username,
                _selectedCredential.value.password
            )
        }

    private val definitionCallback = object : OnJourneyDefinitionCallback {
        override fun onError(code: Int, message: String) {
            _journeyListLoading.value = false
            _journeyList.value = null
            Toast.makeText(getApplication(), "Error: $code -> $message", Toast.LENGTH_LONG).show()
        }

        override fun onSuccess(list: List<JourneyDefinition>) {
            _journeyListLoading.value = false
            _journeyList.value = list
        }
    }

    /**
     * Commented out parts here are from the MJCS code that is yet to be implemented and plugged in.
     */
    override fun getJourneyConfig(): CustomerJourneyConfig? {
        // For fast demo purposes, you can populate base link and credentials from settings screen.
        val baseUrl = getBaseUrl()
        val username = _selectedCredential.value.username
        val password = _selectedCredential.value.password
        val token = tokenState.value
        val enableTokenAuth = settings.enableTokenAuth.value
        val journeyDefinitionGUID: String = _selectedJourney.value?.journeyDefinitionId
            ?: return null

        // From MJCS doc: Default value of 15 seconds (15000ms), trigger will appear after ~15 seconds.
        val captureButtonAppearTime: Long = manualCaptureToggleDelayState.value.valueMillis

        val docScannerConfig = DocumentScannerConfig.Builder()
            .setManualCaptureDisabled(!settings.manualCaptureToggle.value)
            .setManualCaptureAppearTime(captureButtonAppearTime)
            .setEnableScreenshots(settings.enableScreenshots.value)
            .build()

        if ((username.isEmpty() || password.isEmpty()) && !enableTokenAuth) {
            Toast.makeText(application, R.string.customer_journey_check_settings, Toast.LENGTH_LONG).show()
            return null
        }

        if (enableTokenAuth) {
            if (token.isEmpty()) {
                Toast.makeText(application, R.string.customer_journey_check_token_settings, Toast.LENGTH_LONG).show()
                return null
            }
            if (!isValidUrl(baseUrl)) {
                Toast.makeText(application, R.string.credential_invalid_base_url, Toast.LENGTH_LONG).show()
                return null
            }
        }

        val credentials =
            if (enableTokenAuth) {
                Credentials().apply { setToken(token) }
            } else {
                Credentials(username, password)
            }

        val config = CustomerJourneyConfig.Builder(
            baseUrl,
            credentials
        )
            .setJourneyDefinitionId(journeyDefinitionGUID)
            .setDocumentScannerConfig(docScannerConfig)
            .setIsTranslucentActivity(settings.enableTranslucentActivity.value)
            .build()

        return config
    }

    override fun getNavigator(): Navigator {
        // Call to open Customer Journey

        val frontside = settings.enablePreScreenInjectionFront.value
        val backside = settings.enablePreScreenInjectionBack.value
        val selfie = settings.enablePreScreenInjectionSelfie.value
        val liveness = settings.enablePreScreenInjectionPassiveLiveness.value
        val passiveLiveness = settings.enablePreScreenInjectionPassiveLiveness.value
        val poa = settings.enablePreScreenInjectionAddressDocument.value
        val nfcScan = settings.enablePreScreenInjectionNfc.value
        val loading = settings.enablePreScreenInjectionLoading.value
        val cancel = settings.enablePreScreenInjectionCancel.value

        val injection = object : NavigatorInjection {
            override fun shouldInject(action: Action): Boolean {
                Log.i("NavigatorInjection", "shouldInject: $action")
                return when (action) {
                    is Action.FrontSide -> frontside
                    is Action.BackSide -> backside
                    is Action.Selfie -> selfie
                    is Action.Liveness -> liveness
                    is Action.PassiveLiveness -> passiveLiveness
                    is Action.AddressDocument -> poa
                    is Action.NfcScan -> nfcScan
                    else -> false
                }
            }

            override fun shouldInjectLoading(): Boolean {
                return loading
            }

            override fun shouldInjectCancel(): Boolean {
                return cancel
            }
        }
        return Navigator(injection)
    }

    override fun setManualCaptureToggleDelay(option: SettingsManualCaptureToggleDelayType) {
        viewModelScope.launch {
            settingStore.setManualCaptureToggleDelay(option)
        }
    }

    override fun setMovementStrictness(option: FaceCameraSettingsBuilder.MovementStrictness) {
        viewModelScope.launch {
            settingStore.setMovementStrictness(option)
        }
    }

    private fun getSettingSwitch(switch: SettingsSwitch): StateFlow<Boolean> =
        settingStore.getSwitchFlow(switch).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(DATASTORE_SUBSCRIBE_TIMEOUT_MILLIS),
            initialValue = switch.defaultValue
        )

    override fun setSettingSwitch(switch: SettingsSwitch, value: Boolean) {
        viewModelScope.launch {
            settingStore.setSwitch(switch, value)
        }
    }

    override fun setToken(token: String) {
        viewModelScope.launch {
            settingStore.setToken(token)
        }
    }

    override fun setGeneralBaseUrl(url: String) {
        viewModelScope.launch {
            settingStore.setGeneralBaseUrl(url)
        }
    }

    override fun setJourneyCompleted(responseJourney: ResponseJourney) {
        _journeyCompleted.value = responseJourney
    }

    override fun setJourneyError(customerJourneyError: CustomerJourneyError) {
        _journeyError.value = customerJourneyError
    }

    override val settings: SettingsGroup = SettingsGroup(
        enableTokenAuth = getSettingSwitch(SettingsSwitch.ENABLE_TOKEN_AUTH),
        manualCaptureToggle = getSettingSwitch(SettingsSwitch.MANUAL_CAPTURE_TOGGLE),
        enableScreenshots = getSettingSwitch(SettingsSwitch.ENABLE_SCREENSHOTS),
        enableTranslucentActivity = getSettingSwitch(SettingsSwitch.ENABLE_TRANSLUCENT_ACTIVITY),

        enablePreScreenInjectionFront = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_FRONT),
        enablePreScreenInjectionBack = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_BACK),
        enablePreScreenInjectionPassiveLiveness = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_PASSIVE_LIVENESS),
        enablePreScreenInjectionAddressDocument = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_ADDRESS_DOCUMENT),

        enablePreScreenInjectionSelfie = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_SELFIE),
        enablePreScreenInjectionNfc = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_NFC),
        enablePreScreenInjectionLoading = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_LOADING),
        enablePreScreenInjectionCancel = getSettingSwitch(SettingsSwitch.ENABLE_PRESCREEN_INJECTION_CANCEL),
    )

}