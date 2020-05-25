package com.machineinsight_it.smartgarden.presentation.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AnalyticsModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAnalytics(firebaseAnalytics: FirebaseAnalytics): Analytics {
        return FirebaseAppAnalytics(firebaseAnalytics)
    }
}