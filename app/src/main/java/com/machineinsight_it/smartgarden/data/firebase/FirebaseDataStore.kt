package com.machineinsight_it.smartgarden.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.machineinsight_it.smartgarden.data.model.ActivationData
import com.machineinsight_it.smartgarden.data.model.NodeData
import com.machineinsight_it.smartgarden.data.model.PlanItemData
import io.reactivex.Completable
import io.reactivex.Maybe
import java.text.SimpleDateFormat
import java.util.*

private const val TIME_FORMAT = "HH:mm"
private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

class FirebaseDataStore(private val databaseReference: DatabaseReference) {
    private val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    private val dateTimeFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())

    fun fetchSchedules(): Maybe<List<NodeData>> {
        return Maybe.create { emitter ->
            databaseReference.child("nodes")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            try {
                                val nodes = mutableListOf<NodeData>()
                                snapshot.children.forEach { schedule ->
                                    parseScheduleData(schedule)?.let { nodes.add(it) }
                                }

                                emitter.onSuccess(nodes)
                            } catch (error: Exception) {
                                if (!emitter.isDisposed) {
                                    emitter.onError(error)
                                }
                            }
                        } else {
                            emitter.onComplete()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        if (!emitter.isDisposed) {
                            emitter.onError(error.toException())
                        }
                    }
                })
        }
    }

    fun updateSchedule(node: String, schedule: List<PlanItemData>): Completable {
        return Completable.create { emitter ->
            try {
                val map = hashMapOf<String, Map<String, Any>>()
                schedule.forEachIndexed { index, item ->
                    val obj = mapOf(
                        "time" to timeFormatter.format(item.time),
                        "water" to item.water
                    )
                    map[index.toString()] = obj
                }

                databaseReference.child("nodes").child(node).child("plan")
                    .setValue(map as Map<String, Any>)
                emitter.onComplete()
            } catch (e: java.lang.Exception) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }

    private fun parseScheduleData(data: DataSnapshot): NodeData? {
        val name = data.key ?: ""
        val active: Boolean
        val healthCheck: Date
        val lastActivation: ActivationData
        val plan = mutableListOf<PlanItemData>()

        if (data.child("active").exists()) {
            active = data.child("active").value as Boolean
        } else {
            return null
        }

        if (data.child("health_check").exists()) {
            healthCheck = dateTimeFormatter.parse(data.child("health_check").value.toString())
        } else {
            return null
        }

        if (data.child("last_activation").exists()) {
            val activation = parseActivationData(data.child("last_activation"))
            if (activation == null) {
                return null
            } else {
                lastActivation = activation
            }
        } else {
            return null
        }

        if (data.child("plan").exists()) {
            data.child("plan").children.forEach { planItem ->
                parsePlanItemData(planItem)?.let { plan.add(it) }
            }
        } else {
            return null
        }

        return NodeData(active, healthCheck, lastActivation, name, plan)
    }

    private fun parseActivationData(data: DataSnapshot): ActivationData? {
        val timeStamp: Date
        val water: Long

        if (data.child("timestamp").exists()) {
            timeStamp = dateTimeFormatter.parse(data.child("timestamp").value.toString())
        } else {
            return null
        }

        if (data.child("water").exists()) {
            water = data.child("water").value as Long
        } else {
            return null
        }

        return ActivationData(timeStamp, water)
    }

    private fun parsePlanItemData(data: DataSnapshot): PlanItemData? {
        val time: Date
        val water: Long

        if (data.child("time").exists()) {
            time = timeFormatter.parse(data.child("time").value.toString())
        } else {
            return null
        }

        if (data.child("water").exists()) {
            water = data.child("water").value as Long
        } else {
            return null
        }

        return PlanItemData(time, water)
    }
}