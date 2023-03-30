package services.control

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import services.control.data.PadMapping
import java.awt.Robot
import java.awt.event.KeyEvent

object PerformActions {
    private val robot = Robot()
    private var previousLeftJoystickAction: MutableList<Int> = mutableListOf()
    var game = PadMapping.control

    private val input = INPUT()

    private var x = 0f
    private var y = 0f

    init {
        input.type = WinDef.DWORD(INPUT.INPUT_MOUSE.toLong())
        input.input.setType("mi")
        input.input.mi.dwFlags = WinDef.DWORD(0x0001L)
        input.input.mi.time = WinDef.DWORD(0)
        initiateTest()
        initiateMouseTracker()
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

        previousLeftJoystickAction = currentKey
    }

    /**
     * this includes the mouse movement using strength and angle as input.
     */
    fun performMouseTrackAction(mouseData: MouseData) {
        x = mouseData.mouseMovementX.toFloat()
        y = mouseData.mouseMovementY.toFloat()
        counter++
    }


    private fun initiateMouseTracker() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                x *= 0.5f
                y *= 0.5f
                Thread.sleep(2)
                input.input.mi.apply {
                    dx = WinDef.LONG(x.toLong())
                    dy = WinDef.LONG(y.toLong())
                }
                User32.INSTANCE.SendInput(WinDef.DWORD(1L), arrayOf(input), input.size())
            }
        }
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
            PadControls.TOP_PRESSED -> robot.keyPress(game.leftPadTop)
            PadControls.TOP_RELEASED -> robot.keyRelease(game.leftPadTop)

            PadControls.BOTTOM_PRESSED -> robot.keyPress(game.leftPadBottom)
            PadControls.BOTTOM_RELEASED -> robot.keyRelease(game.leftPadBottom)

            PadControls.CENTER_PRESSED -> robot.keyPress(game.leftPadCenter)
            PadControls.CENTER_RELEASED -> robot.keyRelease(game.leftPadCenter)

            PadControls.LEFT_PRESSED -> robot.mousePress(game.leftPadLeft)
            PadControls.LEFT_RELEASED -> robot.mouseRelease(game.leftPadLeft)

            PadControls.RIGHT_PRESSED -> robot.keyPress(game.leftPadRight)
            PadControls.RIGHT_RELEASED -> robot.keyRelease(game.leftPadRight)
        }
    }

    /**
     * handles right game-pad buttons.
     */
    fun performRightGamePadAction(padControls: PadControls) {
        when (padControls) {
            PadControls.TOP_PRESSED -> robot.keyPress(game.rightPadTop)
            PadControls.TOP_RELEASED -> robot.keyRelease(game.rightPadTop)

            PadControls.BOTTOM_PRESSED -> robot.keyPress(game.rightPadBottom)
            PadControls.BOTTOM_RELEASED -> robot.keyRelease(game.rightPadBottom)

            PadControls.CENTER_PRESSED -> robot.mousePress(game.rightPadCenter)
            PadControls.CENTER_RELEASED -> robot.mouseRelease(game.rightPadCenter)

            PadControls.LEFT_PRESSED -> robot.keyPress(game.rightPadLeft)
            PadControls.LEFT_RELEASED -> robot.keyRelease(game.rightPadLeft)

            PadControls.RIGHT_PRESSED -> robot.keyPress(game.rightPadRight)
            PadControls.RIGHT_RELEASED -> robot.keyRelease(game.rightPadRight)
        }
    }
}