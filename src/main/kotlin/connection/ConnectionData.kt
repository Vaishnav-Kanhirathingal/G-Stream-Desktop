package connection

// TODO: check and optimize for necessary data
class ConnectionData(
    val serverIpAddress: String,
    val controlPort: Int,
    val streamPort: Int,

    val horizontalResolution: Int,
    val verticalResolution: Int,
    val frameRateCap: Int,
)