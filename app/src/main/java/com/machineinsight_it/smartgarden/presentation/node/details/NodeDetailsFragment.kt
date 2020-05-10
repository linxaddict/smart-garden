package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }


}