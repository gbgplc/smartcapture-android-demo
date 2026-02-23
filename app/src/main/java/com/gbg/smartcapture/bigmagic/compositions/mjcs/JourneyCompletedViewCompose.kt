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
import com.gbg.smartcapture.bigmagic.compositions.bits.ListDividerView
import com.gbgroup.idscan.bento.enterprice.response.ResponseJourney

@Composable
fun JourneyCompletedView(
    journeyCompleted: State<ResponseJourney?>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HeadingView(R.string.mjcs_journey_complete_heading)

        if (journeyCompleted.value == null) {
            Text(text = stringResource(R.string.no_journey_completed_information_to_show))
        } else {

            val response: ResponseJourney = journeyCompleted.value ?: return
            Text(text = response.journeyId ?: stringResource(R.string.no_journey_id))
            Text(text = response.highLevelResultEnum.name)

            ListDividerView()

            for (entry in response.additionalData ?: emptyList()) {
                if (entry.name != null) {
                    Text(text = entry.name + ": " + entry.value)
                }
            }

            ListDividerView()

            for (entry in response.resultDetailsMap) {
                Text(text = entry.key + ": " + entry.value)
            }
        }
    }
}
