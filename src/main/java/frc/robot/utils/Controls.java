package frc.robot.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.ActuatorMap;

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

    public static double getLeftStickY() {
        return leftJoy.getY();
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

    public static double getLeftTwist() {
        return leftJoy.getTwist();
    }

    public static double getRightTwist() {
        return rightJoy.getTwist();
    }

    public static boolean getLeftTop() {
        return leftJoy.getRawButton(0);
    }

    public static boolean getLeftBottom() {
        return leftJoy.getRawButton(1);
    }

    public static boolean getRightTop() {
        return rightJoy.getRawButton(0);
    }

    public static boolean getRightBottom() {
        return rightJoy.getRawButton(1);
    }
}
