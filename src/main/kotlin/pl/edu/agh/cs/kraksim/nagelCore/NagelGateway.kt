package pl.edu.agh.cs.kraksim.nagelCore

class NagelGateway(
    val id: Long,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
) : NagelRoadNode {

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    val finishedCars: ArrayList<NagelCar> = ArrayList()

    override fun canEnterNodeFrom(lane: NagelLane): Boolean {
        return endingRoads.flatMap { it.lanes }
            .contains(lane)
    }

    fun addCar(car: NagelCar) {
        finishedCars.add(car)
    }
}
