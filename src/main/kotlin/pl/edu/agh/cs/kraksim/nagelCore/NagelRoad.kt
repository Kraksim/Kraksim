package pl.edu.agh.cs.kraksim.nagelCore

class NagelRoad(
    val id: Long,
    val physicalLength: Int,
) {

    val lanes: ArrayList<NagelLane> = ArrayList()
    private var end: NagelRoadNode? = null

    fun setEnd(end: NagelRoadNode) {
        this.end = end
    }

    fun end(): NagelRoadNode {
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