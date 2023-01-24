package connection

class ConnectionData(
    val serverIpAddress: String,

    val videoPort: Int,
    val audioPort: Int,

    val leftGamePadPort: Int,
    val leftJoyStickPort: Int,
    val rightGamePadPort: Int,
    val rightJoyStickPort: Int,

//    val shiftPort: Int,
)