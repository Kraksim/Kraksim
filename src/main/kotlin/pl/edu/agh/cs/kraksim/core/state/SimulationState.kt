package pl.edu.agh.cs.kraksim.core.state

interface SimulationState {
    val id: Long
    var turn: Long

    val roads: List<Road>
    val gateways: List<Gateway>
    val intersections: List<Intersection>

    val lanes: List<Lane>
    val cars: List<Car>
}
