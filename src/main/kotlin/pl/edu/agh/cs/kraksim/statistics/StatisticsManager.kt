package pl.edu.agh.cs.kraksim.statistics

import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.core.state.SimulationState

class StatisticsManager(
    var states: List<StateStatistics> = ArrayList(),
    var expectedVelocity: Map<RoadId, Velocity> = HashMap()
) {

    private fun createTotalStatisticsValues(currentStatisticsValues: StatisticsValues): StatisticsValues{

        val statsList = states.map { it.currentStatisticsValues } + listOf<StatisticsValues>(currentStatisticsValues)

        val wholeMapAverageSpeed = statsList.map { it.speedStatistics.wholeMapAverageSpeed.value }.average()

        val roadAverageSpeed = statsList
                .flatMap { it.speedStatistics.roadAverageSpeed.asIterable() }
                .groupBy{ it.key }
                .map { (key, value) -> key to AverageSpeed(value.map{ it.value.value }.average())}
                .toMap()

        val roadDensity = statsList
                .flatMap { it.density.asIterable() }
                .groupBy { it.key }
                .map { (key, value) -> key to Density(value.map { it.value.value }.average()) }
                .toMap()

        val roadFlowRatio = statsList
                .flatMap { it.roadFlowRatio.asIterable() }
                .groupBy { it.key }
                .map { (key, value) -> key to FlowRatio(value.map{ it.value.value }.average()) }
                .toMap()

        return StatisticsValues(
                SpeedStatistics(roadAverageSpeed = roadAverageSpeed, wholeMapAverageSpeed = AverageSpeed(wholeMapAverageSpeed)),
                roadDensity,
                roadFlowRatio
        )
    }

    fun createStatistics(state: SimulationState): StateStatistics {
        val roadsSpeed = getRoadsSpeed(state)
        val roadData = getRoadData(state)

        val speedStatistics = speedStatistics(roadsSpeed)
        val density = roadData.associate { it.id to Density(it.carsNumber.toDouble() / it.surface) }
        val roadFlowRatio = roadsSpeed.filter { expectedVelocity.containsKey(it.key) }
            .map { (id, carSpeeds) ->
                id to FlowRatio(carSpeeds.map { it.value }.average() / expectedVelocity[id]!!.value)
            }.toMap()


        val currentStatisticsValues = StatisticsValues(
            speedStatistics,
            density,
            roadFlowRatio
        )

        val totalStatisticsValues = createTotalStatisticsValues(currentStatisticsValues)

        return StateStatistics(state.id, state.turn, currentStatisticsValues, totalStatisticsValues)
    }

    private fun getRoadsSpeed(state: SimulationState): Map<RoadId, List<CarSpeed>> {
        val value = HashMap<RoadId, List<CarSpeed>>()
        state.roads.forEach { road ->
            val id = RoadId(road.id)
            val speed = state.cars.map { CarSpeed(it.velocity) }
            value[id] = speed
        }
        return value
    }

    private fun getRoadData(state: SimulationState): List<RoadData> {
        return state.roads.map { road ->
            RoadData(
                id = RoadId(road.id),
                carsNumber = road.lanes.flatMap { lane -> lane.cars }.size,
                surface = road.lanes.sumOf { it.physicalLength }
            )
        }
    }

    private fun speedStatistics(roadsSpeed: Map<RoadId, List<CarSpeed>>): SpeedStatistics {
        val wholeMapAverageSpeed = roadsSpeed.flatMap { it.value }
            .map { speed -> speed.value }
            .average()

        val roadAverageSpeed = roadsSpeed.map { (key, value) ->
            val value1 = value.map { speed -> speed.value }
                .average()
            key to AverageSpeed(value1)
        }.toMap()

        return SpeedStatistics(AverageSpeed(wholeMapAverageSpeed), roadAverageSpeed)
    }
}
