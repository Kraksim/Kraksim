package pl.edu.agh.cs.kraksim.nagelCore

interface NagelRoadNode {

    val endingRoads: List<NagelRoad>
    val startingRoads: List<NagelRoad>

    fun canEnterNodeFrom(lane: NagelLane): Boolean // mówi czy można przejechać przez skrzyżowanie z danego pasa, Gatewaye zawsze zwracaja true
}