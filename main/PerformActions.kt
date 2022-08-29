package main

import main.JoyStickControls.*
import java.awt.Robot
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
     * Takes the responsibility of handling the actions of the left joystick
     */
    fun handleLeftJoystick(joyStickControls: JoyStickControls) {
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
            previousArrowKey = currentKey
        } else {
            // TODO: check if release works
        }
    }

    fun handleRightJotStick(mouseAngle: Int, mouseStrength: Int) {
        // TODO: use angle and strength to move mouse
        robotControl.mouseMove(2000, 2000)
    }

    fun handleGamePad(padControls: PadControls) {
        when (padControls) {
            // TODO: perform necessary key presses with releases
            PadControls.TRIANGLE -> {}
            PadControls.SQUARE -> {}
            PadControls.CIRCLE -> {}
            PadControls.CROSS -> {}
            PadControls.RELEASE -> {}
        }
    }

    fun handleShift(shift: Boolean) {
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