package com.machineinsight_it.smartgarden.presentation.node.list

import android.view.View
import androidx.navigation.findNavController
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.ItemNodeBinding
import com.xwray.groupie.viewbinding.BindableItem

class NodeListItem(private val model: NodeViewModel) : BindableItem<ItemNodeBinding>() {
    override fun getLayout(): Int = R.layout.item_node

    override fun bind(viewBinding: ItemNodeBinding, position: Int) {
        viewBinding.model = model
        val chips = model.schedule.map {
            val layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(
                0, 0,
                viewBinding.root.resources.getDimension(R.dimen.padding_default).toInt(), 0
            )

            val chip = Chip(viewBinding.root.context)
            chip.layoutParams = layoutParams
            chip.text = it
            chip.isClickable = false

            chip
        }
        viewBinding.schedule.removeAllViews()
        chips.forEach { viewBinding.schedule.addView(it) }

        viewBinding.root.setOnClickListener {
            val navController = viewBinding.root.findNavController()
            navController.navigate(
                NodeListFragmentDirections.actionNodeListFragmentToNodeDetailsFragment(model.node)
            )
        }
    }

    override fun initializeViewBinding(view: View): ItemNodeBinding = ItemNodeBinding.bind(view)

}