package pl.edu.agh.cs.kraksim.common

import org.mapstruct.BeforeMapping
import org.mapstruct.MappingTarget
import org.mapstruct.TargetType
import java.util.*

class CycleAvoidingMappingContext {
    private val knownInstances: MutableMap<Any, Any> = IdentityHashMap()

    @Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")
    @BeforeMapping
    fun <T> getMappedInstance(source: Any?, @TargetType targetType: Class<T>?): T? {
        return knownInstances[source] as T?
    }

    @BeforeMapping
    fun storeMappedInstance(source: Any, @MappingTarget target: Any) {
        knownInstances[source] = target
    }
}
