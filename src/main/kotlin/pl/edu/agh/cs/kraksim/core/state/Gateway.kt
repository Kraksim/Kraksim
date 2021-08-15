package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.generator.Generator

interface Gateway : RoadNode {
    val finishedCars: List<Car>
    var generators: List<Generator>
}
