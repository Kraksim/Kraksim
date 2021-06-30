package pl.edu.agh.cs.kraksim.common

fun Double.format(digits: Int) = "%.${digits}f".format(this)
