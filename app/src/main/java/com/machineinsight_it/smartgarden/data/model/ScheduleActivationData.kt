package com.machineinsight_it.smartgarden.data.model

import com.machineinsight_it.smartgarden.domain.ScheduleActivation
import java.util.*

data class ScheduleActivationData(
    val timestamp: Date,
    val amount: Int,
    val active: Boolean
)

fun ScheduleActivationData.toScheduleActivation() = ScheduleActivation(
    timestamp = timestamp,
    amount = amount,
    active = active
)