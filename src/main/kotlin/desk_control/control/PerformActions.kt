package desk_control.control

import desk_control.data.JoyStickControls
import desk_control.data.JoyStickControls.*
import desk_control.data.MouseData
import desk_control.data.PadControls
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object PerformActions {
    private val robot = Robot()

    fun performMovementAction(joyStickControls: JoyStickControls) {
        handleLeftJoystick(joyStickControls)
    }

    fun performGamePadAction(padControls: PadControls) {
        handleGamePad(padControls)
    }

    fun performMouseTrackAction(mouseData: MouseData) {
        handleRightJotStick(mouseData)
    }

    fun performShiftAction(boolean: Boolean) {
        handleShift(boolean)
    }

    private var previousLeftJoystickAction: Int? = null

    /**
     * Takes the responsibility of handling the actions of the left joystick. This includes the character movement
     * controls.
     */
    private fun handleLeftJoystick(joyStickControls: JoyStickControls) {
        val currentKey = when (joyStickControls) {
            STICK_UP -> KeyEvent.VK_UP
            STICK_LEFT -> KeyEvent.VK_LEFT
            STICK_DOWN -> KeyEvent.VK_DOWN
            STICK_RIGHT -> KeyEvent.VK_RIGHT
            RELEASE -> null
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
    private fun handleRightJotStick(mouseData: MouseData) {
        // TODO: use angle and strength to move mouse
        robot.mouseMove(mouseData.mouseAngle * 5, mouseData.mouseStrength * 10)
    }

    /**
     * handles right game-pad buttons.
     */
    private fun handleGamePad(padControls: PadControls) {
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
    private fun handleShift(value: Boolean) {
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