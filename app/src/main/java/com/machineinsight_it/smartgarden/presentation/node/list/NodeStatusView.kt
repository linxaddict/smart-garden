package com.machineinsight_it.smartgarden.presentation.node.list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.ViewNodeStatusBinding

class NodeStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var healthy = false
    private var binding: ViewNodeStatusBinding

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NodeStatusView)
        healthy = typedArray.getBoolean(R.styleable.NodeStatusView_online, false)
        typedArray.recycle()

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_node_status,
            this,
            true
        )

        setOnline(healthy)
    }

    fun setOnline(online: Boolean) {
        if (online) {
            binding.circleGreen.visibility = View.VISIBLE
            binding.circleRed.visibility = View.INVISIBLE
            binding.check.visibility = View.VISIBLE
            binding.alert.visibility = View.INVISIBLE
        } else {
            binding.circleGreen.visibility = View.INVISIBLE
            binding.circleRed.visibility = View.VISIBLE
            binding.check.visibility = View.INVISIBLE
            binding.alert.visibility = View.VISIBLE
        }
    }
}
