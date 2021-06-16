package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.Road
import pl.edu.agh.cs.kraksim.core.RoadNode

class NagelRoad(
    override val id: Long,
    override val physicalLength: Int,
) : Road {

    override val lanes: ArrayList<NagelLane> = ArrayList()

    private var end: NagelRoadNode? = null

    override fun setEnd(end: RoadNode) {
        this.end = end as NagelRoadNode
    }

    override fun end(): NagelRoadNode {
        return end!!
    }

    fun addLane(laneId: Long, indexFromLeft: Int, physicalStartingPoint: Int, physicalEndingPoint: Int) {
        val newLane = NagelLane(laneId, indexFromLeft, this, physicalStartingPoint, physicalEndingPoint)
        lanes.add(newLane)
    }

    override fun toString(): String {
        return "NagelRoad(id=$id, lanes=$lanes)"
    }
}
