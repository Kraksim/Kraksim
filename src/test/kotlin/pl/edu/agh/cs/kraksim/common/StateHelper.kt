package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelLane
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelRoad
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelSimulationState

fun NagelSimulationState.getLane(roadId: Long = 0, lane: Int = 0): NagelLane = (road(roadId) as NagelRoad).lanes[lane]

fun SimulationState.road(id: Long) =
    roads[id] ?: throw NullPointerException("Road with id=$id doesnt exist in $roads")

fun SimulationState.gateway(id: Long) =
    gateways[id] ?: throw NullPointerException("Gateway with id=$id doesnt exist in $gateways")

fun SimulationState.intersection(id: Long) = intersections[id]
    ?: throw NullPointerException("Intersection with id=$id doesnt exist in $intersections")
