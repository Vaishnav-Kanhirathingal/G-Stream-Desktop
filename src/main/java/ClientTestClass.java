import main.Control;
import main.JoyStickControls;
import main.PadControls;
import main.PerformActions;

public class ClientTestClass {
    public static void main(String[] args) {
        // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
        try {
            int smallInterval = 200;
            Thread.sleep(3000);
            PerformActions
                    .INSTANCE
                    .performAction(
                            new Control(
                                    0,
                                    0,
                                    PadControls.CIRCLE,
                                    JoyStickControls.STICK_LEFT,
                                    false
                            )
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}