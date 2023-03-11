package services.control.data

import java.awt.event.KeyEvent.*

data class PadMapping(
    val leftPadTop: Int= VK_SPACE,
    val leftPadBottom: Int,
    val leftPadCenter: Int= VK_SHIFT,
    val leftPadLeft: Int = BUTTON3_DOWN_MASK,
    val leftPadRight: Int,

    val rightPadTop: Int,
    val rightPadBottom: Int,
    val rightPadCenter: Int = BUTTON1_DOWN_MASK,
    val rightPadLeft: Int,
    val rightPadRight: Int,
) {
    companion object {
        //sample mapping object
        val deathStranding = PadMapping(
            leftPadBottom = VK_C,
            leftPadRight = VK_M,

            rightPadTop = VK_TAB,
            rightPadBottom = VK_F,
            rightPadLeft = VK_Q,
            rightPadRight = VK_E,
        )
        val control = PadMapping(
            leftPadBottom = 0,
            leftPadRight = 0,

            rightPadTop = 0,
            rightPadBottom = 0,
            rightPadLeft = 0,
            rightPadRight = 0,
        )
    }
}