package com.machineinsight_it.smartgarden.presentation.node.list

import androidx.databinding.ObservableBoolean
import com.machineinsight_it.smartgarden.domain.interactor.FetchNodesInteractor
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator

class NodeListViewModel(
    private val fetchNodesInteractor: FetchNodesInteractor,
    private val resourceLocator: ResourceLocator
) : BaseViewModel() {
    val nodes = mutableListOf<NodeViewModel>()
    val dataSetChanged = SingleLiveEvent<Int>()
    val refreshing = ObservableBoolean()

    val fetchErrorEvent = SingleLiveEvent<Void>()

    fun fetchNodes() {
        fetchNodesInteractor
            .execute()
            .doOnSubscribe { refreshing.set(true) }
            .doFinally { refreshing.set(false) }
            .toFlowable()
            .flatMapIterable { it }
            .map { NodeViewModel(it, resourceLocator) }
            .toList()
            .subscribe(
                { handleNodesFetched(it) },
                { handleFetchError() }
            )
            .disposeOnClear()
    }

    private fun handleFetchError() {
        fetchErrorEvent.postValue(null)
    }

    private fun handleNodesFetched(nodes: MutableList<NodeViewModel>) {
        this.nodes.clear()
        this.nodes.addAll(nodes)
        dataSetChanged.postValue(this.nodes.size)
    }
}