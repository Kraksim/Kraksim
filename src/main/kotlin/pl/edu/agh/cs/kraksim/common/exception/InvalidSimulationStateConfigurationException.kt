package pl.edu.agh.cs.kraksim.common.exception

class InvalidSimulationStateConfigurationException(val exceptions: List<String>) :
    Exception(exceptions.joinToString(separator = ", \n"))
