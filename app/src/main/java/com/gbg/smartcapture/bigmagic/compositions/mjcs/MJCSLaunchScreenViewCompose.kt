package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.bigmagic.compositions.bits.SpacedListDividerView
import com.gbg.smartcapture.bigmagic.data.SampleAppCredential
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.commons.compositions.components.PrimaryButton
import com.gbg.smartcapture.commons.compositions.components.SecondaryButton
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MJCSLaunchScreenView(
    selectedCredential: State<SampleAppCredential>,
    selectedJourney: State<JourneyDefinition?>,
    onAddCredential: () -> Unit = {},
    onDeleteCredential: () -> Unit = {},
    onSelectCredential: () -> Unit = {},
    onSelectJourney: () -> Unit = {},
    onStartJourney: () -> Unit = {},
    onSettings: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        HeadingView(R.string.manage_credentials_title)

        PrimaryButton(
            text = stringResource(id = R.string.mjcs_add_credential_button),
            onSubmit = onAddCredential
        )
        PrimaryButton(
            text = stringResource(id = R.string.mjcs_remove_credential_button),
            onSubmit = onDeleteCredential
        )
        PrimaryButton(
            text = stringResource(id = R.string.mjcs_select_credential_button),
            onSubmit = onSelectCredential
        )

        HeadingView(R.string.selected_credential)

        if (selectedCredential.value.baseUrl.isBlank()) {
            Text(
                text = stringResource(R.string.no_credential_selected)
            )
        } else {
            CredentialRow(
                credentialJson = selectedCredential.value.toJson(),
                allowDelete = false,
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        PrimaryButton(
            text = stringResource(R.string.select_journey_button),
            onSubmit = onSelectJourney
        )
        HeadingView(R.string.selected_journey)
        selectedJourney.value?.let {
            Text(
                text = it.name
            )
        } ?: run {
            Text(
                text = stringResource(R.string.no_journey_selected)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        SecondaryButton(
            text = stringResource(id = R.string.settings_button),
            onSubmit = onSettings
        )
        if (selectedJourney.value != null) {
            SpacedListDividerView(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            PrimaryButton(
                text = stringResource(R.string.start_journey),
                onSubmit = onStartJourney
            )
        }
    }
}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val selectedCred: StateFlow<SampleAppCredential> = MutableStateFlow(SampleAppCredential())
    val selectedJourney: StateFlow<JourneyDefinition?> = MutableStateFlow(null)

    GBGPreviewView {
        MJCSLaunchScreenView(
            selectedCredential = selectedCred.collectAsStateWithLifecycle(),
            selectedJourney = selectedJourney.collectAsStateWithLifecycle(),
        )
    }
}
