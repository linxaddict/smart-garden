package com.machineinsight_it.smartgarden.presentation.resources

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceLocator @Inject constructor(private val context: Context) {
    fun label(@StringRes resId: Int) = context.resources.getString(resId)
}