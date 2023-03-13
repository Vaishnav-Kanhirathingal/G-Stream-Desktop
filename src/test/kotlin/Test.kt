import com.google.gson.Gson
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.control.data.MouseData
import java.awt.MouseInfo
import java.awt.Robot

const val testMouseListJson =
    "[\"{\\\"1\\\":-28,\\\"2\\\":13}\",\"{\\\"1\\\":-26,\\\"2\\\":13}\",\"{\\\"1\\\":-23,\\\"2\\\":14}\",\"{\\\"1\\\":-22,\\\"2\\\":14}\",\"{\\\"1\\\":-18,\\\"2\\\":12}\",\"{\\\"1\\\":-15,\\\"2\\\":14}\",\"{\\\"1\\\":-12,\\\"2\\\":12}\",\"{\\\"1\\\":-14,\\\"2\\\":18}\",\"{\\\"1\\\":-13,\\\"2\\\":22}\",\"{\\\"1\\\":-3,\\\"2\\\":12}\",\"{\\\"1\\\":-7,\\\"2\\\":22}\",\"{\\\"1\\\":-4,\\\"2\\\":16}\",\"{\\\"1\\\":0,\\\"2\\\":17}\",\"{\\\"1\\\":0,\\\"2\\\":13}\",\"{\\\"1\\\":0,\\\"2\\\":15}\",\"{\\\"1\\\":2,\\\"2\\\":12}\",\"{\\\"1\\\":7,\\\"2\\\":16}\",\"{\\\"1\\\":9,\\\"2\\\":17}\",\"{\\\"1\\\":4,\\\"2\\\":10}\",\"{\\\"1\\\":8,\\\"2\\\":13}\",\"{\\\"1\\\":14,\\\"2\\\":15}\",\"{\\\"1\\\":19,\\\"2\\\":16}\",\"{\\\"1\\\":22,\\\"2\\\":18}\",\"{\\\"1\\\":25,\\\"2\\\":17}\",\"{\\\"1\\\":28,\\\"2\\\":19}\",\"{\\\"1\\\":20,\\\"2\\\":15}\",\"{\\\"1\\\":25,\\\"2\\\":17}\",\"{\\\"1\\\":33,\\\"2\\\":20}\",\"{\\\"1\\\":20,\\\"2\\\":8}\",\"{\\\"1\\\":24,\\\"2\\\":8}\",\"{\\\"1\\\":25,\\\"2\\\":6}\",\"{\\\"1\\\":33,\\\"2\\\":7}\",\"{\\\"1\\\":44,\\\"2\\\":11}\",\"{\\\"1\\\":56,\\\"2\\\":16}\",\"{\\\"1\\\":53,\\\"2\\\":13}\",\"{\\\"1\\\":77,\\\"2\\\":10}\",\"{\\\"1\\\":41,\\\"2\\\":1}\",\"{\\\"1\\\":52,\\\"2\\\":-1}\",\"{\\\"1\\\":31,\\\"2\\\":-9}\",\"{\\\"1\\\":34,\\\"2\\\":-10}\",\"{\\\"1\\\":26,\\\"2\\\":-6}\",\"{\\\"1\\\":26,\\\"2\\\":-13}\",\"{\\\"1\\\":22,\\\"2\\\":-15}\",\"{\\\"1\\\":32,\\\"2\\\":-31}\",\"{\\\"1\\\":60,\\\"2\\\":-77}\",\"{\\\"1\\\":28,\\\"2\\\":-42}\",\"{\\\"1\\\":7,\\\"2\\\":-17}\",\"{\\\"1\\\":6,\\\"2\\\":-23}\",\"{\\\"1\\\":0,\\\"2\\\":-36}\",\"{\\\"1\\\":-13,\\\"2\\\":-39}\",\"{\\\"1\\\":-14,\\\"2\\\":-31}\",\"{\\\"1\\\":-19,\\\"2\\\":-27}\",\"{\\\"1\\\":-22,\\\"2\\\":-34}\",\"{\\\"1\\\":-24,\\\"2\\\":-27}\",\"{\\\"1\\\":-28,\\\"2\\\":-21}\",\"{\\\"1\\\":-84,\\\"2\\\":-52}\",\"{\\\"1\\\":-53,\\\"2\\\":-24}\",\"{\\\"1\\\":-44,\\\"2\\\":-17}\",\"{\\\"1\\\":-46,\\\"2\\\":-12}\",\"{\\\"1\\\":-36,\\\"2\\\":-8}\",\"{\\\"1\\\":-32,\\\"2\\\":-7}\",\"{\\\"1\\\":-25,\\\"2\\\":-3}\",\"{\\\"1\\\":-35,\\\"2\\\":-1}\",\"{\\\"1\\\":-46,\\\"2\\\":0}\",\"{\\\"1\\\":-25,\\\"2\\\":0}\",\"{\\\"1\\\":-33,\\\"2\\\":0}\",\"{\\\"1\\\":-29,\\\"2\\\":1}\",\"{\\\"1\\\":-47,\\\"2\\\":7}\",\"{\\\"1\\\":-28,\\\"2\\\":7}\",\"{\\\"1\\\":-18,\\\"2\\\":4}\",\"{\\\"1\\\":-17,\\\"2\\\":5}\",\"{\\\"1\\\":-14,\\\"2\\\":8}\",\"{\\\"1\\\":-21,\\\"2\\\":11}\",\"{\\\"1\\\":-28,\\\"2\\\":17}\",\"{\\\"1\\\":-27,\\\"2\\\":14}\",\"{\\\"1\\\":-21,\\\"2\\\":11}\",\"{\\\"1\\\":-17,\\\"2\\\":10}\",\"{\\\"1\\\":-15,\\\"2\\\":12}\",\"{\\\"1\\\":-15,\\\"2\\\":12}\",\"{\\\"1\\\":-7,\\\"2\\\":8}\",\"{\\\"1\\\":-8,\\\"2\\\":7}\",\"{\\\"1\\\":-12,\\\"2\\\":15}\",\"{\\\"1\\\":-6,\\\"2\\\":9}\",\"{\\\"1\\\":-11,\\\"2\\\":16}\",\"{\\\"1\\\":-6,\\\"2\\\":12}\",\"{\\\"1\\\":-3,\\\"2\\\":14}\",\"{\\\"1\\\":-4,\\\"2\\\":23}\",\"{\\\"1\\\":0,\\\"2\\\":21}\",\"{\\\"1\\\":3,\\\"2\\\":28}\"]"

