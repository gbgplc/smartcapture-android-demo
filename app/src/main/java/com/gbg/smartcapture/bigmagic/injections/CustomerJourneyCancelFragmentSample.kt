package com.idscan.mjcs.sample.customerjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.ComposeView
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyInjectionView
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyCancelFragment
import com.gbg.smartcapture.mjcs.customerJourney.WindowUiConfig

/**
 * Optional fragment can be injected to show UI if user presses hardware back button
 * or 'X' before cancelling MJCS journey.
 *
 * See [CustomerJourneyCancelFragment] for more details.
 *
 * @since 12.3.0
 */
class CustomerJourneyCancelFragmentSample : CustomerJourneyCancelFragment() {

    override val windowUiConfig = WindowUiConfig(
        fitsSystemWindows = true,
        isSystemBarsVisible = true
    )

    override val TAG: String = CustomerJourneyCancelFragment::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SmartCaptureUiTheme {
                    BackHandler {
                        close()
                    }

                    JourneyInjectionView(
                        text = "BEFORE CANCEL",
                        subtitle = "Press back again to dismiss this screen",
                        buttonText = "Cancel Journey",
                        onClick = {
                            cancelJourney()
                        }
                    )
                }
            }
        }
    }
}