package com.machineinsight_it.smartgarden.presentation.node.list

import androidx.databinding.ObservableBoolean
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.domain.interactor.FetchNodesInteractor
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_FORMAT = "HH:mm:ss yyyy-MM-dd"
private const val TIME_FORMAT = "HH:mm"

class NodeListViewModel(
    private val fetchNodesInteractor: FetchNodesInteractor,
    private val resourceLocator: ResourceLocator
) : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

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
            .map {
                NodeViewModel(
                    it.name.capitalize(),
                    dateFormatter.format(it.lastActivation.timeStamp),
                    it.active,
                    resourceLocator.label(if (it.active) R.string.online else R.string.offline),
                    it.plan.sorted().map { timeFormatter.format(it.time) },
                    it
                )
            }
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