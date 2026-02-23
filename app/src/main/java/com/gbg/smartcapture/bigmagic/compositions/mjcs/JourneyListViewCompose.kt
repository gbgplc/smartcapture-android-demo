package com.gbg.smartcapture.bigmagic.compositions.mjcs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.R
import com.gbg.smartcapture.bigmagic.compositions.bits.HeadingView
import com.gbg.smartcapture.bigmagic.compositions.bits.ListDividerView
import com.gbg.smartcapture.commons.compositions.GBGPreviewView
import com.gbgroup.idscan.bento.enterprice.response.data.JourneyDefinition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun JourneyListView(
    journeyListLoading: State<Boolean>,
    journeyList: State<List<JourneyDefinition>?>,
    onSelectJourney: (journeyDefinition: JourneyDefinition) -> Unit = { journeyDefinition->},
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HeadingView(R.string.mjcs_journey_list_heading)

        // Loading state
        if (journeyListLoading.value) {
            LoadingView()
        }

        // Error or empty list
        journeyList.value?.let {
            if (it.isEmpty()) {
                ShowMessageView(stringResource(R.string.no_journey_data_found))
            } else {
                ListDividerView()

                // List of journeys
                for (journey in it) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable {
                                onSelectJourney(journey)
                            }
                    ) {
                        JourneyLabelView(journey)
                    }
                    ListDividerView()
                }
            }
        } ?: run {
            ShowMessageView(stringResource(R.string.journey_data_could_not_be_loaded_error))
        }
    }
}

@Composable
fun JourneyLabelView(
    journey: JourneyDefinition,
) {
    Column {
        Text(
            text = journey.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = journey.journeyDefinitionId,
            maxLines = 1,
            color = Color.Gray,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ShowMessageView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message
        )
    }
}

@Suppress("unused")
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val journeyListLoading: StateFlow<Boolean> = MutableStateFlow(false)
    val journeyList: StateFlow<List<JourneyDefinition>?> = MutableStateFlow(
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
            JourneyDefinition(
                journeyDefinitionId = "5678-5678",
                name = "Driving Licence Front and Back",
                _channelType = 1,
                _capturingMedia = 1,
                lastUpdatedDateTime = "",
                isActive = true,
                stepList = listOf()
            )
        )
    )

    GBGPreviewView {
        JourneyListView(
            journeyListLoading.collectAsStateWithLifecycle(),
            journeyList.collectAsStateWithLifecycle()
        )
    }
}
