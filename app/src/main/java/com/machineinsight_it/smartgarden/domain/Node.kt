package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

private const val MAX_HEALTH_CHECK_INTERVAL_IN_SECONDS = 60

@Parcelize
data class Node(
    val active: Boolean,
    val healthCheck: Date,
    val lastActivation: Activation,
    val name: String,
    val plan: List<PlanItem>
) : Parcelable

fun Node.healthy(): Boolean {
    val now = Date()
    val diffInMilliseconds = now.time - healthCheck.time
    val diffInSeconds = diffInMilliseconds / 1000

    return diffInSeconds <= MAX_HEALTH_CHECK_INTERVAL_IN_SECONDS
}