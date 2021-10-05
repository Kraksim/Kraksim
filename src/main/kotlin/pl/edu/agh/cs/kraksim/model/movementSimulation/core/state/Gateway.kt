package pl.edu.agh.cs.kraksim.model.movementSimulation.core.state

import pl.edu.agh.cs.kraksim.model.generator.Generator

interface Gateway : RoadNode {
    val finishedCars: List<Car>
    var generators: List<Generator>
}
