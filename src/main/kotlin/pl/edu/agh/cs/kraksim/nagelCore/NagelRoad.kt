package pl.edu.agh.cs.kraksim.nagelCore

class NagelRoad(
    val id: Long,
    val length: Int,
) {

    val lanes: ArrayList<NagelLane> = ArrayList()
    private var end: NagelRoadNode? = null

    fun setEnd(end: NagelRoadNode) {
        this.end = end
    }

    fun end(): NagelRoadNode {
        return end!!
    }

    /*
     private val id: Long,
    val indexFromLeft: Int,
    val parentRoad: NagelRoad,
    startingPoint: Int,
    endingPoint: Int,
     */

    fun addLane(laneId: Long, indexFromLeft: Int, startingPoint: Int, endingPoint: Int) {
        val newLane = NagelLane(laneId, indexFromLeft, this, startingPoint, endingPoint)
        lanes.add(newLane)
    }

    override fun toString(): String {
        return "NagelRoad(id=$id, length=$length, lanes=$lanes)"
    }

}