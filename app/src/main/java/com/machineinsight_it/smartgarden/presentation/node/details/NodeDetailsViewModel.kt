package com.machineinsight_it.smartgarden.presentation.node.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.domain.Node
import com.machineinsight_it.smartgarden.domain.PlanItem
import com.machineinsight_it.smartgarden.domain.healthy
import com.machineinsight_it.smartgarden.domain.interactor.ExecuteOneTimeActivationInteractor
import com.machineinsight_it.smartgarden.domain.interactor.UpdateScheduleInteractor
import com.machineinsight_it.smartgarden.presentation.analytics.Analytics
import com.machineinsight_it.smartgarden.presentation.analytics.AnalyticsEvents
import com.machineinsight_it.smartgarden.presentation.base.BaseViewModel
import com.machineinsight_it.smartgarden.presentation.base.SingleLiveEvent
import com.machineinsight_it.smartgarden.presentation.resources.ResourceLocator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val DATE_TIME_FORMAT = "HH:mm dd-MM-yyyy"
private const val TIME_FORMAT = "HH:mm"

@HiltViewModel
class NodeDetailsViewModel @Inject constructor(
    private val resourceLocator: ResourceLocator,
    private val updateScheduleInteractor: UpdateScheduleInteractor,
    private val executeOneTimeActivationInteractor: ExecuteOneTimeActivationInteractor,
    private val analytics: Analytics
) : BaseViewModel() {
    private val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private var node: Node? = null

    private val _name = MutableLiveData<String>()
    private val _status = MutableLiveData<String>()
    private val _online = MutableLiveData<Boolean>()

    val name: LiveData<String> = _name
    val status: LiveData<String> = _status
    val online: LiveData<Boolean> = _online

    var activations = mutableListOf<ActivationViewModel>()

    val activationAddedEvent = SingleLiveEvent<ActivationViewModel>()
    val navigateUpEvent = SingleLiveEvent<Void>()
    val updateErrorEvent = SingleLiveEvent<Void>()

    fun setNode(node: Node) {
        this.node = node

        _name.postValue(node.name.capitalize())

        val connectionStatus = resourceLocator.label(
            if (node.active) R.string.online else R.string.offline
        )
        val lastActivationLabel = resourceLocator.label(R.string.last_activation_at)
        val lastActivateDate = dateFormatter.format(node.lastActivation.timeStamp)

        _status.postValue("$connectionStatus, $lastActivationLabel $lastActivateDate")
        _online.postValue(node.healthy())

        activations.clear()
        activations.addAll(node.plan.sortedBy { it.time }.map {
            ActivationViewModel(timeFormatter.format(it.time), it.water.toInt(), it.active)
        })
    }

    fun addNewActivation() {
        val model = ActivationViewModel(null, null, false)
        activations.add(model)
        activationAddedEvent.postValue(model)
        analytics.logEvent(AnalyticsEvents.EVENT_NODE_DETAILS_ADD_SCHEDULE_ITEM)
    }

    fun removeActivation(model: ActivationViewModel) {
        activations.remove(model)
        analytics.logEvent(AnalyticsEvents.EVENT_NODE_DETAILS_DELETE_SCHEDULE_ITEM)
    }

    fun save() {
        val validModels = activations.filter { it.isValid() }
        val planItems = validModels.map {
            PlanItem(
                time = timeFormatter.parse(it.time),
                water = it.water?.toLong() ?: 0L,
                active = it.active
            )
        }

        node?.let {
            analytics.logEvent(AnalyticsEvents.EVENT_NODE_DETAILS_UPDATE)

            updateScheduleInteractor.execute(it.name, planItems)
                .subscribe(
                    { navigateUpEvent.postValue(null) },
                    { updateErrorEvent.postValue(null) }
                )
                .disposeOnClear()
        }
    }

    fun allActivationsValid(): Boolean {
        for (model in activations) {
            if (!model.isValid()) {
                return false
            }
        }

        return true
    }

    fun executeOneTimeActivation(water: Long) {
        analytics.logEvent(AnalyticsEvents.EVENT_ONE_TIME_ACTIVATION)

        node?.let {
            executeOneTimeActivationInteractor.execute(it.name, water)
                .subscribe(
                    {
                        // no-op
                    },
                    { updateErrorEvent.postValue(null) }
                )
                .disposeOnClear()
        }
    }
}