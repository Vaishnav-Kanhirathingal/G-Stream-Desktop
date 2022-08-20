package main;

public class ClientTestClass {
    public static void main(String[] args) {
        // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
        try {
            int smallInterval = 500;
            Thread.sleep(5000);
            PerformActions.INSTANCE.handleLeftJoystick(JoyStickControls.STICK_LEFT);
            Thread.sleep(smallInterval);
            PerformActions.INSTANCE.handleLeftJoystick(JoyStickControls.STICK_RIGHT);
            Thread.sleep(smallInterval);
            PerformActions.INSTANCE.handleLeftJoystick(JoyStickControls.STICK_UP);
            Thread.sleep(smallInterval);
            PerformActions.INSTANCE.handleLeftJoystick(JoyStickControls.STICK_DOWN);
            Thread.sleep(smallInterval);
            PerformActions.INSTANCE.handleLeftJoystick(JoyStickControls.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
