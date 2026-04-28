package com.gbg.smartcapture.bigmagic.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.AppSelectionView
import com.gbg.smartcapture.bigmagic.compositions.FaceResultView
import com.gbg.smartcapture.bigmagic.compositions.GenricFatalErrorView
import com.gbg.smartcapture.bigmagic.compositions.OzoneDataInput
import com.gbg.smartcapture.bigmagic.compositions.OzoneResultView
import com.gbg.smartcapture.bigmagic.compositions.SettingsView
import com.gbg.smartcapture.bigmagic.compositions.bits.VersionNumberView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.CredentialsListView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.CredentialsView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyCanceledView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyCompletedView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyErrorView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyListView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.MJCSLaunchScreenView
import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.bigmagic.data.SettingsManualCaptureToggleDelayType
import com.gbg.smartcapture.bigmagic.data.SettingsSwitch
import com.gbg.smartcapture.bigmagic.viewmodel.IRootViewModel
import com.gbg.smartcapture.bigmagic.viewmodel.RootViewModel
import com.gbg.smartcapture.commons.SmartCaptureException
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.commons.gbgGetInsetPadding
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.facecamera.FaceCameraActivity
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder.MovementStrictness
import com.gbg.smartcapture.facecamera.models.FaceCameraResult
import com.gbg.smartcapture.facecamera.models.FaceCameraSettings
import com.gbg.smartcapture.mjcs.CustomerJourneyEventListener
import com.gbg.smartcapture.mjcs.MJCS
import com.gbg.smartcapture.mjcs.MjcsEventService
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyActivity
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError
import com.gbg.smartcapture.ozone.model.OzoneInputData
import com.gbg.smartcapture.ozone.model.OzoneResultData
import com.gbg.smartcapture.ozoneui.OzoneUiActivity
import com.gbg.smartcapture.ozoneui.OzoneUiActivity.Companion.INPUT_DATA
import com.gbg.smartcapture.ozoneui.OzoneUiActivity.Companion.RESULT_FAIL_EXTRA_ERROR
import com.gbgroup.idscan.bento.enterprice.response.ResponseJourney
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import com.gbgroup.idscan.bento.enterprice.response.data.ResponseVersionInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class RootActivity : ComponentActivity() {
    private val viewModel: IRootViewModel by viewModels<RootViewModel>()
    private lateinit var navController: NavHostController
    private var destination = mutableStateOf ("")
    private var eventService: MjcsEventService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        MJCS.init(this)

        setContent {
            SmartCaptureUiTheme {
                val dest = remember { destination }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    RootScreen()
                }

                LaunchedEffect(dest.value) {
                    if (dest.value != "") {
                        navController.navigate(dest.value)
                        dest.value = ""
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerEventService(this)
    }

    @Composable
    private fun RootScreen() {
        navController = rememberNavController()

        val credentialSet = viewModel.credentialSetState.collectAsStateWithLifecycle()
        val selectedCredential = viewModel.selectedCredential.collectAsStateWithLifecycle()

        val journeyListLoading = viewModel.journeyListLoading.collectAsStateWithLifecycle()
        val journeyList = viewModel.journeyList.collectAsStateWithLifecycle()
        val selectedJourney = viewModel.selectedJourney.collectAsStateWithLifecycle()
        val journeyCompleted = viewModel.journeyCompleted.collectAsStateWithLifecycle()
        val journeyError = viewModel.journeyError.collectAsStateWithLifecycle()
        val token = viewModel.tokenState.collectAsStateWithLifecycle()
        val generalBaseUrl = viewModel.generalBaseUrlState.collectAsStateWithLifecycle()

        val insetPadding = LocalLayoutDirection.current.gbgGetInsetPadding()
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(insetPadding)
        ) {
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f, fill = true)
            ) {
                NavHost(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                    startDestination = NavigationItem.LandingScreen.route
                ) {
                    composable(NavigationItem.LandingScreen.route) {
                        AppSelectionView(
                            onFaceCamera = ::onFaceCamera,
                            onDocumentCamera = ::onDocumentCamera,
                            onOzoneMrz = ::onOzoneMrz,
                            onOzoneManual = ::onOzoneManual,
                            onMJCSJourney = ::onMJCSJourney,
                            onSettings = ::onSettings
                        )
                    }
                    composable(NavigationItem.SettingsScreen.route) {
                        SettingsView(
                            viewModel = viewModel,
                            tokenState = token,
                            generalBaseUrlState = generalBaseUrl,
                            onCheckedChange = ::settingsChecked,
                            onManualCaptureToggleDelayChange = ::onManualCaptureToggleDelayChange,
                            onMovementStrictnessChange = ::onMovementStrictnessChange,
                            onTokenChange = ::onTokenChange,
                            onBaseUrlChange = :: onGeneralBaseUrlChange
                        )
                    }
                    composable(NavigationItem.OzoneManualInputScreen.route) {
                        OzoneDataInput (
                            onSubmit = ::startOzone
                        )
                    }
                    composable(NavigationItem.OzoneResultScreen.route) {
                        ozoneResult?.let { result ->
                            OzoneResultView(result)
                        } ?: run {
                            Log.e("Sample App", "Result was null")
                            navController.popBackStack()
                        }
                    }
                    composable(NavigationItem.FaceResultScreen.route) {
                        faceResult?.let { faceResult ->
                            FaceResultView(faceResult)
                        } ?: run {
                            Log.e("Sample App", "Result was null")
                            navController.popBackStack()
                        }
                    }
                    composable(NavigationItem.FatalError.route) {
                        GenricFatalErrorView(errorText ?: "Unknown Fatal Error")
                    }
                    composable(NavigationItem.CredentialsOptionsScreen.route) {
                        MJCSLaunchScreenView(
                            selectedCredential,
                            selectedJourney = selectedJourney,
                            onAddCredential = ::onAddCredential,
                            onDeleteCredential = ::onDeleteCredential,
                            onSelectCredential = ::onSelectCredentialsOption,
                            onSelectJourney = ::onSelectJourneyOption,
                            onStartJourney = ::onStartJourney,
                            onSettings = ::onSettings,
                        )
                    }
                    composable(NavigationItem.CredentialsScreen.route) {
                        CredentialsView(
                            onSubmit = ::onSubmitCredential,
                            onCancel = ::onBack
                        )
                    }
                    composable(NavigationItem.CredentialsListScreen.route) {
                        CredentialsListView(
                            credentialSet = credentialSet,
                            allowDelete = false,
                            onSelectCredential = ::onSelectCredential,
                        )
                    }
                    composable(NavigationItem.CredentialsListDeleteScreen.route) {
                        CredentialsListView(
                            credentialSet = credentialSet,
                            allowDelete = true,
                            onDeleteCredential = ::onDeleteSelectedCredential,
                        )
                    }
                    composable(NavigationItem.JourneyListScreen.route) {
                        JourneyListView(
                            journeyListLoading = journeyListLoading,
                            journeyList = journeyList,
                            onSelectJourney = ::onSelectJourney,
                        )
                    }
                    composable(NavigationItem.JourneyCompletedScreen.route) {
                        JourneyCompletedView(
                            journeyCompleted = journeyCompleted,
                        )
                    }
                    composable(NavigationItem.JourneyFailedScreen.route) {
                        JourneyErrorView(
                            journeyError = journeyError,
                        )
                    }
                    composable(NavigationItem.JourneyCanceledScreen.route) {
                        JourneyCanceledView(
                            journeyError = journeyError,
                        )
                    }
                }
            }
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ){
                VersionNumberView()
            }
        }
    }

    private fun onManualCaptureToggleDelayChange(option: SettingsManualCaptureToggleDelayType) {
        viewModel.setManualCaptureToggleDelay(option)
    }

    private fun onMovementStrictnessChange(option: MovementStrictness) {
        viewModel.setMovementStrictness(option)
    }

    private fun onTokenChange(token: String) {
        viewModel.setToken(token)
    }

    private fun onGeneralBaseUrlChange(url: String) {
        viewModel.setGeneralBaseUrl(url)
    }

    private fun settingsChecked(
        settingsSwitch: SettingsSwitch,
        value: Boolean
    ) {
        viewModel.setSettingSwitch(settingsSwitch, value)
    }

    private fun onOzoneMrz() {
        Toast.makeText(this, "Ozone with MRZ reading is not yet added.", Toast.LENGTH_SHORT).show()
    }

    private fun onOzoneManual() {
        destination.value = NavigationItem.OzoneManualInputScreen.route
    }

    private fun startOzone(
        documentNumber: String,
        dob: String,
        doe: String
    ) {
        val dateRegex = "^[0-9]{6}\$".toRegex()
        if (!dateRegex.containsMatchIn(doe) || !dateRegex.containsMatchIn(dob)) {
            Toast.makeText(this, R.string.invalid_format, Toast.LENGTH_SHORT).show()
            return
        }
        if (documentNumber.isBlank()) {
            Toast.makeText(this, R.string.invalid_format, Toast.LENGTH_SHORT).show()
            return
        }

        navController.popBackStack()
        val intent = Intent(this, OzoneUiActivity::class.java)
        intent.putExtra(INPUT_DATA, OzoneInputData(
            documentNumber = documentNumber,
            dateOfBirth = dob,
            dateOfExpiration = doe
        ))
        ozoneLauncher.launch(intent)
    }

    private val ozoneLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                ozoneResult = OzoneUiActivity.getReadNfcData()
                destination.value = NavigationItem.OzoneResultScreen.route
            }
            RESULT_CANCELED -> {
                Toast.makeText(this, getString(R.string.user_cancelled_ozone), Toast.LENGTH_SHORT).show()
            }
            else -> {
                val error = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra(RESULT_FAIL_EXTRA_ERROR,
                        SmartCaptureException::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.data?.getSerializableExtra(RESULT_FAIL_EXTRA_ERROR) as SmartCaptureException?
                }

                errorText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    @Suppress("BlockingMethodInNonBlockingContext")
                    URLEncoder.encode("${error?.errorCode?.name}\n" +
                            "\n" +
                            "${error?.message}", StandardCharsets.UTF_8.name())
                } else {
                    @Suppress("DEPRECATION")
                    URLEncoder.encode("${error?.errorCode?.name}\n" +
                            "\n" +
                            "${error?.message}")
                }
                destination.value = NavigationItem.FatalError.route
            }
        }
    }

    private fun onDocumentCamera() {
        val intent = Intent(this, SampleAppDocumentCameraActivity::class.java)
        startActivity(intent)
    }

    private fun onSubmitCredential(
        baseUrl: String,
        username: String,
        password: String,
        save: Boolean
    ) {
        val credential = SampleAppCredential(
            baseUrl.trim(),
            username.trim(),
            password.trim()
        )
        val credentialErrorResource = viewModel.credentialError(credential)
        if (credentialErrorResource != null) {
            Toast.makeText(this, credentialErrorResource, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.selectCredential(credential)
            if (save) {
                viewModel.saveCredential(credential)
            }
            navController.popBackStack()
        }
    }

    private fun onBack() {
        navController.popBackStack()
    }

    private fun onAddCredential() {
        navController.navigate(NavigationItem.CredentialsScreen.route)
    }

    private fun onSelectCredential(credential: SampleAppCredential) {
        viewModel.selectCredential(credential)
        navController.popBackStack()
    }

    private fun onDeleteCredential() {
        navController.navigate(NavigationItem.CredentialsListDeleteScreen.route)
    }

    private fun onDeleteSelectedCredential(
        credential: SampleAppCredential
    ) {
        viewModel.removeCredential(credential)
    }

    private fun onSelectCredentialsOption() {
        navController.navigate(NavigationItem.CredentialsListScreen.route)
    }

    private fun onSelectJourney(
        journey: JourneyDefinition
    ) {
        viewModel.selectJourney(journey)
        navController.popBackStack()
    }

    private fun onSelectJourneyOption() {
        val error = viewModel.getJourneyInitError()
        if (error != null) {
            Toast.makeText(this@RootActivity, error, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.getJourneys()
            navController.navigate(NavigationItem.JourneyListScreen.route)
        }
    }

    override fun onDestroy() {
        eventService?.customerJourneyEventListener = null
        eventService?.unregisterService(this)
        eventService = null
        super.onDestroy()
    }

    private fun onStartJourney() {
        if (!viewModel.isValidCredential()) {
            Toast.makeText(this, R.string.credential_unselected_or_invalid, Toast.LENGTH_SHORT).show()
            return
        }
        if (!viewModel.isValidJourney()) {
            Toast.makeText(this, R.string.journey_unselected_or_invalid, Toast.LENGTH_SHORT).show()
            return
        }
        val config = viewModel.getJourneyConfig()
        if (config == null) {
            Toast.makeText(this, R.string.journey_config_fail, Toast.LENGTH_SHORT).show()
            return
        }
        val navigator = viewModel.getNavigator()
        CoroutineScope(Dispatchers.Main).launch {
            CustomerJourneyActivity.startActivity(this@RootActivity, config, navigator)
        }
    }

    private fun onJourneyCompletedScreen() {
        navController.navigate(NavigationItem.JourneyCompletedScreen.route)
    }

    private fun onJourneyFailedScreen() {
        navController.navigate(NavigationItem.JourneyFailedScreen.route)
    }

    private fun onJourneyCanceledScreen() {
        navController.navigate(NavigationItem.JourneyCanceledScreen.route)
    }

    private fun onMJCSJourney() {
        navController.navigate(NavigationItem.CredentialsOptionsScreen.route)
    }

    private fun onFaceCamera() {
        val faceMovementStrictnessSelected = viewModel.faceCameraMovementStrictnessState.value
        faceResult = null
        val intent = Intent(this, FaceCameraActivity::class.java)
        val settings = FaceCameraSettings.builder()
            .setMovementStrictness(faceMovementStrictnessSelected)
            .build()
        intent.putExtra(FaceCameraActivity.SETTINGS_EXTRA, settings)
        faceCameraLauncher.launch(intent)
    }

    private var faceCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val res = FaceCameraActivity.getResult()
                res?.let {
                    faceResult = it
                    destination.value = NavigationItem.FaceResultScreen.route
                }
            }
            RESULT_CANCELED -> {
                Toast.makeText(this,
                    getString(R.string.user_cancelled_face_capture), Toast.LENGTH_SHORT).show()
            }
            else -> {
                val error: SmartCaptureException =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getSerializableExtra(
                            FaceCameraActivity.ERROR_OBJECT,
                            SmartCaptureException::class.java
                        ) ?: SmartCaptureException(
                            SmartCaptureException.ErrorCode.Unknown,
                            "Unknown Error"
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getSerializableExtra(
                            FaceCameraActivity.ERROR_OBJECT
                        ) as SmartCaptureException? ?: SmartCaptureException(
                            SmartCaptureException.ErrorCode.Unknown,
                            "Unknown Error"
                        )
                    }
                error.printStackTrace()
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error_title))
                    .setMessage(error.message)
                    .setIcon(com.gbg.smartcapture.facecamera.R.drawable.triangle)
                    .show()
            }
        }
    }

    private fun onSettings() {
        navController.navigate(NavigationItem.SettingsScreen.route)
    }

    private fun registerEventService(activity: ComponentActivity) {
        if (eventService != null) {
            return
        }
        MjcsEventService.registerService(activity) { mjcsEventService: MjcsEventService? ->
            eventService = mjcsEventService
            eventService?.customerJourneyEventListener = customerJourneyEventListener
            Log.e("Root", "Event service registered")
        }
    }

    val customerJourneyEventListener = object : CustomerJourneyEventListener {
        /**
         * Callback if journey was successfully completed.
         */
        override fun onJourneyCompleted(responseJourney: ResponseJourney) {
            runOnUiThread {
                viewModel.setJourneyCompleted(responseJourney)
                onJourneyCompletedScreen()
            }
        }
        /**
         * Callback for failure from which we dit not succeed to restore our self.
         */
        override fun onJourneyFailed(customerJourneyError: CustomerJourneyError) {
            runOnUiThread {
                viewModel.setJourneyError(customerJourneyError)
                onJourneyFailedScreen()
            }
        }
        /**
         * Called back if customer clicked back or exits Customer Journey.
         */
        override fun onJourneyCanceled(customerJourneyError: CustomerJourneyError) {
            runOnUiThread {
                viewModel.setJourneyError(customerJourneyError)
                onJourneyCanceledScreen()
            }
        }
        /**
         * Provides version information
         */
        override fun onServerInfo(info: ResponseVersionInfo) {
            Log.w("CustomerJourneyEventListener", "onServerInfo: ")
            Log.w("CustomerJourneyEventListener", info.toString())
        }
    }


    private enum class Screen {
        LANDING,
        SETTINGS,
        CREDENTIALS_OPTIONS,
        CREDENTIALS_LIST,
        CREDENTIALS_LIST_DELETE,
        CREDENTIALS_DETAIL,
        JOURNEY_LIST,
        JOURNEY_COMPLETED,
        JOURNEY_FAILED,
        JOURNEY_CANCELED,
        FACE_RESULT,
        FATAL_ERROR,
        OZONE_RESULT,
        OZONE_MANUAL_INPUT
    }

    private sealed class NavigationItem(val route: String) {
        data object LandingScreen : NavigationItem(Screen.LANDING.name)
        data object SettingsScreen : NavigationItem(Screen.SETTINGS.name)
        data object FaceResultScreen : NavigationItem(Screen.FACE_RESULT.name)
        data object OzoneManualInputScreen : NavigationItem(Screen.OZONE_MANUAL_INPUT.name)
        data object OzoneResultScreen : NavigationItem(Screen.OZONE_RESULT.name)
        data object FatalError : NavigationItem(Screen.FATAL_ERROR.name)
        data object CredentialsOptionsScreen : NavigationItem(Screen.CREDENTIALS_OPTIONS.name)
        data object CredentialsScreen : NavigationItem(Screen.CREDENTIALS_DETAIL.name)
        data object CredentialsListScreen : NavigationItem(Screen.CREDENTIALS_LIST.name)
        data object CredentialsListDeleteScreen : NavigationItem(Screen.CREDENTIALS_LIST_DELETE.name)
        data object JourneyListScreen : NavigationItem(Screen.JOURNEY_LIST.name)
        data object JourneyCompletedScreen : NavigationItem(Screen.JOURNEY_COMPLETED.name)
        data object JourneyFailedScreen : NavigationItem(Screen.JOURNEY_FAILED.name)
        data object JourneyCanceledScreen : NavigationItem(Screen.JOURNEY_CANCELED.name)
    }

    companion object {
        private var faceResult: FaceCameraResult? = null
        private var ozoneResult: OzoneResultData? = null
        private var errorText: String? = null
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        GBGPreviewView {
            RootScreen()
        }
    }
}
