package com.gbg.smartcapture.bigmagic.injections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import com.gbg.smartcapture.bigmagic.compositions.mjcs.JourneyInjectionView
import com.gbg.smartcapture.commons.theme.SmartCaptureUiTheme
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyFragment
import com.gbg.smartcapture.mjcs.customerJourney.WindowUiConfig

class CustomerJourneyFragmentSample : CustomerJourneyFragment() {

    override var TAG: String = CustomerJourneyFragmentSample::class.java.simpleName
        get() = "$field$text"

    private val text by lazy {
        requireArguments().getString(KEY_CUSTOMER_JOURNEY_SAMPLE_TEXT, "")
    }

    private val subtitle by lazy {
        requireArguments().getString(KEY_CUSTOMER_JOURNEY_SAMPLE_SUBTITLE, "")
    }

    override val windowUiConfig = WindowUiConfig(
        fitsSystemWindows = true,
        isSystemBarsVisible = true
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SmartCaptureUiTheme {
                    JourneyInjectionView(
                        text = text,
                        subtitle = subtitle,
                        buttonText = "Continue",
                        onClick = {
                            finish()
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val KEY_CUSTOMER_JOURNEY_SAMPLE_TEXT = "KEY_CUSTOMER_JOURNEY_SAMPLE_TEXT"
        private const val KEY_CUSTOMER_JOURNEY_SAMPLE_SUBTITLE = "KEY_CUSTOMER_JOURNEY_SAMPLE_SUBTITLE"

        fun newInstance(
            text: String,
            subtitle: String? = null,
        ): CustomerJourneyFragmentSample {
            return CustomerJourneyFragmentSample().apply {
                arguments = bundleOf(
                    KEY_CUSTOMER_JOURNEY_SAMPLE_TEXT to text,
                    KEY_CUSTOMER_JOURNEY_SAMPLE_SUBTITLE to subtitle
                )
            }
        }
    }
}