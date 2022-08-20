package main

import java.awt.Robot
import java.awt.event.KeyEvent

object PerformActions {
    val r = Robot()
    fun performAction(control: Control) {
        // TODO: use the recieved data to perform actions on the device.
        r.keyPress(KeyEvent.VK_WINDOWS)
        r.keyRelease(KeyEvent.VK_WINDOWS);
    }
}