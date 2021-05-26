package pl.edu.agh.cs.kraksim.nagelCore

interface NagelRoadNode {
    fun getPossibleRoads(lane: NagelLane): Collection<NagelRoad>
    fun canEnterNodeFrom(lane: NagelLane): Boolean // mówi czy można przejechać przez skrzyżowanie z danego pasa, Gatewaye zawsze zwracaja true
}