package main;

public class ClientTestClass {
    public static void main(String[] args) {
        // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
        try {
            int smallInterval = 200;
//            Thread.sleep(5000);
            PerformActions.INSTANCE.handleRightJotStick(1,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
