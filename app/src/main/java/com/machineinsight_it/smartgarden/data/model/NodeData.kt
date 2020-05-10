package com.machineinsight_it.smartgarden.data.model

import java.util.*

data class NodeData(
    val active: Boolean,
    val healthCheck: Date,
    val lastActivation: ActivationData,
    val name: String,
    val plan: List<PlanItemData>
)