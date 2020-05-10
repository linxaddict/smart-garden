package com.machineinsight_it.smartgarden.presentation.node.list

import com.machineinsight_it.smartgarden.domain.Node

class NodeViewModel(
    val name: String,
    val lastActivation: String,
    val healthy: Boolean,
    val status: String,
    val schedule: List<String>,
    val node: Node
)