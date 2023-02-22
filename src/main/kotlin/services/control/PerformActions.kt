package services.control

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.INPUT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.control.data.JoyStickControls
import services.control.data.JoyStickControls.*
import services.control.data.MouseData
import services.control.data.PadControls
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

object PerformActions {
    private val robot = Robot()
    private var previousLeftJoystickAction: MutableList<Int> = mutableListOf()


    private val input = INPUT()

    init {
        input.type = WinDef.DWORD(INPUT.INPUT_MOUSE.toLong())
        input.input.setType("mi")
        input.input.mi.dwFlags = WinDef.DWORD(0x0001L)
        input.input.mi.time = WinDef.DWORD(0)
        initiateTest()
    }

    /**
     * Takes the responsibility of handling the actions of the left joystick. This includes the character movement
     * controls.
     */
    fun performMovementAction(joyStickControls: JoyStickControls) {
        val currentKey: MutableList<Int> = when (joyStickControls) {
            STICK_UP -> mutableListOf(KeyEvent.VK_W)
            STICK_LEFT -> mutableListOf(KeyEvent.VK_A)
            STICK_DOWN -> mutableListOf(KeyEvent.VK_S)
            STICK_RIGHT -> mutableListOf(KeyEvent.VK_D)
            STICK_UP_RIGHT -> mutableListOf(KeyEvent.VK_W, KeyEvent.VK_D)
            STICK_UP_LEFT -> mutableListOf(KeyEvent.VK_W, KeyEvent.VK_A)
            STICK_DOWN_RIGHT -> mutableListOf(KeyEvent.VK_S, KeyEvent.VK_D)
            STICK_DOWN_LEFT -> mutableListOf(KeyEvent.VK_S, KeyEvent.VK_A)
            RELEASE -> mutableListOf()
        }
        for (i in previousLeftJoystickAction) robot.keyRelease(i)
        for (i in currentKey) robot.keyPress(i)


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
    fun performMouseTrackAction(mouseData: MouseData, frames: Int = 2) {
        counter++
        repeat(frames) {
            val i = it + 1
            Thread.sleep(2)
            input.input.mi.apply {
                dx = WinDef.LONG(((mouseData.mouseMovementX * i) / frames).toLong())
                dy = WinDef.LONG(((mouseData.mouseMovementY * i) / frames).toLong())
            }
            User32.INSTANCE.SendInput(WinDef.DWORD(1L), arrayOf(input), input.size())
        }

//        input.input.mi.dx = WinDef.LONG(mouseData.mouseMovementX.toLong())
//        input.input.mi.dy = WinDef.LONG(mouseData.mouseMovementY.toLong())
//        User32.INSTANCE.SendInput(WinDef.DWORD(1L), arrayOf(input), input.size())
    }

    private var counter = 0
    private fun initiateTest() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Thread.sleep(1000)
                println("counter = $counter")
                counter = 0
            }
        }
    }

    fun performLeftGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TOP -> robot.apply { keyPress(KeyEvent.VK_SPACE);keyRelease(KeyEvent.VK_SPACE) }
            PadControls.BOTTOM -> robot.apply { keyPress(KeyEvent.VK_C);keyRelease(KeyEvent.VK_C) }
            PadControls.CENTER -> robot.apply { performShiftAction() }
            PadControls.LEFT -> robot.apply { mousePress(KeyEvent.BUTTON3_DOWN_MASK);mouseRelease(KeyEvent.BUTTON3_DOWN_MASK) }
            PadControls.RIGHT -> robot.apply { keyPress(KeyEvent.VK_M);keyRelease(KeyEvent.VK_M) }
        }
    }

    /**
     * handles right game-pad buttons.
     */
    fun performRightGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TOP -> robot.apply { keyPress(KeyEvent.VK_TAB);keyRelease(KeyEvent.VK_TAB) }
            PadControls.BOTTOM -> robot.apply { keyPress(KeyEvent.VK_F);keyRelease(KeyEvent.VK_F) }
            PadControls.CENTER -> robot.apply { mousePress(InputEvent.BUTTON1_DOWN_MASK);mouseRelease(InputEvent.BUTTON1_DOWN_MASK) }
            PadControls.LEFT -> robot.apply { keyPress(KeyEvent.VK_Q);keyRelease(KeyEvent.VK_Q) }
            PadControls.RIGHT -> robot.apply { keyPress(KeyEvent.VK_E);keyRelease(KeyEvent.VK_E) }
        }
    }

    private var pressed = false

    /**
     * handles long key presses for shift
     */
    private fun performShiftAction() {
        if (pressed) {
            robot.keyRelease(KeyEvent.VK_SHIFT)
        } else {
            robot.keyPress(KeyEvent.VK_SHIFT)
        }
        pressed = !pressed
    }
}