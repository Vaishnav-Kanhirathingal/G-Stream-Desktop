package services.control.data

class PadMapping(
    val leftPadLeft: Int,
    val leftPadRight: Int,
    val leftPadTop: Int,
    val leftPadBottom: Int,

    val rightPadLeft: Int,
    val rightPadRight: Int,
    val rightPadTop: Int,
    val rightPadBottom: Int,
) {
    companion object {
        //sample mapping object
        val deathStranding = PadMapping(
            leftPadLeft = 0,
            leftPadRight = 0,
            leftPadTop = 0,
            leftPadBottom = 0,
            rightPadLeft = 0,
            rightPadRight = 0,
            rightPadTop = 0,
            rightPadBottom = 0
        )
    }
}