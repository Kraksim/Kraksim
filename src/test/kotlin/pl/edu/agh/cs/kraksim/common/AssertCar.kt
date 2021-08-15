package pl.edu.agh.cs.kraksim.common

import org.assertj.core.api.Assertions
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.gps.GPSType

fun Car.assertVelocity(velocity: Velocity): Car {
    Assertions.assertThat(this.velocity).isEqualTo(velocity)
    return this
}

fun Car.assertPositionRelativeToStart(positionRelativeToStart: Int): Car {
    Assertions.assertThat(this.positionRelativeToStart).isEqualTo(positionRelativeToStart)
    return this
}

fun Car.assertGpsRoute(list: List<Road>): Car {
    Assertions.assertThat(this.gps.route).isEqualTo(list)
    return this
}

fun Car.assertGpsRouteIsEmpty(): Car {
    Assertions.assertThat(this.gps.route).isEmpty()
    return this
}

fun Car.assertGpsType(gpsType: GPSType): Car {
    Assertions.assertThat(this.gps.type).isEqualTo(gpsType)
    return this
}
