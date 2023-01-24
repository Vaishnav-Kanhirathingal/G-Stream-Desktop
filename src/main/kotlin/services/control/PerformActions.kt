package services.control

import services.control.data.JoyStickControls
import services.control.data.JoyStickControls.*
import services.control.data.MouseData
import services.control.data.PadControls
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
        if (previousLeftJoystickAction != currentKey && pressed) {
            robot.keyRelease(KeyEvent.VK_SHIFT)
            pressed = false
        }
        previousLeftJoystickAction = currentKey
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    fun performMouseTrackAction(mouseData: MouseData) {
        val angle = Math.toRadians(mouseData.mouseAngle.toDouble())
        val strength = mouseData.mouseStrength.toDouble() * 0.4
        animateToPosition(
            dx = (cos(angle) * strength).toInt(), dy = (-sin(angle) * strength).toInt()
        )
    }

    private fun animateToPosition(
        dx: Int, dy: Int, frames: Int = 2
    ) {
        val x = MouseInfo.getPointerInfo().location.x
        val y = MouseInfo.getPointerInfo().location.y

        repeat(frames) {
            val i = it + 1
            Thread.sleep(6)
            robot.mouseMove((x + ((dx * i) / frames)), (y + ((dy * i) / frames)))
        }
    }

    /**
     * handles right game-pad buttons.
     */
    fun performRightGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TOP -> robot.apply { keyPress(KeyEvent.VK_TAB);keyRelease(KeyEvent.VK_TAB) }
            PadControls.BOTTOM -> robot.apply {}
            PadControls.LEFT -> robot.apply { keyPress(KeyEvent.VK_Q);keyRelease(KeyEvent.VK_Q) }
            PadControls.RIGHT -> robot.apply { keyPress(KeyEvent.VK_E);keyRelease(KeyEvent.VK_E) }
            PadControls.CENTER -> robot.apply { mousePress(InputEvent.BUTTON1_DOWN_MASK);mouseRelease(InputEvent.BUTTON1_DOWN_MASK) }
        }
    }

    fun performLeftGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TOP -> robot.apply { performShiftAction() }
            PadControls.BOTTOM -> robot.apply { keyPress(KeyEvent.VK_C);keyRelease(KeyEvent.VK_C) }
            PadControls.LEFT -> robot.apply {}
            PadControls.RIGHT -> robot.apply {}
            PadControls.CENTER -> robot.apply { mousePress(InputEvent.BUTTON3_DOWN_MASK);mouseRelease(InputEvent.BUTTON3_DOWN_MASK) }
        }
    }

    private var pressed = false

    /**
     * handles long key presses for shift
     */
    private fun performShiftAction() {
        pressed = if (pressed) {
            robot.keyRelease(KeyEvent.VK_SHIFT)
            false
        } else {
            robot.keyPress(KeyEvent.VK_SHIFT)
            true
        }

    }

}