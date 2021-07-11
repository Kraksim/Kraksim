package pl.edu.agh.cs.kraksim.core.state

interface Road {
    val id: Long
    val physicalLength: Int
    val lanes: List<Lane>

    val end: RoadNode
}
