package pl.edu.agh.cs.kraksim.api.nagel

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.repository.entities.*

@Component
class NagelSimulationStateFactory(
    val nagelMapFactory: NagelMapFactory
) {

    fun from(entity: SimulationEntity): SimulationState {
        val (roads, intersections, gateways) = nagelMapFactory.from(entity.mapEntity)

        val trafficState = entity.trafficStateEntity
        val simState = NagelSimulationState(
            trafficState.id,
            roads,
            gateways,
            intersections,
            trafficState.turn
        )

        trafficState.carsOnMap.forEach { putCarOnMap(it, simState.roads) }
        trafficState.trafficLights.forEach { insertTrafficLightState(it, simState.intersections) }

        trafficState.gatewaysStates.forEach { adjustGatewayState(it, simState.gateways) }

        return simState
    }

    private fun adjustGatewayState(it: GatewayStateEntity, gateways: Map<GatewayId, NagelGateway>) {
        val gateway =
            requireNotNull(gateways[it.gatewayId]) { "Gateway id=${it.gatewayId} doesnt exist for GatewayStateEntity id=${it.id}" }

        it.collectedCars.map { createCar(it) }
            .forEach { gateway.addFinishedCar(it) }

    }

    private fun insertTrafficLightState(it: TrafficLightEntity, intersections: Map<IntersectionId, NagelIntersection>) {
        val intersection =
            requireNotNull(intersections[it.intersectionId]) { "Intersection id=${it.intersectionId} doesnt exist for TrafficLightEntity id=${it.id}" }

        it.phases.forEach { e: PhaseEntity ->
            val phase = intersection.phases[e.laneId]
                ?: throw IllegalStateException("Intersection id=${it.intersectionId} doesnt have phase for TrafficLightEntity id=${it.id}")
            phase.state = e.state
            phase.phaseTime = e.phaseTime
        }
    }

    private fun putCarOnMap(it: CarEntity, roads: Map<RoadId, NagelRoad>) {
        val lanes: Map<LaneId, NagelLane> = roads.values.flatMap { it.lanes }.associateBy { it.id }
        val car = createCar(it, roads)
        car.moveToLane(lanes[it.currentLaneId], it.positionRelativeToStart)
    }

    private fun createCar(it: CarEntity, roads: Map<RoadId, NagelRoad>? = null): NagelCar {
        val route = it.gps.route.mapNotNull { roads?.get(it) } // empty list if roads is null
        val gps = GPS(route)

        return NagelCar(
            id = it.id,
            velocity = it.velocity,
            gps = gps
        )
    }
}
