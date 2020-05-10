package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.databinding.ObservableField
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_FORMAT = "HH:mm dd-MM-yyyy"
private const val TIME_FORMAT = "HH:mm"

class NodeDetailsViewModel(private val resourceLocator: ResourceLocator) : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private var node: Node? = null

    val name = ObservableField<String>()
    val status = ObservableField<String>()
    val lastActivation = ObservableField<String>()
    var activations = mutableListOf<ActivationViewModel>()

    fun setNode(node: Node) {
        this.node = node

        name.set(node.name.capitalize())

        val connectionStatus = resourceLocator.label(
            if (node.active) R.string.online else R.string.offline
        )
        val lastActivationLabel = resourceLocator.label(R.string.last_activation_at)
        val lastActivateDate = dateFormatter.format(node.lastActivation.timeStamp)
        status.set("$connectionStatus, $lastActivationLabel $lastActivateDate")

        lastActivation.set(dateFormatter.format(node.lastActivation.timeStamp))
        activations.addAll(node.plan.map {
            ActivationViewModel(timeFormatter.format(it.time), it.water.toInt())
        })
    }
}