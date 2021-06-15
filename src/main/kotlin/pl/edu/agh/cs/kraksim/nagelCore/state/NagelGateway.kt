package pl.edu.agh.cs.kraksim.nagelCore.state

import pl.edu.agh.cs.kraksim.core.state.Gateway

class NagelGateway(
    override val id: Long,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
) : NagelRoadNode, Gateway {

    val finishedCars: ArrayList<NagelCar> = ArrayList()

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    fun addFinishedCar(car: NagelCar) {
        finishedCars.add(car)
        car.moveToLane(null)
    }

    override fun toString(): String {
        return "NagelGateway(id=$id, finishedCars=$finishedCars)"
    }
}
