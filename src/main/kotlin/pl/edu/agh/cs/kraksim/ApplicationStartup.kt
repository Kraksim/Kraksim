package pl.edu.agh.cs.kraksim

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.random.TrueRandomProvider
import pl.edu.agh.cs.kraksim.core.state.IntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.gps.RoadLengthGPS
import pl.edu.agh.cs.kraksim.nagelCore.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulation
import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseStrategyType
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor

@Component
class ApplicationStartup : CommandLineRunner {
    override fun run(vararg args: String?) {
        val road1 = NagelRoad(1, 18)
        val road2 = NagelRoad(2, 18)
        val road3 = NagelRoad(3, 18)
        val road4 = NagelRoad(4, 18)

        road1.addLane(1, 0, 0, 18)
        road2.addLane(2, 0, 0, 18)
        road3.addLane(3, 0, 0, 18)
        road4.addLane(4, 0, 0, 18)

        val roads = listOf(road1, road2, road3, road4)

        val gateway1 = NagelGateway(
            id = 1,
            startingRoads = listOf(road1),
            endingRoads = emptyList()
        )

        val gateway2 = NagelGateway(
            id = 2,
            startingRoads = listOf(road2),
            endingRoads = emptyList()
        )

        val gateway3 = NagelGateway(
            id = 3,
            startingRoads = emptyList(),
            endingRoads = listOf(road3)
        )

        val gateway4 = NagelGateway(
            id = 4,
            startingRoads = emptyList(),
            endingRoads = listOf(road4)
        )

        val gateways = listOf(gateway1, gateway2, gateway3, gateway4)

        val directions = setOf(
            IntersectionTurningLaneDirection(road1.lanes[0].id, road3.id),
            IntersectionTurningLaneDirection(road1.lanes[0].id, road4.id),
            IntersectionTurningLaneDirection(road2.lanes[0].id, road3.id),
            IntersectionTurningLaneDirection(road2.lanes[0].id, road4.id)
        )

        val phases: Map<Lane, TrafficLightPhase> = mapOf(
            road1.lanes[0] to TrafficLightPhase(Int.MAX_VALUE, LightColor.GREEN),
            road2.lanes[0] to TrafficLightPhase(Int.MAX_VALUE, LightColor.GREEN)
        )

        val intersection = NagelIntersection(
            id = 1,
            endingRoads = listOf(road1, road2),
            startingRoads = listOf(road3, road4),
            directions = directions,
            phases = phases
        )

        val state = NagelSimulationState(
            id = 1,
            turn = 1,
            gateways = gateways,
            roads = roads,
            intersections = listOf(intersection)
        )

        val lightPhaseManager = LightPhaseManager(
            state,
            mapOf(LightPhaseStrategyType.TURN_BASED to listOf(intersection.id))
        )

        val simulation = NagelSimulation(
            state, NagelMovementSimulationStrategy(TrueRandomProvider()),
            lightPhaseManager
        )

        val car1 = NagelCar(
            velocity = 4,
            RoadLengthGPS(gateway1, gateway3, state)
        )
        car1.moveToLane(road1.lanes[0], 0)

        val car2 = NagelCar(
            velocity = 4,
            RoadLengthGPS(gateway2, gateway3, state)
        )
        car2.moveToLane(road2.lanes[0], 2)

        val car3 = NagelCar(
            velocity = 4,
            RoadLengthGPS(gateway2, gateway4, state)
        )
        car3.moveToLane(road2.lanes[0], 0)

        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
        simulation.step()
        println(state.toString() + "\n")
    }
}
