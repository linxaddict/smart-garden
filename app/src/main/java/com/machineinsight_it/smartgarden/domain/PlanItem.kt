package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PlanItem(
    val time: Date,
    val water: Long,
    val active: Boolean
) : Comparable<PlanItem>, Parcelable {
    override fun compareTo(other: PlanItem): Int = time.compareTo(other.time)
}