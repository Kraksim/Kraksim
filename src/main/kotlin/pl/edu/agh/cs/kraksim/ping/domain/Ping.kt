package pl.edu.agh.cs.kraksim.ping.domain

import java.time.LocalDateTime

class Ping(
    val name: String,
    val version: String,
    val time: LocalDateTime,
    val senderIpAddress: String
)
