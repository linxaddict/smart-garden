package com.machineinsight_it.smartgarden.data.repository

import com.machineinsight_it.smartgarden.data.firebase.FirebaseDataStore
import com.machineinsight_it.smartgarden.data.mapper.toNode
import com.machineinsight_it.smartgarden.data.mapper.toPlanItemData
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.PlanItem
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

class NodeDataRepository @Inject constructor(private val schedulesDataStore: FirebaseDataStore) :
    NodeRepository {
    override fun fetchNodes(): Maybe<List<Node>> =
        schedulesDataStore
            .fetchSchedules()
            .toFlowable()
            .flatMapIterable { it }
            .map { it.toNode() }
            .toList()
            .toMaybe()

    override fun updateSchedule(node: String, items: List<PlanItem>): Completable =
        schedulesDataStore.updateSchedule(node, items.map { it.toPlanItemData() })

    override fun executeOneTimeActivation(node: String, water: Long): Completable =
        schedulesDataStore.executeOneTimeActivation(node, water)
}