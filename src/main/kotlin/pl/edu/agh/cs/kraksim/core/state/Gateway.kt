package pl.edu.agh.cs.kraksim.core.state

interface Gateway : RoadNode {
    val finishedCars: List<Car>
}
