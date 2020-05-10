package com.machineinsight_it.smartgarden.domain.repository

import com.machineinsight_it.smartgarden.domain.Node
import io.reactivex.Maybe

interface NodeRepository {
    fun fetchNodes(): Maybe<List<Node>>
}