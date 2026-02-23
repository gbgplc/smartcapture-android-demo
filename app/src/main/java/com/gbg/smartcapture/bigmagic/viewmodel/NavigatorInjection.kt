package com.gbg.smartcapture.bigmagic.viewmodel

import com.gbgroup.idscan.bento.enterprice.journey.Action
import java.io.Serializable

interface NavigatorInjection : Serializable {
    fun shouldInject(action: Action): Boolean
    fun shouldInjectLoading(): Boolean
    fun shouldInjectCancel(): Boolean
}