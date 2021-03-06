package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.generator.Generator
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.*
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*

@Component
class NagelSimulationStateFactory(
    val nagelMapFactory: NagelMapFactory
) {
    fun toEntity(
        state: NagelSimulationState,
        simulationEntity: SimulationEntity,
    ): SimulationStateEntity {
        return SimulationStateEntity(
            turn = state.turn,
            stateType = StateType.NAGEL_SCHRECKENBERG,
            carsOnMap = state.cars.map { carToCarEntity(it) },
            gatewaysStates = state.gateways.entries.map { (id, gateway) ->
                GatewayStateEntity(
                    gatewayId = id,
                    collectedCars = gateway.finishedCars.map { carToCarEntity(it) },
                    generators = gateway.generators.map { generator ->
                        GeneratorEntity(
                            generator.lastCarReleasedTurnsAgo,
                            generator.releaseDelay,
                            generator.carsToRelease,
                            generator.targetGatewayId,
                            generator.gpsType
                        )
                    }
                )
            },
            trafficLights = state.intersections.map { (intersectionId, intersection) ->
                TrafficLightEntity(
                    intersectionId = intersectionId,
                    phases = intersection.phases.map { (laneId, phase) ->
                        PhaseEntity(
                            laneId = laneId,
                            phaseTime = phase.phaseTime,
                            state = phase.state,
                            period = phase.period
                        )
                    }
                )
            }
        )
    }

    fun from(entity: SimulationEntity): NagelSimulationState {
        val (roads, intersections, gateways) = nagelMapFactory.from(entity.mapEntity)

        val trafficState = entity.latestTrafficStateEntity
        val simState = NagelSimulationState(
            trafficState.id,
            roads,
            gateways,
            intersections,
            trafficState.turn
        )

        trafficState.carsOnMap
            .groupBy { it.currentLaneId }
            .forEach { (_, cars) ->
                cars
                    .sortedByDescending { it.positionRelativeToStart }
                    .forEach { putCarOnMap(it, simState.roads) }
            }
        trafficState.trafficLights.forEach { insertTrafficLightState(it, simState.intersections) }

        trafficState.gatewaysStates.forEach { adjustGatewayState(it, simState.gateways) }

        return simState
    }

    private fun adjustGatewayState(gatewayStateEntity: GatewayStateEntity, gateways: Map<GatewayId, NagelGateway>) {
        val gateway =
            requireNotNull(gateways[gatewayStateEntity.gatewayId]) { "Gateway id=${gatewayStateEntity.gatewayId} doesnt exist for GatewayStateEntity id=${gatewayStateEntity.id}" }

        gatewayStateEntity.collectedCars.map { createCar(it) }
            .forEach { gateway.addFinishedCar(it) }

        gateway.generators = gatewayStateEntity.generators.map { generatorEntity ->
            Generator(
                generatorEntity.lastCarReleasedTurnsAgo,
                generatorEntity.releaseDelay,
                generatorEntity.carsToRelease,
                generatorEntity.targetGatewayId,
                generatorEntity.gpsType,
                generatorEntity.id
            )
        }
    }

    private fun insertTrafficLightState(trafficLightEntity: TrafficLightEntity, intersections: Map<IntersectionId, NagelIntersection>) {
        val intersection =
            requireNotNull(intersections[trafficLightEntity.intersectionId]) { "Intersection id=${trafficLightEntity.intersectionId} doesnt exist for TrafficLightEntity id=${trafficLightEntity.id}" }

        trafficLightEntity.phases.forEach { e: PhaseEntity ->
            val phase = intersection.phases[e.laneId]
                ?: throw IllegalStateException("Intersection id=${trafficLightEntity.intersectionId} doesnt have phase for TrafficLightEntity id=${trafficLightEntity.id}")
            phase.state = e.state
            phase.phaseTime = e.phaseTime
            phase.period = e.period
        }
    }

    private fun putCarOnMap(carEntity: CarEntity, roads: Map<RoadId, NagelRoad>) {
        require(carEntity.positionRelativeToStart % Car.AVERAGE_CAR_LENGTH == 0) { "Car id=${carEntity.carId} should have positionRelativeToStart divisible by AVERAGE_CAR_LENGTH=${Car.AVERAGE_CAR_LENGTH}, but has ${carEntity.positionRelativeToStart}" }
        val lanes: Map<LaneId, NagelLane> = roads.values.flatMap { it.lanes }.associateBy { it.id }
        val car = createCar(carEntity, roads, lanes[carEntity.currentLaneId])
        car.moveToLaneFront(lanes[carEntity.currentLaneId], carEntity.positionRelativeToStart / Car.AVERAGE_CAR_LENGTH)
    }

    private fun createCar(
        carEntity: CarEntity,
        roads: Map<RoadId, NagelRoad>? = null,
        currentLane: NagelLane? = null
    ): NagelCar {
        val route = carEntity.gps.route.mapNotNull { roads?.get(it) } // empty list if roads is null
        val gps = GPS(route, carEntity.gps.type)
        if (currentLane?.parentRoad != null) {
            gps.currentRoad = currentLane.parentRoad
        }

        return NagelCar(
            id = carEntity.carId,
            velocity = carEntity.velocity,
            gps = gps,
            brakeLightOn = carEntity.brakeLightOn
        )
    }

    private fun carToCarEntity(car: NagelCar): CarEntity {
        return CarEntity(
            carId = car.id,
            velocity = car.velocity,
            currentLaneId = car.currentLane?.id,
            positionRelativeToStart = car.positionRelativeToStart * Car.AVERAGE_CAR_LENGTH,
            gps = GPSEntity(
                route = car.gps.route.map { route -> route.id },
                type = car.gps.type
            ),
            brakeLightOn = car.brakeLightOn
        )
    }
}
