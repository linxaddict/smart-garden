package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Activation(
    val timeStamp: Date,
    val water: Long
) : Parcelable