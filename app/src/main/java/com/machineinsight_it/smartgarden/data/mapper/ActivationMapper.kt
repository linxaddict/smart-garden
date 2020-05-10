package com.machineinsight_it.smartgarden.data.mapper

import com.machineinsight_it.smartgarden.data.model.ActivationData
import com.machineinsight_it.smartgarden.domain.Activation

fun ActivationData.toActivation() = Activation(
    timeStamp = timeStamp,
    water = water
)