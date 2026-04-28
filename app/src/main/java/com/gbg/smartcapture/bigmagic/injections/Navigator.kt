package com.gbg.smartcapture.bigmagic.injections

import com.gbg.smartcapture.bigmagic.viewmodel.NavigatorInjection
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyCancelFragment
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyFragment
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyLoadingFragment
import com.gbg.smartcapture.mjcs.customerJourney.CustomerJourneyNavigator
import com.gbg.smartcapture.mjcs.customerJourney.LivenessLoadingFragment
import com.gbgroup.idscan.bento.enterprice.ResponseUpload
import com.gbgroup.idscan.bento.enterprice.journey.Action
import com.gbgroup.idscan.bento.enterprice.journey.RequiredActionAttempted

class Navigator(private val injection: NavigatorInjection) : CustomerJourneyNavigator {

    override fun actionBefore(action: Action, responseUpload: ResponseUpload?): CustomerJourneyFragment? {
        return when (action) {
            is Action.FrontSide -> getFragment("BEFORE FRONT SIDE", action)
            { super.actionBefore(action, responseUpload) }
            is Action.BackSide -> getFragment("BEFORE BACK SIDE", action)
            { super.actionBefore(action, responseUpload) }
            is Action.Selfie -> getFragment("BEFORE SELFIE", action)
            is Action.Liveness -> getFragment("BEFORE LIVENESS", action)
            is Action.PassiveLiveness -> getFragment("BEFORE PASSIVE LIVENESS", action)
            is Action.AddressDocument -> getFragment("BEFORE ADDRESS DOCUMENT", action)
            is Action.NfcScan -> getFragment("BEFORE NFC SCAN", action)
            else -> return null
        }
    }

    override fun onUploadStarted(action: Action): CustomerJourneyLoadingFragment? {
        return when {
            action == Action.Liveness || action == Action.PassiveLiveness || action == Action.Selfie -> LivenessLoadingFragment()
            injection.shouldInjectLoading() -> CustomerJourneyLoadingFragmentSample()
            else -> null
        }
    }

    override fun actionBeforeCancel(): CustomerJourneyCancelFragment? {
        return if (injection.shouldInjectCancel()) {
            CustomerJourneyCancelFragmentSample()
        } else {
            null
        }
    }

    private fun getFragment(fragmentText: String, action: Action, defaultImpl: () -> CustomerJourneyFragment? = { null }) = when {
        action.attempt != RequiredActionAttempted.REQUIRED_ACTION_FIRST_ATTEMPT -> defaultImpl.invoke()
        injection.shouldInject(action) -> CustomerJourneyFragmentSample.newInstance(
            text = fragmentText,
            subtitle = "Is retry: ${action.isRetry}"
        )
        else -> null
    }
}