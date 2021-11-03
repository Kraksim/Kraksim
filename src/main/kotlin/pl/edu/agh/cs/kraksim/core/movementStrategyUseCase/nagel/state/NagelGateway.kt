package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.generator.Generator

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
        car.moveToLaneFront(null)
    }

    override fun toString(): String {
        return "NagelGateway(id=$id, finishedCars=$finishedCars)"
    }
}
