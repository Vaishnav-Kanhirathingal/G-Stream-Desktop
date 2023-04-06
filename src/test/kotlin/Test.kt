import com.google.gson.GsonBuilder
import services.control.data.PadMapping
import java.awt.Desktop
import java.net.URI

fun main(){
    testBrowser()
}

fun testBrowser() {
    try {
        val uri = URI("http://google.com/")
        val dt = Desktop.getDesktop()
        dt.browse(uri)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun testJson() {
    println(
        GsonBuilder().setPrettyPrinting().create().toJson(
            PadMapping(
                leftPadTop = 2,
                leftPadBottom = 2,
                leftPadCenter = 2,
                leftPadLeft = 2,
                leftPadRight = 2,
                rightPadTop = 2,
                rightPadBottom = 2,
                rightPadCenter = 1,
                rightPadLeft = 1,
                rightPadRight = 1,
                gameName = "Valorant"
            )
        )
    )
}