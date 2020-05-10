package com.machineinsight_it.smartgarden.data.repository

import com.machineinsight_it.smartgarden.data.firebase.FirebaseDataStore
import com.machineinsight_it.smartgarden.data.mapper.toNode
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import io.reactivex.Maybe

class NodeDataRepository(private val schedulesDataStore: FirebaseDataStore) : NodeRepository {
    override fun fetchNodes(): Maybe<List<Node>> =
        schedulesDataStore
            .fetchSchedules()
            .toFlowable()
            .flatMapIterable { it }
            .map { it.toNode() }
            .toList()
            .toMaybe()
}