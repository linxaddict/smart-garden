package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.FragmentNodeDetailsBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NodeDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModel: NodeDetailsViewModel

    lateinit var binding: FragmentNodeDetailsBinding

    private val args: NodeDetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_node_details, container, false)
        viewModel.setNode(args.node)
        binding.model = viewModel

        viewModel.activations.forEachIndexed { index, it ->
            if (index != 0) {
                val spacer = Space(binding.root.context)
                val height = resources.getDimension(R.dimen.padding_default)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    height.toInt())
                spacer.layoutParams = layoutParams

                binding.activations.addView(spacer)
            }

            val view = ActivationView(binding.root.context)
            view.time = it.time
            view.water = it.water

            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            view.layoutParams = layoutParams

            binding.activations.addView(view)
        }

        return binding.root
    }


}