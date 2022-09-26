import java.awt.Robot

fun main() {
    val robot = Robot()
    for (i in 0..10_000 step 10) {
        Thread.sleep(5)
        robot.mouseMove(i,540)
    }
}