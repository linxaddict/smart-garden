package com.machineinsight_it.smartgarden.data.mapper

import com.machineinsight_it.smartgarden.data.model.PlanItemData
import com.machineinsight_it.smartgarden.domain.PlanItem

fun PlanItemData.toPlanItem() = PlanItem(
    time = time,
    water = water
)