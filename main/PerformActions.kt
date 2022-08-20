package main

import main.JoyStickControls.*
import java.awt.Robot
import java.awt.event.KeyEvent

object PerformActions {
    private val r = Robot()
    fun performAction(control: Control) {
        // TODO: use the recieved data to perform actions on the device.
        r.keyPress(KeyEvent.VK_WINDOWS)
        r.keyRelease(KeyEvent.VK_WINDOWS);


        handleLeftJoystick(control.playerMovement)
    }

    private var previousArrowKey: Int? = null

    fun handleLeftJoystick(joyStickControls: JoyStickControls) {
        println("key press called ${joyStickControls.name}")
        if (previousArrowKey != null) {
            println("key release initiated")
            r.keyRelease(previousArrowKey!!)
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
            r.keyPress(currentKey)
            previousArrowKey = currentKey
        }
    }
}