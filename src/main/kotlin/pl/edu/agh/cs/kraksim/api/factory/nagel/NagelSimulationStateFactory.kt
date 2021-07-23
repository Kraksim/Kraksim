package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulation
import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.*

@Component
class NagelSimulationStateFactory(
    val nagelMapFactory: NagelMapFactory
) {
    fun toEntity(
            state: NagelSimulationState,
            simulationEntity: SimulationEntity,
    ): TrafficStateEntity{
        return TrafficStateEntity(
                turn = state.turn,
                simulation = simulationEntity,
                stateType = StateType.NAGEL_SCHRECKENBERG,
                carsOnMap = state.cars.map { carToCarEntity(it) },
                gatewaysStates = state.gateways.entries.map { (id, gateway) -> GatewayStateEntity(
                        gatewayId = id, //to na 90% jest zle bo musi tu byc gatewayId a nie id
                        collectedCars = gateway.finishedCars.map { carToCarEntity(it) },
                        generators = //???? co tu
                    )
                },
                trafficLights = state.intersections.map {(intersectionId, intersection) -> TrafficLightEntity(
                        intersectionId = intersectionId,
                        phases = intersection.phases.map{ (laneId, phase) -> PhaseEntity(
                                laneId = laneId,
                                phaseTime = phase.phaseTime,
                                state = phase.state
                        ) }
                ) }
        )
    }
    fun from(entity: SimulationEntity): SimulationState {
        val (roads, intersections, gateways) = nagelMapFactory.from(entity.mapEntity)

        val trafficState = entity.latestTrafficStateEntity
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
        val gps = GPS(route, it.gps.type)

        return NagelCar(
            id = it.carId,
            velocity = it.velocity,
            gps = gps
        )
    }

    private fun carToCarEntity(car: NagelCar): CarEntity{
        return CarEntity(
                carId = car.id,
                velocity = car.velocity,
                currentLaneId = car.currentLane?.id!!,
                positionRelativeToStart = car.positionRelativeToStart,
                gps = GPSEntity(
                        route = car.gps.route.map { route -> route.id },
                        type = car.gps.type
                )
        )
    }
}
