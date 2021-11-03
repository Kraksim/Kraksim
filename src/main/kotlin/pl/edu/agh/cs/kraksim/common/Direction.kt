package pl.edu.agh.cs.kraksim.common

import kotlin.math.sign

enum class Direction(
    val delta: Int
) {
    LEFT(-1),
    STAY(0),
    RIGHT(1);

    companion object {
        fun basedOnTurn(turn: Long): Direction {
            return if (turn % 2 == 0L) RIGHT else LEFT
        }

        fun fromDistanceToTargetLane(distance: Int): Direction {
            return values().find { it.delta == distance.sign } ?: throw IllegalStateException("Sign to returns either -1, 0, 1, so this exception can't happen")
        }
    }
}
