import JoyStickControls.*
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object PerformActions {
    private val robotControl = Robot()
    private var shiftPressed = false

    fun performAction(control: Control) {
        // TODO: use the received data to perform actions on the device.
        handleLeftJoystick(control.playerMovement)
        handleRightJotStick(control.mouseAngle, control.mouseStrength)
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
        if (previousArrowKey != null) {
            println("key release initiated")
            robotControl.keyRelease(previousArrowKey!!)
            previousArrowKey = null
        }
        val currentKey: Int? =
            when (joyStickControls) {
                STICK_RIGHT -> KeyEvent.VK_RIGHT
                STICK_UP -> KeyEvent.VK_UP
                STICK_LEFT -> KeyEvent.VK_LEFT
                STICK_DOWN -> KeyEvent.VK_DOWN
                RELEASE -> null
            }
        if (currentKey != null) {
            // TODO: keyPress isn't being held
            println("key pressed - $currentKey")
            robotControl.keyPress(currentKey)
        }
        previousArrowKey = currentKey
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    private fun handleRightJotStick(mouseAngle: Int, mouseStrength: Int) {
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