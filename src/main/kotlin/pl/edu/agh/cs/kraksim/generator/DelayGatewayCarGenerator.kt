package pl.edu.agh.cs.kraksim.generator

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.core.state.*
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GpsFactory
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.StateType
import kotlin.random.Random

@Component
class DelayGatewayCarGenerator(
    private val gpsFactory: GpsFactory
): GatewayCarGenerator {

    override fun generate(state: SimulationState) {
        state.gateways.values.forEach { gateway: Gateway ->
            gateway.generateCars(state)
        }
    }

    private fun Gateway.generateCars(state: SimulationState) {
        removeEmptyGenerators()

        val releaseGenerators = getGeneratorsToReleaseNow()
        val generatorsThatReleased = releaseNewCarsIfSpace(releaseGenerators, state)

        increaseTurnData(generatorsThatReleased)
    }

    private fun Gateway.removeEmptyGenerators() {
        generators = generators?.filter { it.carsToRelease != 0 }
    }

    private fun Gateway.getGeneratorsToReleaseNow() =
        generators?.filter { it.releaseDelay == it.lastCarReleasedTurnsAgo }

    private fun Gateway.releaseNewCarsIfSpace(
        releaseGenerators: List<Generator>?,
        state: SimulationState
    ) = releaseGenerators?.mapNotNull { generator: Generator ->
        val gps = gpsFactory.from(this, generator, state)
        val car: Car = generateCar(state.type, gps)
        val lane = getRandomLaneToStartFrom(gps)

        return@mapNotNull if (lane != null) {
            car.moveToLane(lane)
            generator
        } else null
    }

    private fun Gateway.increaseTurnData(generatorsThatReleased: List<Generator>?) {
        generators?.forEach { it.lastCarReleasedTurnsAgo++ }

        generatorsThatReleased?.forEach {
            it.carsToRelease--
            it.lastCarReleasedTurnsAgo = 0
        }
    }

    private fun generateCar(type: StateType, gps: GPS): Car = when (type) {
        StateType.NAGEL_SCHRECKENBERG -> generateNagelCar(gps)
    }

    private fun getRandomLaneToStartFrom(gps: GPS) = gps.popNext().getFreeLanes().randomOrNull()

    private fun Road.getFreeLanes(): List<Lane> =
        this.lanes.filter { it.cars.getOrNull(0)?.positionRelativeToStart != 0 }

    private fun generateNagelCar(gps: GPS): NagelCar {
        return NagelCar(
            id = Random.nextLong(),
            velocity = 0, // todo moze jakies losowanko z jaka predkoscia wjezdza?
            gps = gps

        )
    }
}

