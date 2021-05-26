package pl.edu.agh.cs.kraksim.nagelCore

class NagelRoad(
    val id: Long,
    val lanes: List<NagelLane>,
    val start: NagelRoadNode,
    val end: NagelRoadNode,
    val length: Int,
) {


}