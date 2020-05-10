package com.machineinsight_it.smartgarden.domain.repository

import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.PlanItem
import io.reactivex.Completable
import io.reactivex.Maybe

interface NodeRepository {
    fun fetchNodes(): Maybe<List<Node>>

    fun updateSchedule(node: String, items: List<PlanItem>): Completable

    fun executeOneTimeActivation(node: String, water: Long): Completable
}