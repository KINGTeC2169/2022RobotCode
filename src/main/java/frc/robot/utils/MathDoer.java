package frc.robot.utils;

public class MathDoer {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }

    public static double turnTicks(double degrees) {
        double turnDistanceCirc = Constants.drivetrainCircumference * (degrees / 360);
        return turnDistanceCirc / (6*Math.PI) * Constants.TalonSRXCPR;
    }
}
