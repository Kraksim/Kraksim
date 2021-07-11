package pl.edu.agh.cs.kraksim.api

import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.RoadNode
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelIntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelRoad
import pl.edu.agh.cs.kraksim.repository.entities.*

class MapFactory {

    fun from(entity: MapEntity, stateType: StateType) {

//        entity.


    }

    private fun roadFrom(roadEntity: RoadEntity) {

    }


    class NagelMapFactory {

        fun from(entity: MapEntity): Triple<List<NagelRoad>, List<RoadNode>, List<RoadNode>> {
            val roads = entity.roads.map(this::nagelSchreckenbergRoad)
            val roadIdMap = roads.associateBy { it.id }

            val roadNodes = entity.roadNodes.map { this.roadNodeFrom(it, roadIdMap) }
            val (intersections, gateways) = roadNodes.partition { it is Intersection }

            return Triple(roads, intersections, gateways)
        }

        fun nagelSchreckenbergRoad(e: RoadEntity): NagelRoad {
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
            RoadNodeType.GATEWAY -> createRoadNode(node, roadIdMap)
        }

        private fun createIntersection(node: RoadNodeEntity, roadIdMap: Map<Long, NagelRoad>): Intersection {
            requireNotNull(node.turnDirections) { "Invalid RoadNodeEntity id=${node.id}, for type intersection expected not null turnDirections" }


        }

        private fun createRoadNode(node: RoadNodeEntity, roadIdMap: Map<Long, NagelRoad>): Gateway {


        }

        private fun createTurnDirectionFrom(node: TurnDirectionEntity, roadIdMap: Map<Long, NagelRoad>): NagelIntersectionTurningLaneDirection {
            val from = roadIdMap[node.sourceLaneId]
            val to = roadIdMap[node.destinationRoadId]
            requireNotNull(from)

            return NagelIntersectionTurningLaneDirection(
                from = from,
                to = to
            )
        }
    }
}

