package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.nagelCore.state.NagelRoadNode

interface Road {
    val id: Long
    val physicalLength: Int
    val lanes: List<Lane>

    val end: NagelRoadNode
}
