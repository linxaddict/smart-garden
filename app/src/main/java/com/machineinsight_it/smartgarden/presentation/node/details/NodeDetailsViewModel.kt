package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.databinding.ObservableField
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TIME_FORMAT = "HH:mm dd-MM-yyyy"
private const val TIME_FORMAT = "HH:mm"

class NodeDetailsViewModel : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private var node: Node? = null

    val name = ObservableField<String>()
    val lastActivation = ObservableField<String>()

    fun setNode(node: Node) {
        this.node = node

        name.set(node.name.capitalize())
        lastActivation.set(dateFormatter.format(node.lastActivation.timeStamp))
    }
}