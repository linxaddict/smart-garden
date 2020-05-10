package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PlanItem(
    val time: Date,
    val water: Long
) : Comparable<PlanItem>, Parcelable {
    override fun compareTo(other: PlanItem): Int = time.compareTo(other.time)
}