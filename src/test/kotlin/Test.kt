import services.control.PerformActions
import services.control.data.MouseData
import java.awt.Robot

val robot = Robot()

fun main() {
    for (i in 0..100) {
        PerformActions.performMouseTrackAction(MouseData(mouseMovementX = 10, mouseMovementY = 10))
    }
}


class NewTest {
    private var arr = mutableListOf<Int>()
    private var index = 0

    fun peek(): Int {
        return arr[index]
    }

    fun push(element: Int) {
        arr.add(element)
        index++
    }

    fun pop() {
        arr.removeAt(index)
        index--
    }
}