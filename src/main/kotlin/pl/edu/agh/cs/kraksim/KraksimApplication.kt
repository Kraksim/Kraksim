package pl.edu.agh.cs.kraksim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KraksimApplication

fun main(args: Array<String>) {
    runApplication<KraksimApplication>(*args)
}
