package com.machineinsight_it.smartgarden.di

import android.app.Application
import com.machineinsight_it.smartgarden.SmartGardenApplication
import com.machineinsight_it.smartgarden.data.di.DataModule
import com.machineinsight_it.smartgarden.presentation.analytics.AnalyticsModule
import com.machineinsight_it.smartgarden.presentation.di.PresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class, AnalyticsModule::class, AppModule::class,
        PresentationModule::class, DataModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: SmartGardenApplication)
}