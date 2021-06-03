package pl.edu.agh.cs.kraksim.comon.random

interface RandomProvider {
    fun getBoolean(trueProbability: Double): Boolean
}