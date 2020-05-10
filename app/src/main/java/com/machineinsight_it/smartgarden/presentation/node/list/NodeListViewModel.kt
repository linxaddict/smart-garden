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
                {
                    nodes.clear()
                    nodes.addAll(it)
                    dataSetChanged.postValue(nodes.size)
                },
                {
                    println("error: $it")
                }
            )
            .disposeOnClear()
    }
}