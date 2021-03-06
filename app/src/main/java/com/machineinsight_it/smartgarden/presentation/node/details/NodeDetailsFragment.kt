package com.machineinsight_it.smartgarden.presentation.node.details

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.FragmentNodeDetailsBinding
import com.machineinsight_it.smartgarden.presentation.analytics.Analytics
import com.machineinsight_it.smartgarden.presentation.analytics.AnalyticsEvents
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


interface NodeDetailsController {
    fun executeOneTimeActivation(water: Long)
}

@AndroidEntryPoint
class NodeDetailsFragment : Fragment(), NodeDetailsController {
    lateinit var viewModel: NodeDetailsViewModel

    @Inject
    lateinit var analytics: Analytics

    lateinit var binding: FragmentNodeDetailsBinding

    private val args: NodeDetailsFragmentArgs by navArgs()

    private val removeListener = View.OnClickListener {
        if (it is ActivationView) {
            it.model?.let { model -> viewModel.removeActivation(model) }
        }
        binding.activations.removeView(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics.logEvent(AnalyticsEvents.EVENT_NODE_DETAILS_OPEN)
        viewModel = ViewModelProvider(this).get(NodeDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_node_details, container, false)
        viewModel.setNode(args.node)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.activations.forEach { addActivationView(it) }

        observeActivationAddedEvent()
        observeNavigateUpEvent()
        observeUpdateErrorEvent()

        binding.add.setOnClickListener {
            viewModel.addNewActivation()
        }
        binding.fab.setOnClickListener {
            val dialog = OneTimeActivationDialogFragment()
            dialog.show(childFragmentManager, null)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun observeUpdateErrorEvent() {
        viewModel.updateErrorEvent.observe(viewLifecycleOwner, Observer {
            Snackbar.make(
                binding.root, R.string.errorCannotUpdateSchedule,
                Snackbar.LENGTH_LONG
            ).show()
        })
    }

    private fun observeNavigateUpEvent() {
        viewModel.navigateUpEvent.observe(
            viewLifecycleOwner,
            Observer { findNavController().navigateUp() })
    }

    private fun observeActivationAddedEvent() {
        viewModel.activationAddedEvent.observe(
            viewLifecycleOwner,
            Observer { addActivationView(it, true) })
    }

    private fun addActivationView(model: ActivationViewModel, showKeyboard: Boolean = false) {
        val view = ActivationView(binding.root.context)
        view.model = model

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        view.setOnRemoveClickListener(removeListener)

        binding.activations.addView(view)

        if (showKeyboard) {
            val inputManager = activity
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            activity?.currentFocus?.let {
                inputManager.hideSoftInputFromWindow(
                    it.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }

            binding.root.doOnLayout { view.focusOnTime() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.node_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        hideKeyboard()

        return when (item.itemId) {
            R.id.action_save -> {
                if (viewModel.allActivationsValid()) {
                    viewModel.save()
                }
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideKeyboard() {
        val inputManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity?.currentFocus?.let {
            inputManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun executeOneTimeActivation(water: Long) {
        viewModel.executeOneTimeActivation(water)
    }
}