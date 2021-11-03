package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.core.state.Road

class NagelRoad(
    override val id: Long,
    override val physicalLength: Int,
) : Road {

    override lateinit var end: NagelRoadNode
    override val lanes: ArrayList<NagelLane> = ArrayList()

    fun addLane(laneId: Long, indexFromLeft: Int, physicalStartingPoint: Int, physicalEndingPoint: Int) {
        val newLane = NagelLane(laneId, indexFromLeft, this, physicalStartingPoint, physicalEndingPoint)
        lanes.add(newLane)
    }

    fun getLane(indexFromLeft: Int): NagelLane? {
        return lanes.firstOrNull { lane -> lane.indexFromLeft == indexFromLeft }
    }

    override fun toString(): String {
        return "NagelRoad(id=$id, lanes=$lanes)"
    }
}
