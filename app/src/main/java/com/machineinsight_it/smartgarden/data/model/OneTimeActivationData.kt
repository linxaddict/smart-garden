package com.machineinsight_it.smartgarden.data.model

import com.machineinsight_it.smartgarden.domain.OneTimeActivation
import java.util.*

data class OneTimeActivationData(
    val timestamp: Date,
    val amount: Int
)

fun OneTimeActivationData.toOneTimeActivation() = OneTimeActivation(
    timestamp = timestamp,
    amount = amount
)