package pl.edu.agh.cs.kraksim.nagelCore

class Road(
    val id: Long,
    val lanes: List<NagelLane>,
    val start: RoadNode,
    val end: RoadNode,
    val length: Int
) {


}