class TestClass {
    private val input = WinUser.INPUT()

    init {
        input.type = WinDef.DWORD(WinUser.INPUT.INPUT_MOUSE.toLong())
        input.input.setType("mi")
        input.input.mi.dwFlags = WinDef.DWORD(0x0001L)
        input.input.mi.time = WinDef.DWORD(0)
    }

    private var x = 0f
    private var y = 0f

    suspend fun initiateLoopedTracker() {
        val t = System.currentTimeMillis()
        while (System.currentTimeMillis() < t + 10000) {
            Thread.sleep(2)
            x *= 0.65f
            y *= 0.65f
            setMouse(x, y)
        }
    }

    fun setXY(x: Int, y: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    fun setMouse(x: Float, y: Float) {
        input.input.mi.apply {
            dx = WinDef.LONG(x.toLong())
            dy = WinDef.LONG(y.toLong())
        }
        User32.INSTANCE.SendInput(WinDef.DWORD(1L), arrayOf(input), input.size())

    }
}

fun main() {
//    val x = getMouseInputs()
//    checkMouseInputs()
    performTesting(fromJson(testMouseListJson))
}


fun getMouseInputs(): MutableList<MouseData> {
    val mouseDataList = mutableListOf<MouseData>()
    var prev = MouseData(0, 0)

    val t = System.currentTimeMillis()
    while (System.currentTimeMillis() < t + 3000) {
        Thread.sleep(30)
        val current = MouseData(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y)
        mouseDataList.add(
            MouseData(

                current.mouseMovementX - prev.mouseMovementX,
                current.mouseMovementY - prev.mouseMovementY
            )
        )
        prev = current
    }
    mouseDataList.removeAt(0)
    println(toJson(mouseDataList))
    return mouseDataList
}


fun performTesting(mouseDataList: MutableList<MouseData>) {
    val test = TestClass()

    CoroutineScope(Dispatchers.IO).launch { test.initiateLoopedTracker() }

    val robot = Robot()
    robot.mouseMove(360, 300)
    for (i in mouseDataList) {
        Thread.sleep(30)
        test.setMouse(i.mouseMovementX.toFloat(), i.mouseMovementY.toFloat())
    }
    Thread.sleep(3000)
    robot.mouseMove(360, 300)
    for (i in mouseDataList) {
        Thread.sleep(30)
        test.setXY(i.mouseMovementX, i.mouseMovementY)
    }
}


fun toJson(list: MutableList<MouseData>): String {
    val temp1 = mutableListOf<String>()
    for (i in list) {
        temp1.add(Gson().toJson(i))
    }
    return Gson().toJson(temp1)
}

fun fromJson(str: String): MutableList<MouseData> {
    val temp1 = Gson().fromJson(str, mutableListOf<String>()::class.java)
    val returnable = mutableListOf<MouseData>()
    for (i in temp1) {
        returnable.add(Gson().fromJson(i, MouseData::class.java))
    }
    return returnable
}