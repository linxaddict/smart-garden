package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.ViewActivationBinding

class ActivationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var timeValue: String? = null
    private var waterValue: Int? = null
    private var binding: ViewActivationBinding
    private var modelValue: ActivationViewModel? = null

    var time: String?
        set(value) {
            timeValue = value
            modelValue?.time = value
            binding.timeEdit.setText(value)
        }
        get() = timeValue

    var water: Int?
        set(value) {
            waterValue = value
            modelValue?.water = value
            binding.waterEdit.setText(value.toString())
        }
        get() = waterValue

    var model: ActivationViewModel?
        set(value) {
            modelValue = value
            time = value?.time
            water = value?.water
        }
        get() = modelValue

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActivationView)
        timeValue = typedArray.getString(R.styleable.ActivationView_time)
        waterValue = typedArray.getInt(R.styleable.ActivationView_water, 0)
        if (waterValue == 0) {
            waterValue = null
        }
        typedArray.recycle()

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_activation,
            this,
            true
        )

        timeValue?.let { binding.timeEdit.setText(it) }
        binding.timeEdit.doOnTextChanged { text, _, _, _ ->
            text?.let {
                val value = it.toString()
                if (!timeIsValid(value)) {
                    binding.time.error = "HH:MM"
                } else {
                    binding.time.error = null
                }

                timeValue = value
                modelValue?.time = value
            }
        }

        waterValue?.let { binding.waterEdit.setText(it.toString()) }
        binding.waterEdit.doOnTextChanged { text, _, _, _ ->
            text?.let {
                waterValue = try {
                    binding.water.error = null
                    val value = Integer.parseInt(it.toString())
                    if (value == 0) {
                        binding.water.error = resources.getString(R.string.mustBeGreaterThan0)
                    }
                    value
                } catch (e: NumberFormatException) {
                    binding.water.error = resources.getString(R.string.mustBeGreaterThan0)
                    null
                }
                modelValue?.water = waterValue
            }
        }
    }

    fun setOnRemoveClickListener(listener: OnClickListener) {
        binding.remove.setOnClickListener {
            listener.onClick(this@ActivationView)
        }
    }

    private fun timeIsValid(time: String): Boolean {
        if (time.length != 5 || !time.contains(":")) {
            return false
        }

        val parts = time.split(":")
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
}