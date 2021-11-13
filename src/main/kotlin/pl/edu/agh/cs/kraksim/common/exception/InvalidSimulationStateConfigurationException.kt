package pl.edu.agh.cs.kraksim.common.exception

class InvalidSimulationStateConfigurationException(val exceptions: List<Exception>) :
    Exception(exceptions.mapExceptionsToMessage())

private fun List<Exception>.mapExceptionsToMessage() =
    map { it.message }.joinToString(separator = ", ")