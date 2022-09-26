package connection

// TODO: check and optimize for necessary data
class ConnectionData(
    val serverIpAddress: String,

    val streamPort: Int,

    val movementPort: Int,
    val gamePadPort: Int,
    val mouseTrackPort: Int,
    val shiftPort: Int,

    val horizontalResolution: Int,
    val verticalResolution: Int,
    val frameRateCap: Int,
)