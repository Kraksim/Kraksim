package pl.edu.agh.cs.kraksim.gps

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.generator.Generator
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS

@Component
class GpsFactory(
    private val roadLengthGPS: RoadLengthGPS
) {

    fun from(starterGateway: Gateway, generator: Generator, state: SimulationState): GPS {
        val target = requireNotNull(state.gateways[generator.targetGatewayId]) {
            "Gateway with id=${generator.targetGatewayId} doesn't exist in $state"
        }

        return when (GPSType.DIJKSTRA_ROAD_LENGTH) {
            GPSType.DIJKSTRA_ROAD_LENGTH -> calculateRoadLength(starterGateway, target, state)
        }
    }

    private fun calculateRoadLength(starterGateway: Gateway, target: Gateway, state: SimulationState): GPS =
        roadLengthGPS.calculate(starterGateway, target, state)

}

enum class GPSType {
    DIJKSTRA_ROAD_LENGTH
}
