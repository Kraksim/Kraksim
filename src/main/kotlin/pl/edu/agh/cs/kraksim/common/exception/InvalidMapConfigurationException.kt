package pl.edu.agh.cs.kraksim.common.exception

class InvalidMapConfigurationException(val exceptions: List<String>) :
    Exception(exceptions.joinToString(separator = ", \n"))
