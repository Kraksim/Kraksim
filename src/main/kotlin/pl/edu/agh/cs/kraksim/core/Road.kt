package pl.edu.agh.cs.kraksim.core

interface Road {
    val id: Long
    val physicalLength: Int
    val lanes: List<Lane>

    fun setEnd(end: RoadNode)
    fun end(): RoadNode
}