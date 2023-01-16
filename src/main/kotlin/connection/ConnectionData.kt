package connection

class ConnectionData(
    val serverIpAddress: String,

    val videoPort: Int,
    val audioPort: Int,

    val movementPort: Int,
    val gamePadPort: Int,
    val mouseTrackPort: Int,
    val shiftPort: Int,

    val horizontalResolution: Int,
    val verticalResolution: Int,
    val frameRateCap: Int,
)