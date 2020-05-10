package com.machineinsight_it.smartgarden.presentation.node.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.machineinsight_it.smartgarden.data.repository.NodeDataRepository
import com.machineinsight_it.smartgarden.domain.interactor.FetchNodesInteractor
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import com.machineinsight_it.smartgarden.presentation.di.ViewModelKey
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        NodeListModule.Providers::class,
        NodeListModule.Bindings::class
    ]
)
abstract class NodeListModule {
    @ContributesAndroidInjector(
        modules = [
            Injectors::class
        ]
    )
    abstract fun bind(): NodeListFragment

    @Module
    class Providers {
        @Provides
        @IntoMap
        @ViewModelKey(NodeListViewModel::class)
        fun provideNodeListViewModel(
            fetchNodesInteractor: FetchNodesInteractor,
            resourceLocator: ResourceLocator
        ): ViewModel =
            NodeListViewModel(fetchNodesInteractor, resourceLocator)
    }

    @Module
    abstract class Bindings {
        @Binds
        abstract fun bindNodeRepository(nodeDataRepository: NodeDataRepository): NodeRepository
    }

    @Module
    class Injectors {
        @Provides
        fun provideNodeListViewModel(
            factory: ViewModelProvider.Factory,
            target: NodeListFragment
        ): NodeListViewModel =
            ViewModelProviders.of(target, factory).get(NodeListViewModel::class.java)
    }
}