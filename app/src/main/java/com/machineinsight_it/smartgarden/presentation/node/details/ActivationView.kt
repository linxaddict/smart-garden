package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.ViewActivationBinding
import java.lang.NumberFormatException

class ActivationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var timeValue: String? = null
    private var waterValue: Int? = null
    private var binding: ViewActivationBinding

    var time: String?
        set(value) {
            timeValue = value
            binding.timeEdit.setText(value)
        }
        get() = timeValue

    var water: Int?
        set(value) {
            waterValue = value
            binding.waterEdit.setText(value.toString())
        }
        get() = waterValue

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
            text?.let { timeValue = it.toString() }
        }

        waterValue?.let { binding.waterEdit.setText(it.toString()) }
        binding.waterEdit.doOnTextChanged { text, _, _, _ ->
            text?.let {
                waterValue = try {
                    Integer.parseInt(it.toString())
                } catch (e: NumberFormatException) {
                    0
                }
            }
        }
    }
}