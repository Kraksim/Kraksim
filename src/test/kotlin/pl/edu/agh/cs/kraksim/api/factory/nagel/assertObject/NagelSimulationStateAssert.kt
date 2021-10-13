package pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.SimulationStateEntity

class NagelSimulationStateAssert(
    private val state: NagelSimulationState
) {
    fun assertTurn(stateEntity: SimulationStateEntity): NagelSimulationStateAssert {
        assertThat(state.turn).isEqualTo(stateEntity.turn)
        return this
    }

    fun assertCarsState(stateEntity: SimulationStateEntity): NagelSimulationStateAssert {
        stateEntity.trafficLights
        state.cars.forEach { car ->
            val carEntity = stateEntity.carsOnMap.find { it.carId == car.id }
            assertThat(carEntity).isNotNull
            assertThat(car.currentLane?.id).isEqualTo(carEntity!!.currentLaneId)
            assertThat(car.gps.type).isEqualTo(carEntity.gps.type)
            val parsedRouteIds = car.gps.route.map { it.id }
            val entityRouteIds = carEntity.gps.route
            assertThat(parsedRouteIds.size).isEqualTo(entityRouteIds.size)
            parsedRouteIds.indices.forEach { index -> assertThat(parsedRouteIds[index]).isEqualTo(entityRouteIds[index]) }
            assertThat(car.positionRelativeToStart * Car.AVERAGE_CAR_LENGTH).isEqualTo(carEntity.positionRelativeToStart)
            assertThat(car.velocity).isEqualTo(carEntity.velocity)
        }
        return this
    }

    fun assertTrafficLightPhases(stateEntity: SimulationStateEntity): NagelSimulationStateAssert {
        stateEntity.trafficLights.forEach { trafficLightsEntity ->
            val intersection = state.intersections[trafficLightsEntity.intersectionId]
            assertThat(intersection).isNotNull
            trafficLightsEntity.phases.forEach { phaseEntity ->
                val parsedPhase = intersection!!.phases[phaseEntity.laneId]
                assertThat(parsedPhase).isNotNull
                assertThat(parsedPhase!!.phaseTime).isEqualTo(phaseEntity.phaseTime)
                assertThat(parsedPhase.state).isEqualTo(parsedPhase.state)
            }
        }
        return this
    }
}
