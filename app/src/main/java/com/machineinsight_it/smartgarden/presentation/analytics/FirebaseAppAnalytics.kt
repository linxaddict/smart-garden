package com.machineinsight_it.smartgarden.presentation.analytics

import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAppAnalytics(private val firebaseAnalytics: FirebaseAnalytics) : Analytics {
    override fun logEvent(event: String) {
        firebaseAnalytics.logEvent(event, null)
    }

}