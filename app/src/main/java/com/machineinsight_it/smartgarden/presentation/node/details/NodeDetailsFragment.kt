package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

    private val removeListener = View.OnClickListener {
        if (it is ActivationView) {
            it.model?.let { model -> viewModel.removeActivation(model) }
        }
        binding.activations.removeView(it)
    }

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
            addActivationView(it)
        }
        viewModel.activationAddedEvent.observe(this, Observer {
            addActivationView(it)
        })

        binding.add.setOnClickListener {
            viewModel.addNewActivation()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun addActivationView(model: ActivationViewModel) {
        val view = ActivationView(binding.root.context)
        view.model = model

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        view.setOnRemoveClickListener(removeListener)

        binding.activations.addView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.node_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                if (viewModel.allActivationsValid()) {
                    viewModel.save()
                    findNavController().navigateUp()
                } else {
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.activationAddedEvent.removeObservers(this)
    }
}