import main.DeskControl.Control
import main.DeskControl.JoyStickControls
import main.DeskControl.PadControls
import main.DeskControl.PerformActions.performAction

fun main(args: Array<String>) {
    // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
    try {
        performAction(
            Control(
                mouseStrength = 0,
                mouseAngle = 0,
                gamePad = PadControls.CIRCLE,
                playerMovement = JoyStickControls.STICK_LEFT,
                shift = false
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}