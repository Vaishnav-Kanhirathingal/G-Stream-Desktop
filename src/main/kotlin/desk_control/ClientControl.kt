package desk_control

import desk_control.PerformActions.performAction

fun main(args: Array<String>) {
    // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
    try {
        performAction(
            Control(
                mouseData = MouseData(0, 0),
                gamePad = PadControls.CIRCLE,
                playerMovement = JoyStickControls.STICK_LEFT,
                shift = false
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}