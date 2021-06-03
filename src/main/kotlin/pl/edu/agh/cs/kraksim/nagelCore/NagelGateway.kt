package pl.edu.agh.cs.kraksim.nagelCore

class NagelGateway(
    val id: Long,
    override val endingRoads: List<NagelRoad>,
    override val startingRoads: List<NagelRoad>,
) : NagelRoadNode {

    val finishedCars: ArrayList<NagelCar> = ArrayList()

    init {
        endingRoads.forEach { it.setEnd(this) }
    }

    fun addCar(car: NagelCar) {
        finishedCars.add(car)
    }

    override fun toString(): String {
        return "NagelGateway(id=$id, finishedCars=$finishedCars)"
    }
}
