package desk_control

data class Control(
    var mouseStrength: Int,
    var mouseAngle: Int,
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