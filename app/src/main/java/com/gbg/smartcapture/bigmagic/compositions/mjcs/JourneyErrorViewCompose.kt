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
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyError

@Composable
fun JourneyErrorView(
    journeyError: State<CustomerJourneyError?>
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HeadingView(R.string.mjcs_journey_error_heading)

        val error = journeyError.value
        if (error != null) {
            Text(text = stringResource(R.string.journey_id, error.journeyId))
            Text(text = stringResource(R.string.journey_error_code, error.code))
            Text(text = stringResource(R.string.journey_error_message, error.message))
        }
    }
}

/*
@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val error = CustomerJourneyError(code = 123, message = "Test Error", journeyId = "Journey321")
    Log.e("asdf", error.message)
    print(error.message)
    val journeyError: MutableStateFlow<CustomerJourneyError?> = MutableStateFlow(error)

    PreviewView {
        JourneyErrorView(
            journeyError = journeyError.collectAsStateWithLifecycle(),
        )
    }
}
*/