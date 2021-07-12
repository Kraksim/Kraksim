package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.partitionByType
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.IntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.core.state.RoadNode
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelGateway
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelIntersection
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelRoad
import pl.edu.agh.cs.kraksim.repository.entities.*
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

@Component
class NagelMapFactory {

    fun from(entity: MapEntity): Triple<List<NagelRoad>, List<NagelIntersection>, List<NagelGateway>> {
        val roads = entity.roads.map(this::nagelSchreckenbergRoad)
        val roadIdMap = roads.associateBy { it.id }

        val roadNodes = entity.roadNodes.map { this.roadNodeFrom(it, roadIdMap) }
        val (intersections, gateways) = roadNodes.partitionByType<NagelIntersection, NagelGateway, RoadNode>()

        return Triple(roads, intersections, gateways)
    }

    private fun nagelSchreckenbergRoad(e: RoadEntity): NagelRoad {
        val nagelRoad = NagelRoad(
            id = e.id,
            physicalLength = e.length
        )

        e.lanes.forEach {
            nagelRoad.addLane(it.id, it.indexFromLeft, it.startingPoint, it.endingPoint)
        }

        return nagelRoad
    }

    private fun roadNodeFrom(node: RoadNodeEntity, roadIdMap: Map<Long, NagelRoad>) = when (node.type) {
        RoadNodeType.INTERSECTION -> createIntersection(node, roadIdMap)
        RoadNodeType.GATEWAY -> createGateway(node, roadIdMap)
    }

    private fun createIntersection(node: RoadNodeEntity, roadIdMap: Map<Long, NagelRoad>): Intersection {
        requireNotNull(node.turnDirections) { "Invalid RoadNodeEntity id=${node.id}, for type intersection expected not null turnDirections" }

        return NagelIntersection(
            id = node.id,
            directions = node.turnDirections!!.map(this::createTurnDirectionFrom),
            endingRoads = node.endingRoads.map { roadIdMap[it]!! },
            startingRoads = node.startingRoads.map { roadIdMap[it]!! },
            // values of phases are defined by state, here we insert any (MAX_VALUE, RED) and replace them when given state
            phases = node.endingRoads.flatMap { roadIdMap[it]!!.lanes }
                .associate { lane -> lane.id to TrafficLightPhase(Int.MAX_VALUE, TrafficLightPhase.LightColor.RED) }
        )
    }

    private fun createGateway(node: RoadNodeEntity, roadIdMap: Map<Long, NagelRoad>): Gateway {
        return NagelGateway(
            id = node.id,
            endingRoads = node.endingRoads.map { roadIdMap[it]!! },
            startingRoads = node.startingRoads.map { roadIdMap[it]!! }
        )
    }

    private fun createTurnDirectionFrom(node: TurnDirectionEntity) =
        IntersectionTurningLaneDirection(
            from = node.sourceLaneId,
            to = node.destinationRoadId
        )
}
