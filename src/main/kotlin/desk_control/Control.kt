package desk_control

data class Control(
    var mouseData: MouseData,
    var gamePad: PadControls,
    var playerMovement: JoyStickControls,
    var shift: Boolean
)

enum class JoyStickControls {
    STICK_RIGHT, STICK_UP, STICK_LEFT, STICK_DOWN, RELEASE
}

enum class PadControls {
    TRIANGLE, SQUARE, CIRCLE, CROSS, RELEASE
}

data class MouseData(
    var mouseStrength: Int,
    var mouseAngle: Int,
)