package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.machineinsight_it.smartgarden.domain.interactor.ExecuteOneTimeActivationInteractor
import com.machineinsight_it.smartgarden.domain.interactor.UpdateScheduleInteractor
import com.machineinsight_it.smartgarden.presentation.di.ViewModelKey
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        NodeDetailsModule.Providers::class
    ]
)
abstract class NodeDetailsModule {
    @ContributesAndroidInjector(
        modules = [
            Injectors::class
        ]
    )
    abstract fun bind(): NodeDetailsFragment

    @Module
    class Providers {
        @Provides
        @IntoMap
        @ViewModelKey(NodeDetailsViewModel::class)
        fun provideNodeDetailsViewModel(
            resourceLocator: ResourceLocator,
            updateScheduleInteractor: UpdateScheduleInteractor,
            executeOneTimeActivationInteractor: ExecuteOneTimeActivationInteractor
        ): ViewModel =
            NodeDetailsViewModel(
                resourceLocator, updateScheduleInteractor,
                executeOneTimeActivationInteractor
            )
    }

    @Module
    class Injectors {
        @Provides
        fun provideNodeDetailsViewModel(
            factory: ViewModelProvider.Factory,
            target: NodeDetailsFragment
        ): NodeDetailsViewModel =
            ViewModelProviders.of(target, factory).get(NodeDetailsViewModel::class.java)
    }
}