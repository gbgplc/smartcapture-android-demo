package com.gbg.smartcapture.bigmagic.compositions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.bigmagic.compositions.bits.ListDividerView
import com.gbg.smartcapture.bigmagic.compositions.bits.SettingSwitchRow
import com.gbg.smartcapture.bigmagic.compositions.bits.TitledDropdown
import com.gbg.smartcapture.bigmagic.data.SettingsManualCaptureToggleDelayType
import com.gbg.smartcapture.bigmagic.data.SettingsSwitch
import com.gbg.smartcapture.bigmagic.viewmodel.IRootViewModel
import com.gbg.smartcapture.bigmagic.viewmodel.MockedRootViewModel
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.facecamera.interfaces.FaceCameraSettingsBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    viewModel: IRootViewModel,
    tokenState: androidx.compose.runtime.State<String>,
    generalBaseUrlState: androidx.compose.runtime.State<String>,
    onCheckedChange: (settingsSwitch: SettingsSwitch, value: Boolean) -> Unit = { _, _ -> },
    onManualCaptureToggleDelayChange: (value: SettingsManualCaptureToggleDelayType) -> Unit = { _ -> },
    onMovementStrictnessChange: (value: FaceCameraSettingsBuilder.MovementStrictness) -> Unit = { _ -> },
    onTokenChange: (value: String) -> Unit = { _ -> },
    onBaseUrlChange: (value: String) -> Unit = { _ -> },
) {
    val enableTokenAuth = viewModel.settings.enableTokenAuth.collectAsStateWithLifecycle()
    val enableManualCaptureToggle = viewModel.settings.manualCaptureToggle.collectAsStateWithLifecycle()
    val enableScreenshots = viewModel.settings.enableScreenshots.collectAsStateWithLifecycle()
    val enableTranslucent = viewModel.settings.enableTranslucentActivity.collectAsStateWithLifecycle()
    val enablePreScreenInjectionFront = viewModel.settings.enablePreScreenInjectionFront.collectAsStateWithLifecycle()
    val enablePreScreenInjectionBack = viewModel.settings.enablePreScreenInjectionBack.collectAsStateWithLifecycle()
    val enablePreScreenInjectionPassiveLiveness = viewModel.settings.enablePreScreenInjectionPassiveLiveness.collectAsStateWithLifecycle()
    val enablePreScreenInjectionAddress = viewModel.settings.enablePreScreenInjectionAddressDocument.collectAsStateWithLifecycle()
    val enablePreScreenInjectionSelfie = viewModel.settings.enablePreScreenInjectionSelfie.collectAsStateWithLifecycle()
    val enablePreScreenInjectionNfc = viewModel.settings.enablePreScreenInjectionNfc.collectAsStateWithLifecycle()
    val enablePreScreenInjectionLoading = viewModel.settings.enablePreScreenInjectionLoading.collectAsStateWithLifecycle()
    val enablePreScreenInjectionCancel = viewModel.settings.enablePreScreenInjectionCancel.collectAsStateWithLifecycle()
    val manualCaptureToggleDelaySelected = viewModel.manualCaptureToggleDelayState.collectAsStateWithLifecycle()
    val movementStrictnessSelected = viewModel.faceCameraMovementStrictnessState.collectAsStateWithLifecycle()
    var token by remember { mutableStateOf(tokenState.value) }
    var baseUrl by remember { mutableStateOf(generalBaseUrlState.value) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HeadingView(stringResource = R.string.mjcs_settings_screen_title)

        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_TOKEN_AUTH,
            checked = enableTokenAuth.value,
            onCheckedChange = onCheckedChange
        )

        // When token auth switch is on - show the token input and the base url, else hide them
        if (enableTokenAuth.value) {

            TextField(
                value = token,
                onValueChange = { newValue ->
                    token = newValue
                    onTokenChange(newValue)
                },
                label = {
                    Text(stringResource(R.string.settings_token_auth))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            TextField(
                value = baseUrl,
                onValueChange = { newUrl: String ->
                    baseUrl = newUrl
                    onBaseUrlChange(newUrl)
                },
                label = {
                    // Warn if the base URL is an invalid URL
                    if (baseUrl.isEmpty() || viewModel.isValidUrl(baseUrl)) {
                        Text(text = stringResource(R.string.credentials_base_url))
                    } else {
                        Text(
                            text = stringResource(R.string.credential_invalid_base_url),
                            color = Color.Red
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }

        ListDividerView()

        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.MANUAL_CAPTURE_TOGGLE,
            checked = enableManualCaptureToggle.value,
            onCheckedChange = onCheckedChange
        )
        TitledDropdown(
            title = stringResource(R.string.settings_manual_capture_toggle_delay),
            items = SettingsManualCaptureToggleDelayType.ALL,
            selectedItem = manualCaptureToggleDelaySelected.value,
            onItemSelected = onManualCaptureToggleDelayChange,
            itemLabel = {
                stringResource(it.titleResource)
            },
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_SCREENSHOTS,
            checked = enableScreenshots.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_TRANSLUCENT_ACTIVITY,
            checked = enableTranslucent.value,
            onCheckedChange = onCheckedChange
        )

        ListDividerView()
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        HeadingView(stringResource = R.string.prescreen_injection_title)

        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_FRONT,
            checked = enablePreScreenInjectionFront.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_BACK,
            checked = enablePreScreenInjectionBack.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_PASSIVE_LIVENESS,
            checked = enablePreScreenInjectionPassiveLiveness.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_ADDRESS_DOCUMENT,
            checked = enablePreScreenInjectionAddress.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_SELFIE,
            checked = enablePreScreenInjectionSelfie.value,
            onCheckedChange = onCheckedChange
        )

        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_NFC,
            checked = enablePreScreenInjectionNfc.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_LOADING,
            checked = enablePreScreenInjectionLoading.value,
            onCheckedChange = onCheckedChange
        )
        SettingSwitchRow(
            settingsSwitch = SettingsSwitch.ENABLE_PRESCREEN_INJECTION_CANCEL,
            checked = enablePreScreenInjectionCancel.value,
            onCheckedChange = onCheckedChange
        )

        ListDividerView()
        HeadingView(stringResource = R.string.face_settings_screen_title)

        TitledDropdown(
            title = stringResource(R.string.settings_face_movement_strictness),
            items = FaceCameraSettingsBuilder.MovementStrictness.ALL,
            selectedItem = movementStrictnessSelected.value,
            onItemSelected = onMovementStrictnessChange,
            itemLabel = {
                it.name
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val token: StateFlow<String> = MutableStateFlow("1234")
    val baseUrl: StateFlow<String> = MutableStateFlow("https://")
    GBGPreviewView {
        SettingsView(
            MockedRootViewModel(),
            token.collectAsStateWithLifecycle(),
            baseUrl.collectAsStateWithLifecycle()
        )
    }
}