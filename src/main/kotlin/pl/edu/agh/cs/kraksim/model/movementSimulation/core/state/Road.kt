package pl.edu.agh.cs.kraksim.model.movementSimulation.core.state

interface Road {
    val id: Long
    val physicalLength: Int
    val lanes: List<Lane>

    val end: RoadNode
}
