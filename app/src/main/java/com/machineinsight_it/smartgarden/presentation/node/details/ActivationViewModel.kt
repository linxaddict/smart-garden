package com.machineinsight_it.smartgarden.presentation.node.details

import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel

class ActivationViewModel(
    var time: String?,
    var water: Int?
) : BaseViewModel() {
    private fun timeIsValid(): Boolean {
        if (time == null) {
            return false
        }

        val timeValue = time ?: ""

        if (timeValue.length != 5 || !timeValue.contains(":")) {
            return false
        }

        val parts = timeValue.split(":")
        if (parts.size != 2) {
            return false
        }

        try {
            val hour = Integer.parseInt(parts[0])
            if (hour >= 24) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }

        try {
            val minute = Integer.parseInt(parts[1])
            if (minute >= 60) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }

        return true
    }

    private fun waterIsValid(): Boolean {
        return water != null && water ?: 0 > 0
    }

    fun isValid(): Boolean = timeIsValid() && waterIsValid()
}