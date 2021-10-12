package pl.edu.agh.cs.kraksim.statistics.application

import pl.edu.agh.cs.kraksim.common.CarSpeed
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.statistics.domain.RoadData
import pl.edu.agh.cs.kraksim.statistics.domain.SpeedStatistics
import pl.edu.agh.cs.kraksim.statistics.domain.StateStatistics
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsValues

class StatisticsManager(
    var states: List<StateStatistics> = ArrayList(),
    var expectedVelocity: Map<RoadId, Velocity> = HashMap()
) {

    lateinit var latestState: StateStatistics

    fun createStatistics(state: SimulationState) {
        val roadsSpeed = getRoadsSpeed(state)
        val roadData = getRoadData(state)

        val speedStatistics = speedStatistics(roadsSpeed)
        val density = roadData.associate { it.id to it.carsNumber.toDouble() / it.surface }
        val roadFlowRatio = roadsSpeed.filter { expectedVelocity.containsKey(it.key) }
            .map { (id, carSpeeds) ->
                id to carSpeeds.average() / expectedVelocity[id]!!
            }.toMap()

        val currentStatisticsValues = StatisticsValues(
            speedStatistics,
            density,
            roadFlowRatio
        )

        val totalStatisticsValues = createTotalStatisticsValues(currentStatisticsValues)
        val currentState = StateStatistics(state.id, state.turn, currentStatisticsValues, totalStatisticsValues)
        states += currentState
        latestState = currentState
    }

    private fun getRoadsSpeed(state: SimulationState): Map<RoadId, List<CarSpeed>> {
        return state.roads.mapValues { (_, road) ->
            road.lanes.flatMap { it.cars }.map { it.velocity }
        }
    }

    private fun getRoadData(state: SimulationState): List<RoadData> {
        return state.roads.values.map { road ->
            RoadData(
                id = road.id,
                carsNumber = road.lanes.flatMap { lane -> lane.cars }.size,
                surface = road.lanes.sumOf { it.physicalLength }
            )
        }
    }

    private fun speedStatistics(roadsSpeed: Map<RoadId, List<CarSpeed>>): SpeedStatistics {
        val wholeMapAverageSpeed = roadsSpeed.flatMap { it.value }
            .average()

        val roadAverageSpeed = roadsSpeed.map { (key, value) -> key to value.average() }
            .toMap()

        return SpeedStatistics(wholeMapAverageSpeed, roadAverageSpeed)
    }

    private fun createTotalStatisticsValues(currentStatisticsValues: StatisticsValues): StatisticsValues {

        val statsList = states.map { it.currentStatisticsValues } + listOf(currentStatisticsValues)

        val wholeMapAverageSpeed = statsList.map { it.speedStatistics.wholeMapAverageSpeed }.average()

        val roadAverageSpeed = statsList
            .flatMap { it.speedStatistics.roadAverageSpeed.asIterable() }
            .groupBy { it.key }
            .map { (key, value) -> key to value.map { (_, speed) -> speed }.average() }
            .toMap()

        val roadDensity = statsList
            .flatMap { it.density.asIterable() }
            .groupBy { it.key }
            .map { (key, value) -> key to value.map { (_, density) -> density }.average() }
            .toMap()

        val roadFlowRatio = statsList
            .flatMap { it.roadFlowRatio.asIterable() }
            .groupBy { it.key }
            .map { (key, value) -> key to value.map { (_, flow) -> flow }.average() }
            .toMap()

        return StatisticsValues(
            SpeedStatistics(wholeMapAverageSpeed, roadAverageSpeed),
            roadDensity,
            roadFlowRatio
        )
    }
}
