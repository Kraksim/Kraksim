package pl.edu.agh.cs.kraksim.nagelCore

class NagelGateway(
    val id: Long,
    val connectedRoads: List<NagelRoad>
) : NagelRoadNode {

    val finishedCars: ArrayList<NagelCar> = ArrayList()

    override fun canEnterNodeFrom(lane: NagelLane): Boolean {
        return true
    }

    fun addCar(car: NagelCar) {
        finishedCars.add(car)
    }
}
