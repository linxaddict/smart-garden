package com.machineinsight_it.smartgarden.data.model

import com.machineinsight_it.smartgarden.domain.Circuit

data class CircuitData(
    val id: Int,
    val name: String,
    val active: Boolean,
    val oneTimeActivation: OneTimeActivationData?,
    val schedule: List<ScheduleActivationData>
)

fun CircuitData.toCircuit() = Circuit(
    id = id,
    name = name,
    active = active,
    oneTimeActivation = oneTimeActivation?.toOneTimeActivation(),
    schedule = schedule.map { it.toScheduleActivation() }
)