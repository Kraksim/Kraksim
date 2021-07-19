package pl.edu.agh.cs.kraksim

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS
import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.repository.CarRepository
import pl.edu.agh.cs.kraksim.repository.SimulationRepository
import pl.edu.agh.cs.kraksim.repository.TrafficStateRepository
import pl.edu.agh.cs.kraksim.repository.entities.CarEntity
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.TrafficStateEntity
import pl.edu.agh.cs.kraksim.repository.entities.TrafficStateId

@Component
class ApplicationStartup(
        val roadLengthGPS: RoadLengthGPS,
        val rep: CarRepository,
        val tfrep: TrafficStateRepository,
        val simprep: SimulationRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val simpog = SimulationEntity(null, ArrayList(), null)
        val trafficStateEntity = TrafficStateEntity(1, simulation = simpog)
        simpog.trafficStateEntity += trafficStateEntity
        simprep.save(simpog)
        val pog = CarEntity(3, 1, 5, null, TrafficStateId(trafficStateEntity, 10))
        val champ = CarEntity(3, 1, 5, null, TrafficStateId(trafficStateEntity, 11))
        rep.save(pog)
        rep.save(champ)

        val nextTrafficStateEntity = TrafficStateEntity(2, simulation = simpog)
        simpog.trafficStateEntity += nextTrafficStateEntity
        tfrep.save(nextTrafficStateEntity)
        simprep.save(simpog)
        val pog2 = CarEntity(3, 1, 5, null, TrafficStateId(nextTrafficStateEntity, pog.trafficStateId.id))
        val champ2 = CarEntity(3, 1, 5, null, TrafficStateId(nextTrafficStateEntity, champ.trafficStateId.id) )
        rep.save(pog2)
        rep.save(champ2)


//        val road1 = NagelRoad(1, 18)
//        val road2 = NagelRoad(2, 18)
//        val road3 = NagelRoad(3, 18)
//        val road4 = NagelRoad(4, 18)
//
//        road1.addLane(1, 0, 0, 18)
//        road2.addLane(2, 0, 0, 18)
//        road3.addLane(3, 0, 0, 18)
//        road4.addLane(4, 0, 0, 18)
//
//        val roads = listOf(road1, road2, road3, road4)
//
//        val gateway1 = NagelGateway(
//            id = 1,
//            startingRoads = listOf(road1),
//            endingRoads = emptyList()
//        )
//
//        val gateway2 = NagelGateway(
//            id = 2,
//            startingRoads = listOf(road2),
//            endingRoads = emptyList()
//        )
//
//        val gateway3 = NagelGateway(
//            id = 3,
//            startingRoads = emptyList(),
//            endingRoads = listOf(road3)
//        )
//
//        val gateway4 = NagelGateway(
//            id = 4,
//            startingRoads = emptyList(),
//            endingRoads = listOf(road4)
//        )
//
//        val gateways = listOf(gateway1, gateway2, gateway3, gateway4)
//
//        val directions = listOf(
//            IntersectionTurningLaneDirection(road1.lanes[0].id, road3.id),
//            IntersectionTurningLaneDirection(road1.lanes[0].id, road4.id),
//            IntersectionTurningLaneDirection(road2.lanes[0].id, road3.id),
//            IntersectionTurningLaneDirection(road2.lanes[0].id, road4.id)
//        )
//
//        val phases: Map<LaneId, TrafficLightPhase> = mapOf(
//            road1.lanes[0].id to TrafficLightPhase(Int.MAX_VALUE, LightColor.GREEN),
//            road2.lanes[0].id to TrafficLightPhase(Int.MAX_VALUE, LightColor.GREEN)
//        )
//
//        val intersection = NagelIntersection(
//            id = 1,
//            endingRoads = listOf(road1, road2),
//            startingRoads = listOf(road3, road4),
//            directions = directions,
//            phases = phases
//        )
//
//        val state = NagelSimulationState(
//            id = 1,
//            turn = 1,
//            gateways = gateways,
//            roads = roads,
//            intersections = listOf(intersection)
//        )
//
//        val lightPhaseManager = LightPhaseManager(
//            state,
//            mapOf(TurnBasedLightPhaseStrategy() to listOf(intersection.id))
//        )
//
//        val simulation = NagelSimulation(
//            state, NagelMovementSimulationStrategy(TrueRandomProvider(0.5)),
//            lightPhaseManager,
//            StatisticsManager()
//        )
//
//        val car1 = NagelCar(
//            0,
//            velocity = 4,
//            roadLengthGPS.calculate(gateway1, gateway3, state)
//        )
//        car1.moveToLane(road1.lanes[0], 0)
//
//        val car2 = NagelCar(
//            1,
//            velocity = 4,
//            roadLengthGPS.calculate(gateway2, gateway3, state)
//        )
//        car2.moveToLane(road2.lanes[0], 2)
//
//        val car3 = NagelCar(
//            2,
//            velocity = 4,
//            roadLengthGPS.calculate(gateway2, gateway4, state)
//        )
//        car3.moveToLane(road2.lanes[0], 0)
//
//        repeat(22) {
//            println(state.toString() + "\n")
//            simulation.step()
//        }
    }
}
