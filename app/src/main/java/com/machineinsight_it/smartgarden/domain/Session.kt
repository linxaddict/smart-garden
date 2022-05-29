package com.machineinsight_it.smartgarden.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    val email: String,
    val token: String,
    val refreshToken: String
) : Parcelable