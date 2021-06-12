package pl.edu.agh.cs.kraksim.statistics

import pl.edu.agh.cs.kraksim.core.SimulationState

class StatisticsManager(
    var states: List<StateStatistics> = ArrayList(),
    var expectedVelocity: Map<RoadId, Velocity> = HashMap()
) {

    fun createStatistics(state: SimulationState) {
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
        // todo refactor kodu, lepiej zmienne nazwać itp.
        /* todo dokończyć
         var stats = StateStatistics(
              simulationId = state.id,
              turn = state.turn,

              )*/
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

        return SpeedStatistics(wholeMapAverageSpeed, roadAverageSpeed)
    }
}
