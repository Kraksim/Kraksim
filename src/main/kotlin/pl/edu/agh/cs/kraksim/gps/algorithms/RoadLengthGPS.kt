package pl.edu.agh.cs.kraksim.gps.algorithms

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSType

@Component
class RoadLengthGPS : DijkstraBasedGPS() {

    fun calculate(source: Gateway, target: Gateway, state: SimulationState): GPS =
        super.calculate(
            source, target, state,
            { road ->
                road.physicalLength.toDouble()
            },
            GPSType.DIJKSTRA_ROAD_LENGTH
        )
}
