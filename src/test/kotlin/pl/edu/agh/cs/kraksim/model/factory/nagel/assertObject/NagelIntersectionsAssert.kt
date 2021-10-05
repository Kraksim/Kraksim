package pl.edu.agh.cs.kraksim.model.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelIntersection
import pl.edu.agh.cs.kraksim.repository.entities.TurnDirectionEntity

class NagelIntersectionsAssert(
    private val intersections: List<NagelIntersection>
) {
    fun assertTurningDirections(intersectionId: Long, directions: List<TurnDirectionEntity>): NagelIntersectionsAssert {
        val intersection = intersections.find { it.id == intersectionId }
        directions.forEach { directionEntity ->
            assertThat(
                intersection?.directions?.find { direction ->
                    directionEntity.sourceLane.id == direction.from &&
                        directionEntity.destinationRoad.id == direction.to
                }
            ).isNotNull
        }
        return this
    }
}
