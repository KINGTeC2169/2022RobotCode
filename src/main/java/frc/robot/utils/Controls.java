package frc.robot.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private static XboxController controller = new XboxController(ActuatorMap.controller);
    private static Joystick leftJoy = new Joystick(ActuatorMap.lJoyStick);
    private static Joystick rightJoy = new Joystick(ActuatorMap.rJoyStick);

    public static double getLeftControllerX() {
        return controller.getLeftX();
    }

    public static double getLeftControllerY() {
        return controller.getLeftY();
    }

    public static double getRightControllerX() {
        return controller.getRightX();
    }

    public static double getRightControllerY() {
        return controller.getRightY();
    }

    public static boolean getControllerA() {
        return controller.getAButton();
    }
    public static double getLeftStickY() {
        return -leftJoy.getY();
    }

    public static double getLeftStickX() {
        return leftJoy.getX();
    }

    public static double getRightStickY() {
        return rightJoy.getY();
    }

    public static double getRightStickX() {
        return rightJoy.getX();
    }

    public static double getLeftStickTwist() {
        return leftJoy.getTwist();
    }

    public static double getRightStickTwist() {
        return rightJoy.getTwist();
    }

    public static boolean getLeftStickTop() {
        return leftJoy.getRawButton(1);
    }

    public static boolean getLeftStickBottom() {
        return leftJoy.getRawButton(2);
    }

    public static boolean getRightStickTop() {
        return rightJoy.getRawButton(1);
    }

    public static boolean getRightStickBottom() {
        return rightJoy.getRawButton(2);
    }
}
