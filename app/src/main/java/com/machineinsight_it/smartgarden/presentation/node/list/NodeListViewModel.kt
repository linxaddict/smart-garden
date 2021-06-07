package com.machineinsight_it.smartgarden.presentation.node.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.machineinsight_it.smartgarden.domain.interactor.FetchNodesInteractor
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NodeListViewModel @Inject constructor(
    private val fetchNodesInteractor: FetchNodesInteractor,
    private val resourceLocator: ResourceLocator,
) : BaseViewModel() {
    val nodes = mutableListOf<NodeViewModel>()
    val dataSetChanged = SingleLiveEvent<Int>()
    val fetchErrorEvent = SingleLiveEvent<Void?>()

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = _refreshing

    fun fetchNodes() {
        fetchNodesInteractor
            .execute()
            .doOnSubscribe {
                if (nodes.isEmpty()) {
                    _refreshing.postValue(true)
                }
            }
            .doFinally { _refreshing.postValue(false) }
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