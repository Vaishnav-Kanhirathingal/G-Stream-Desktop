import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinUser.INPUT;

//import static com.sun.jna.Native.sizeof;//compilation error 'sizeof(int)' has private access in 'com.sun.jna.Native'
import static com.sun.jna.platform.win32.WinUser.INPUT.INPUT_MOUSE;

public class Test {

    public static void main(String[] a) {
        moveMouse(
                1_000L,
                1_000L
        );
    }

    /**
     * @param x change required in x co-ordinate
     * @param y change required in y co-ordinate
     *
     */
    public static void moveMouse(Long x, Long y) {
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT_MOUSE);
        input.input.mi.dx = new LONG(x);
        input.input.mi.dy = new LONG(y);
        input.input.mi.time = new DWORD(0);
        input.input.mi.dwFlags = new DWORD(0x0001L);

        INPUT[] inputArray = {input};

        DWORD result = User32.INSTANCE.SendInput(new DWORD(1), inputArray, input.size());

        // sizeof (used below in the commented code) returns the compilation error
        // 'sizeof(int)' has private access in 'com.sun.jna.Native'
        // I got the idea to use sizeof from another StackOverflow post
//        DWORD result = User32.INSTANCE.SendInput(new DWORD(1), inputArray, sizeof(input));
        System.out.println("result = " + result.longValue());
        System.out.println("size = " + input.size());
    }
}