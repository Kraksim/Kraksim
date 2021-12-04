package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.simulation.domain.MapType
import pl.edu.agh.cs.kraksim.simulation.domain.RoadNodeType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class CreateMapRequest(
    val type: MapType,
    @field:Valid
    val roadNodes: List<CreateRoadNodeRequest>,
    @field:Valid
    val roads: List<CreateRoadRequest>,
    @field:Valid
    @field:NotEmpty
    val compatibleWith: List<MovementSimulationStrategyType>,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val description: String,
)

class CreateRoadNodeRequest(
    val type: RoadNodeType,
    val position: CreatePositionRequest,
    val endingRoadsIds: List<Long>,
    val startingRoadsIds: List<Long>,
    val turnDirections: List<CreateTurnDirectionRequest>?,
    val overrideTurnDirectionsTurnEverywhere: Boolean = false,
    @field:NotBlank
    val name: String,
)

class CreateRoadRequest(
    val length: Int,
    @field:NotEmpty
    val lanes: List<CreateLaneRequest>,
    val id: Long = 0,
    @field:NotBlank
    val name: String,
)

class CreateLaneRequest(
    val startingPoint: Int,
    val endingPoint: Int,
    val indexFromLeft: Int,
    val id: Long = 0
)

class CreateTurnDirectionRequest(
    val sourceLaneId: Long,
    val destinationRoadId: Long
)

data class CreatePositionRequest(val x: Double, val y: Double)
