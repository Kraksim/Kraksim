package pl.edu.agh.cs.kraksim.nagelCore

class TrafficLightPhase(
    var phaseTime: Int,
    var state: LightColor
) {


    enum class LightColor {
        RED, YELLOW, GREEN
    }
}
