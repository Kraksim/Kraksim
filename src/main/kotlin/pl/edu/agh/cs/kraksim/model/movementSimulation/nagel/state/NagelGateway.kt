package pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.Gateway
import pl.edu.agh.cs.kraksim.model.generator.Generator

class NagelGateway(
    override val id: Long,
    endingRoads: List<NagelRoad>,
    startingRoads: List<NagelRoad>,
    override var generators: List<Generator> = emptyList()
) : NagelRoadNode, Gateway {

    override val finishedCars: ArrayList<NagelCar> = ArrayList()
    override val startingRoads: Map<RoadId, NagelRoad> = startingRoads.associateBy { it.id }
    override val endingRoads: Map<RoadId, NagelRoad> = endingRoads.associateBy { it.id }

    init {
        this.endingRoads.values.forEach { it.end = this }
    }

    fun addFinishedCar(car: NagelCar) {
        finishedCars.add(car)
        car.moveToLane(null)
    }

    override fun toString(): String {
        return "NagelGateway(id=$id, finishedCars=$finishedCars)"
    }
}
