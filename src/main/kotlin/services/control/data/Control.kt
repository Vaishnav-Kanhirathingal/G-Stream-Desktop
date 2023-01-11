package services.control.data

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