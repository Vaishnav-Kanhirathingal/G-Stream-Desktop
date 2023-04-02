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

    val gameName:String
) {
    companion object {
        //sample mapping object
        val deathStranding = PadMapping(
            leftPadBottom = VK_C,
            leftPadRight = VK_TAB,

            rightPadTop = VK_F,
            rightPadBottom = VK_ALT,
            rightPadLeft = VK_Q,
            rightPadRight = VK_E,
            gameName = "Death Stranding"
        )
        val control = PadMapping(
            leftPadBottom = VK_C,
            leftPadRight = VK_TAB,

            rightPadTop = VK_F,
            rightPadBottom = VK_CONTROL,
            rightPadLeft = VK_Q,
            rightPadRight = VK_E,
            gameName = "Control"
        )
        val defaultGameList = mutableListOf(deathStranding, control)
    }
}