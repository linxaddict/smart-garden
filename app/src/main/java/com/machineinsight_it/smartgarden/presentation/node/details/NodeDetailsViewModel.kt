package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.databinding.ObservableField
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.PlanItem
import com.machineinsight_it.smartgarden.domain.interactor.UpdateScheduleInteractor
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_FORMAT = "HH:mm dd-MM-yyyy"
private const val TIME_FORMAT = "HH:mm"

class NodeDetailsViewModel(
    private val resourceLocator: ResourceLocator,
    private val updateScheduleInteractor: UpdateScheduleInteractor
) : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private var node: Node? = null

    val name = ObservableField<String>()
    val status = ObservableField<String>()
    var activations = mutableListOf<ActivationViewModel>()

    val activationAddedEvent = SingleLiveEvent<ActivationViewModel>()

    fun setNode(node: Node) {
        this.node = node

        name.set(node.name.capitalize())

        val connectionStatus = resourceLocator.label(
            if (node.active) R.string.online else R.string.offline
        )
        val lastActivationLabel = resourceLocator.label(R.string.last_activation_at)
        val lastActivateDate = dateFormatter.format(node.lastActivation.timeStamp)

        status.set("$connectionStatus, $lastActivationLabel $lastActivateDate")
        activations.addAll(node.plan.map {
            ActivationViewModel(timeFormatter.format(it.time), it.water.toInt())
        })
    }

    fun addNewActivation() {
        val model = ActivationViewModel("12:00", 150)
        activations.add(model)
        activationAddedEvent.postValue(model)
    }

    fun removeActivation(model: ActivationViewModel) {
        activations.remove(model)
    }

    fun save() {
        val validModels = activations.filter { it.isValid() }
        val planItems = validModels.map {
            PlanItem(
                time = timeFormatter.parse(it.time),
                water = it.water?.toLong() ?: 0L
            )
        }

        node?.let {
            updateScheduleInteractor.execute(it.name, planItems)
                .subscribe(
                    {
                        println("schedule updated!")
                    },
                    {
                        println("update error: $it")
                    }
                )
        }
    }

    fun allActivationsValid(): Boolean {
        for (model in activations) {
            if (!model.isValid()) {
                return false
            }
        }

        return true
    }
}