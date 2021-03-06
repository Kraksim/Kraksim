package pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoadNode
import pl.edu.agh.cs.kraksim.simulation.domain.RoadNodeEntity

class NagelRoadNodeAssert(
    private val roadNodes: List<NagelRoadNode>
) {
    fun assertEndingAndStartingRoads(roadNodeEntity: RoadNodeEntity) {
        val roadNode = roadNodes.find { it.id == roadNodeEntity.id }
        checkRoads(roadNode!!.endingRoads.keys.toList(), roadNodeEntity.endingRoads.map { it.id })
        checkRoads(roadNode.startingRoads.keys.toList(), roadNodeEntity.startingRoads.map { it.id })
    }

    private fun checkRoads(ids: Collection<Long>, entityIds: Collection<Long>) {
        assertThat(ids.size).isEqualTo(entityIds.size)
        if (ids.isNotEmpty()) {
            ids.forEach { id -> assertThat(id).isIn(entityIds) }
        }
    }
}
