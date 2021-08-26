package pl.edu.agh.cs.kraksim

import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface AToBConverter {

    @Mapping(source = "fieldA", target = "fieldB")
    fun convertAToB(a: A): B
}

class A(val fieldA: String)

class B(val fieldB: String)