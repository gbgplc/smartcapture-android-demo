package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun JourneyCanceledView(
    journeyError: State<CustomerJourneyError?>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HeadingView(R.string.mjcs_journey_cancelled_heading)

        journeyError.value?.let {
            Text(text = stringResource(R.string.journey_id, it.journeyId))
            Text(text = it.message)
        }
    }
}

@Composable
@Suppress("unused")
@Preview(showBackground = true)
private fun Preview() {
    val error = CustomerJourneyError(code = 123, message = "Test Error", journeyId = "Journey321")
    val journeyError: MutableStateFlow<CustomerJourneyError?> = MutableStateFlow(error)

    GBGPreviewView {
        JourneyCanceledView(
            journeyError = journeyError.collectAsStateWithLifecycle(),
        )
    }
}