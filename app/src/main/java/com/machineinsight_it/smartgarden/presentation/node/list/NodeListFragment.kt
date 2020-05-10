package com.machineinsight_it.smartgarden.presentation.node.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.FragmentNodeListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NodeListFragment : Fragment() {
    lateinit var binding: FragmentNodeListBinding

    @Inject
    lateinit var viewModel: NodeListViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_node_list, container, false)

        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.nodes.adapter = adapter

        viewModel.dataSetChanged.observe(this, Observer {
            adapter.clear()
            adapter.addAll(
                viewModel.nodes.map {
                    NodeListItem(
                        it
                    )
                }
            )
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchNodes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.dataSetChanged.removeObservers(this)
    }
}