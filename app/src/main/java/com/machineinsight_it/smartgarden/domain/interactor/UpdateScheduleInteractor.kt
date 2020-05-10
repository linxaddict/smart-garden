package com.machineinsight_it.smartgarden.domain.interactor

import com.machineinsight_it.smartgarden.domain.PlanItem
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateScheduleInteractor @Inject constructor(private val nodeRepository: NodeRepository) {
    fun execute(node: String, items: List<PlanItem>): Completable =
        nodeRepository.updateSchedule(node, items)
}