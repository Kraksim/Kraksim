package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class RoadLengthGPS(
    source: Gateway,
    target: Gateway,
    state: SimulationState
) : DijkstraBasedGps(source, target, state) {

    override fun getRoadWeight(node: Road): Double =
        node.physicalLength.toDouble()
}
