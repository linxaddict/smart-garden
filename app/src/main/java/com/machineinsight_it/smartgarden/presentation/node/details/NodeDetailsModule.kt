package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.machineinsight_it.smartgarden.presentation.di.ViewModelKey
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
        fun provideNodeDetailsViewModel(): ViewModel = NodeDetailsViewModel()
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