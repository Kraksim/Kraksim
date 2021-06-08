package pl.edu.agh.cs.kraksim.core

interface Lane {
    val id: Long
    val indexFromLeft: Int
    val parentRoad: Road
    val physicalStartingPoint: Int
    val physicalEndingPoint: Int

    val cars: List<Car>

    val physicalLength: Int

    fun addCar(car: Car)
    fun remove(car: Car)
    fun containsCar(): Boolean
    fun getFreeSpaceInFront(): Int
}