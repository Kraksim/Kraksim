package pl.edu.agh.cs.kraksim.comon.random

interface RandomProvider {
    fun getBool(trueProbability: Double): Boolean
}