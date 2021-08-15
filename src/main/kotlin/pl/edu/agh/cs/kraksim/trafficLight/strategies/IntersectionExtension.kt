package pl.edu.agh.cs.kraksim.trafficLight.strategies

import pl.edu.agh.cs.kraksim.core.state.Intersection

fun Intersection.getLightPhasesOfLanesGroupedByRoad() =
    endingRoads.values.map { road -> lightPhasesOf(road) }

fun Intersection.modifyPhases(modifier: Int) =
    getLightPhasesOfLanesGroupedByRoad()
        .onEach { lanes -> lanes.forEach { it.phaseTime += modifier } }
