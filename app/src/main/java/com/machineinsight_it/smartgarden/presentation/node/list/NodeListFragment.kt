package com.machineinsight_it.smartgarden.presentation.node.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.FragmentNodeListBinding
import com.machineinsight_it.smartgarden.presentation.analytics.Analytics
import com.machineinsight_it.smartgarden.presentation.analytics.AnalyticsEvents
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NodeListFragment : Fragment() {
    lateinit var binding: FragmentNodeListBinding

    private val viewModel: NodeListViewModel by viewModels()

    @Inject
    lateinit var analytics: Analytics

    lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics.logEvent(AnalyticsEvents.EVENT_NODE_LIST_OPEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_node_list, container, false)
        binding.model = viewModel

        adapter = GroupAdapter<GroupieViewHolder>()
        binding.nodes.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner

        observeDataSetChangedEvent(adapter)
        observeFetchErrorEvent()

        binding.swipeRefresh.setOnRefreshListener { viewModel.fetchNodes() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.nodes.isNotEmpty()) {
            adapter.clear()
            adapter.addAll(
                viewModel.nodes.map {
                    NodeListItem(
                        it
                    )
                }
            )
        }
    }

    private fun observeFetchErrorEvent() {
        viewModel.fetchErrorEvent.observe(viewLifecycleOwner, Observer {
            Snackbar.make(
                binding.root, R.string.errorCannotFetchNodes,
                Snackbar.LENGTH_LONG
            ).show()
        })
    }

    private fun observeDataSetChangedEvent(adapter: GroupAdapter<GroupieViewHolder>) {
        viewModel.dataSetChanged.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.addAll(
                viewModel.nodes.map {
                    NodeListItem(
                        it
                    )
                }
            )
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchNodes()
    }
}