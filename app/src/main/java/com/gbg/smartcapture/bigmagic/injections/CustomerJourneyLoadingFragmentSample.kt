package com.gbg.smartcapture.bigmagic.injections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyInjectionView
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyLoadingFragment
import kotlinx.coroutines.flow.MutableStateFlow

class CustomerJourneyLoadingFragmentSample : CustomerJourneyLoadingFragment() {

    override var TAG = "CustomerJourneyLoadingFragmentSample"

    private val errorText: MutableStateFlow<String?> = MutableStateFlow(null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SmartCaptureUiTheme {
                    val error = errorText.collectAsStateWithLifecycle()

                    BackHandler(error.value != null) {
                        cancel()
                    }

                    if (error.value != null) {
                        JourneyInjectionView(
                            text = "LOADING ERROR: ${error.value}",
                            subtitle = "Press back to cancel the journey.",
                            buttonText = "Retry",
                            onClick = {
                                retry()
                            }
                        )
                    } else {
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LinearProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    override fun <E> onError(error: E) {
        errorText.value = error.toString()
    }

    override fun onUploadFinished(): Boolean {
        super.onUploadFinished()
        //... anything you want to do after the upload has finished
        finish() //call finish after
        return false // if you do not want the fragment to be removed immediately after this method then return true
    }
}