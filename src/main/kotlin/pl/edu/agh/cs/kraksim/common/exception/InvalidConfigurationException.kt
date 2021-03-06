package pl.edu.agh.cs.kraksim.common.exception

open class InvalidConfigurationException(val exceptions: List<String>) :
    Exception(exceptions.joinToString(separator = ", \n"))

class InvalidMapConfigurationException(exceptions: List<String>) : InvalidConfigurationException(exceptions)
