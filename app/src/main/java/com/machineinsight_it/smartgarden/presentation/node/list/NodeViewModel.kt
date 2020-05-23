package com.machineinsight_it.smartgarden.presentation.node.list

import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.healthy
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_FORMAT = "HH:mm dd-MM-yyyy"
private const val TIME_FORMAT = "HH:mm"

class NodeViewModel(val node: Node, resourceLocator: ResourceLocator) {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    val name: String = node.name.capitalize()
    val status: String
    val schedule = node.plan.filter { it.active }.sorted().map { timeFormatter.format(it.time) }
    val online = node.healthy()

    init {
        val connectionStatus = resourceLocator.label(
            if (node.active) R.string.online else R.string.offline
        )
        val lastActivationLabel = resourceLocator.label(R.string.last_activation_at)
        val lastActivateDate = dateFormatter.format(node.lastActivation.timeStamp)
        status = "$connectionStatus, $lastActivationLabel $lastActivateDate"
    }
}