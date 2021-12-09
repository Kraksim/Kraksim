package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType

class MapDTO(
    val type: MapType,
    val roadNodes: List<RoadNodeDTO>,
    val roads: List<RoadDTO>,
    val name: String,
    var description: String,
    val compatibleWith: List<MovementSimulationStrategyType>,
    val id: Long = 0
)

class BasicMapInfoDTO(
    val type: MapType,
    val name: String,
    val description: String,
    val id: Long = 0,
    val compatibleWith: List<MovementSimulationStrategyType>,
    val nodes: List<BasicRoadNodeDto>,
    val edges: List<BasicEdgeDto>
)

class BasicEdgeDto(
    val from: Long,
    val to: Long,
    val roadThickness: Int,
    val roadName: String,
)

class BasicRoadNodeDto(
    val name: String,
    val type: RoadNodeType,
    val position: PositionDTO,
    val id: Long = 0
)

class RoadNodeDTO(
    val type: RoadNodeType,
    val position: PositionDTO,
    val endingRoads: List<RoadDTO>,
    val startingRoads: List<RoadDTO>,
    val turnDirections: List<TurnDirectionDTO>,
    val name: String,
    val id: Long = 0
)

class RoadDTO(
    val length: Int,
    val lanes: List<LaneDTO>,
    val name: String,
    val id: Long = 0
)

class LaneDTO(
    val startingPoint: Int,
    val endingPoint: Int,
    val indexFromLeft: Int,
    val name: String,
    val id: Long = 0
)

class TurnDirectionDTO(
    val sourceLane: LaneDTO,
    val destinationRoad: RoadDTO,
    val id: Long = 0
)

data class PositionDTO(val x: Double, val y: Double)
