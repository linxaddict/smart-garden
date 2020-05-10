package com.machineinsight_it.smartgarden.domain.interactor

import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.repository.NodeRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchNodesInteractor @Inject constructor(private val nodeRepository: NodeRepository) {
    fun execute(): Single<List<Node>> =
        nodeRepository.fetchNodes()
            .toSingle()
}