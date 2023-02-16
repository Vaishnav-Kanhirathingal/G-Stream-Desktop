import services.control.PerformActions
import services.control.data.MouseData
import java.awt.Robot

val robot = Robot()

fun main() {
    for (i in 0..100) {
        PerformActions.performMouseTrackAction(MouseData(mouseMovementX = 10, mouseMovementY = 10))
    }
}