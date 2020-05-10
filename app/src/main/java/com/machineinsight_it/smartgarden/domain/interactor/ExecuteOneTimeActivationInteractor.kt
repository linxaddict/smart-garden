package com.machineinsight_it.smartgarden.domain.interactor

import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import io.reactivex.Completable
import javax.inject.Inject

class ExecuteOneTimeActivationInteractor @Inject constructor(private val nodeRepository: NodeRepository) {
    fun execute(node: String, water: Long): Completable =
        nodeRepository.executeOneTimeActivation(node, water)
}