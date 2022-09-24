package desk_control.control

import desk_control.data.Control
import desk_control.data.JoyStickControls
import desk_control.data.JoyStickControls.*
import desk_control.data.MouseData
import desk_control.data.PadControls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object PerformActions {
    private val robot = Robot()
    private var shiftPressed = false

    init {
        initiateLeftHandler()
    }

    fun performAction(control: Control) {
        // TODO: use the received data to perform actions on the device.
        handleLeftJoystick(control.playerMovement)
        handleRightJotStick(control.mouseData)
        handleGamePad(control.gamePad)
        handleShift(control.shift)
    }

    var leftKeyPressed: Int? = null
    private fun initiateLeftHandler() {
        CoroutineScope(Dispatchers.IO).launch {
            var prev: Int? = null
            while (true) {
                Thread.sleep(2)
                println("value = $prev, $leftKeyPressed")
                prev?.let {
                    robot.keyRelease(it)
                }
                leftKeyPressed?.let {
                    robot.keyPress(it)
                }
                prev = leftKeyPressed
            }
        }
    }

    /**
     * Takes the responsibility of handling the actions of the left joystick. This includes the character movement
     * controls.
     */
    private fun handleLeftJoystick(joyStickControls: JoyStickControls) {
        leftKeyPressed = when (joyStickControls) {
            STICK_UP -> KeyEvent.VK_W
            STICK_LEFT -> KeyEvent.VK_A
            STICK_DOWN -> KeyEvent.VK_S
            STICK_RIGHT -> KeyEvent.VK_D
            RELEASE -> null
        }
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    private fun handleRightJotStick(mouseData: MouseData) {
        // TODO: use angle and strength to move mouse
        robot.mouseMove(960, 540)
    }

    /**
     * handles right game-pad buttons.
     */
    private fun handleGamePad(padControls: PadControls) {
        when (padControls) {
            // TODO: perform necessary key presses with releases
            PadControls.TRIANGLE -> robot.apply { keyPress(KeyEvent.VK_Q); keyRelease(KeyEvent.VK_Q) }
            PadControls.SQUARE -> robot.apply { keyPress(KeyEvent.VK_E); keyRelease(KeyEvent.VK_E) }
            PadControls.CIRCLE -> robot.apply { mousePress(InputEvent.BUTTON3_DOWN_MASK);mouseRelease(InputEvent.BUTTON3_DOWN_MASK) }
            PadControls.CROSS -> robot.apply { mousePress(InputEvent.BUTTON1_DOWN_MASK);mouseRelease(InputEvent.BUTTON1_DOWN_MASK) }
            PadControls.RELEASE -> {}// TODO: do nothing
        }
    }

    /**
     * handles long key presses for shift
     */
    private fun handleShift(shift: Boolean) {
        if (shiftPressed != shift) {
            if (shift) robot.keyPress(KeyEvent.VK_SHIFT)
            else robot.keyRelease(KeyEvent.VK_SHIFT)
            shiftPressed = shift
        }
    }
}