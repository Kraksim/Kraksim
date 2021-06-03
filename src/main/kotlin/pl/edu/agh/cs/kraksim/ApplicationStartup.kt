package pl.edu.agh.cs.kraksim

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.comon.RandomProvider
import pl.edu.agh.cs.kraksim.nagelCore.*
import pl.edu.agh.cs.kraksim.nagelCore.simulation.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.simulation.NagelSimulation

@Component
class ApplicationStartup : CommandLineRunner {
    override fun run(vararg args: String?) {


        val road1 = NagelRoad(1, 10)
        val road2 = NagelRoad(2, 9)
        val road3 = NagelRoad(3, 8)
        val road4 = NagelRoad(4, 7)

        road1.addLane(1, 0, 0, 10)
        road2.addLane(2, 0, 0, 9)
        road3.addLane(3, 0, 0, 5)
        road4.addLane(4, 0, 0, 7)

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
            NagelIntersectionTurningLaneDirection(road1.lanes[0], road3),
            NagelIntersectionTurningLaneDirection(road1.lanes[0], road4),
            NagelIntersectionTurningLaneDirection(road2.lanes[0], road3),
            NagelIntersectionTurningLaneDirection(road2.lanes[0], road4)
        )
        val intersection = NagelIntersection(
            id = 1,
            endingRoads = listOf(road1, road2),
            startingRoads = listOf(road3, road4),
            directions = directions
        )

        val state = NagelSimulationState(
            gateways = gateways,
            roads = roads,
            intersections = listOf(intersection)
        )

        val simulation = NagelSimulation(state, NagelMovementSimulationStrategy(RandomProvider()))

        val car1 = NagelCar(
            currentLane = road1.lanes[0],
            velocity = 4,
            positionRelativeToStart = 0
        )

        road1.lanes[0].addCar(car1)

        val car2 = NagelCar(
            currentLane = road2.lanes[0],
            velocity = 4,
            positionRelativeToStart = 2
        )

        road2.lanes[0].addCar(car2)

        val car3 = NagelCar(
            currentLane = road2.lanes[0],
            velocity = 4,
            positionRelativeToStart = 0
        )

        road2.lanes[0].addCar(car3)

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