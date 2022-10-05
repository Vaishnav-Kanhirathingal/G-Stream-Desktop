package desk_control.control

import desk_control.data.JoyStickControls
import desk_control.data.JoyStickControls.*
import desk_control.data.MouseData
import desk_control.data.PadControls
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import kotlin.math.cos
import kotlin.math.sin

object PerformActions {
    private val robot = Robot()
    private var previousLeftJoystickAction: Int? = null

    /**
     * Takes the responsibility of handling the actions of the left joystick. This includes the character movement
     * controls.
     */
    fun performMovementAction(joyStickControls: JoyStickControls) {
        val currentKey =
            if (false) {
                when (joyStickControls) {
                    STICK_UP -> KeyEvent.VK_UP
                    STICK_LEFT -> KeyEvent.VK_LEFT
                    STICK_DOWN -> KeyEvent.VK_DOWN
                    STICK_RIGHT -> KeyEvent.VK_RIGHT
                    RELEASE -> null
                }
            } else {
                when (joyStickControls) {
                    STICK_UP -> KeyEvent.VK_W
                    STICK_LEFT -> KeyEvent.VK_A
                    STICK_DOWN -> KeyEvent.VK_S
                    STICK_RIGHT -> KeyEvent.VK_D
                    RELEASE -> null
                }
            }
        previousLeftJoystickAction?.let { robot.keyRelease(it) }
        currentKey?.let { robot.keyPress(it) }

        // this signifies a movement direction change. Hence, the shift key should be released if pressed
        if (previousLeftJoystickAction != currentKey && shiftPressed) {
            robot.keyRelease(KeyEvent.VK_SHIFT)
            shiftPressed = false
        }
        previousLeftJoystickAction = currentKey
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    fun performMouseTrackAction(mouseData: MouseData) {
        animateToPosition(
            dx = (cos(Math.toRadians(mouseData.mouseAngle.toDouble())) * (mouseData.mouseStrength.toDouble() * 0.4)).toInt(),
            dy = (-sin(Math.toRadians(mouseData.mouseAngle.toDouble())) * (mouseData.mouseStrength.toDouble() * 0.4)).toInt()
        )
    }

    private fun animateToPosition(
        dx: Int,
        dy: Int,
        frames: Int = 2
    ) {
        val x = MouseInfo.getPointerInfo().location.x
        val y = MouseInfo.getPointerInfo().location.y

        repeat(frames) {
            val i = it + 1
            Thread.sleep(6)
            robot.mouseMove((x + ((dx * i) / frames)), (y + ((dy * i) / frames)))
        }
//        robot.mouseMove((x + dx).toInt(), (y + dy).toInt())
    }

    /**
     * handles right game-pad buttons.
     */
    fun performGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TRIANGLE -> robot.apply { keyPress(KeyEvent.VK_Q); keyRelease(KeyEvent.VK_Q) }
            PadControls.SQUARE -> robot.apply { keyPress(KeyEvent.VK_E); keyRelease(KeyEvent.VK_E) }
            PadControls.CIRCLE -> robot.apply { mousePress(InputEvent.BUTTON3_DOWN_MASK);mouseRelease(InputEvent.BUTTON3_DOWN_MASK) }
            PadControls.CROSS -> robot.apply { mousePress(InputEvent.BUTTON1_DOWN_MASK);mouseRelease(InputEvent.BUTTON1_DOWN_MASK) }
            PadControls.RELEASE -> {}
        }
    }

    private var prevShiftHandlerValue = false
    private var shiftPressed = false

    /**
     * handles long key presses for shift
     */
    fun performShiftAction(value: Boolean) {
        if (value != prevShiftHandlerValue) { // this means the button was pressed on the android side
            shiftPressed =
                if (shiftPressed) {
                    robot.keyRelease(KeyEvent.VK_SHIFT)
                    false
                } else {
                    robot.keyPress(KeyEvent.VK_SHIFT)
                    true
                }
            prevShiftHandlerValue = value
        }
    }
}