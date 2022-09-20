package desk_control

import desk_control.JoyStickControls.*
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object PerformActions {
    private val robotControl = Robot()
    private var shiftPressed = false

    fun performAction(control: Control) {
        // TODO: use the received data to perform actions on the device.
        handleLeftJoystick(control.playerMovement)
        handleRightJotStick(control.mouseData)
        handleGamePad(control.gamePad)
        handleShift(control.shift)
    }

    private var previousArrowKey: Int? = null

    /**
     * Takes the responsibility of handling the actions of the left joystick. This includes the character movement
     * controls.
     */
    private fun handleLeftJoystick(joyStickControls: JoyStickControls) {
        println("key press called ${joyStickControls.name}")
        previousArrowKey?.let {
            println("key release initiated")
            robotControl.keyRelease(it)
        }
        val currentKey: Int? =
            when (joyStickControls) {
                STICK_UP -> KeyEvent.VK_W
                STICK_LEFT -> KeyEvent.VK_A
                STICK_DOWN -> KeyEvent.VK_S
                STICK_RIGHT -> KeyEvent.VK_D
                RELEASE -> null
            }
        currentKey?.let { println("key pressed - $it");robotControl.keyPress(it) }
        previousArrowKey = currentKey
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    private fun handleRightJotStick(mouseData: MouseData) {
        // TODO: use angle and strength to move mouse
        robotControl.mouseMove(960, 540)
    }

    /**
     * handles right game-pad buttons.
     */
    private fun handleGamePad(padControls: PadControls) {
        when (padControls) {
            // TODO: perform necessary key presses with releases
            PadControls.TRIANGLE -> robotControl.keyPress(KeyEvent.VK_Q)
            PadControls.SQUARE -> robotControl.keyPress(KeyEvent.VK_E)
            PadControls.CIRCLE -> {
                robotControl.mousePress(InputEvent.BUTTON3_DOWN_MASK)
                robotControl.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
            }

            PadControls.CROSS -> {
                robotControl.mousePress(InputEvent.BUTTON1_DOWN_MASK)
                robotControl.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
            }

            PadControls.RELEASE -> {
                // TODO: do nothing
            }
        }
    }

    /**
     * handles long key presses for shift
     */
    private fun handleShift(shift: Boolean) {
        if (!shiftPressed && shift) {
            // TODO: press shift
            println("shift pressed")
        } else if (shiftPressed && !shift) {
            // TODO: release shift
            println("shift released")
        }
        shiftPressed = shift
    }
}