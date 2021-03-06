package pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoad
import pl.edu.agh.cs.kraksim.simulation.domain.LaneEntity
import pl.edu.agh.cs.kraksim.simulation.domain.RoadEntity
import pl.edu.agh.cs.kraksim.simulation.domain.RoadNodeEntity

class NagelRoadsAssert(
    private val roads: List<NagelRoad>
) {
    fun assertPhysicalLength(entity: RoadEntity): NagelRoadsAssert {
        val road = roads.find { it.id == entity.id }
        assertThat(road?.physicalLength).isEqualTo(entity.length)
        return this
    }

    fun assertEnd(end: RoadNodeEntity, roadId: Long): NagelRoadsAssert {
        val road = roads.find { it.id == roadId }
        assertThat(road!!.end.id).isEqualTo(end.id)
        assertThat(road.id).isIn(end.endingRoads.map { it.id })
        return this
    }

    fun assertLane(laneEntity: LaneEntity, roadId: Long): NagelRoadsAssert {
        val road = roads.find { it.id == roadId }
        assertThat(road).isNotNull
        val lane = road?.lanes?.find { it.id == laneEntity.id }
        assertThat(lane?.indexFromLeft).isEqualTo(laneEntity.indexFromLeft)
        assertThat(lane?.parentRoad).isEqualTo(road)
        assertThat(lane?.physicalStartingPoint).isEqualTo(laneEntity.startingPoint)
        assertThat(lane?.physicalEndingPoint).isEqualTo(laneEntity.endingPoint)
        assertThat(lane?.cellsCount)
            .isEqualTo((laneEntity.endingPoint - laneEntity.startingPoint) / Car.AVERAGE_CAR_LENGTH)

        return this
    }
}
