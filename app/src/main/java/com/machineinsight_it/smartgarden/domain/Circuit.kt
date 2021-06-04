package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Circuit(
    val id: Int,
    val name: String,
    val active: Boolean,
    val oneTimeActivation: OneTimeActivation?,
    val schedule: List<ScheduleActivation>
) : Parcelable