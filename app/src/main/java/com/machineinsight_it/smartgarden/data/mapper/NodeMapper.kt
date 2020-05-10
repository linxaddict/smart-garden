package com.machineinsight_it.smartgarden.data.mapper

import com.machineinsight_it.smartgarden.data.model.PlanItemData
import com.machineinsight_it.smartgarden.data.model.NodeData
import com.machineinsight_it.smartgarden.domain.Node

fun NodeData.toNode() = Node(
    active = active,
    healthCheck = healthCheck,
    lastActivation = lastActivation.toActivation(),
    name = name,
    plan = plan.map(PlanItemData::toPlanItem)
)