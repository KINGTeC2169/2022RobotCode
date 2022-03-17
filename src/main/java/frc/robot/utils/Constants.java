package frc.robot.utils;

public final class Constants {
    public static final double turnSensitivity = 1.0;

    //Sensor Units per revolution (Counts per revolution)
    public static final int TalonFXCPR = 2048;
    public static final int TalonSRXCPR = 8192;

    public static final double wheelCirc = 19.163715187;
    
    //Speeds for constant motors
    public static final double intakeSpeed = .5;
    public static final double indexSpeed = .5;

    //Falcon 500 free Current = 1.5A
    //Falcon 500 stall Current = 257A
    public static final double climberLimit = -200000;
    public static final double climberCurrent = 200;

    //Shooter
    public static final double taliban = 1000;
    public static final double shoveBallTime = .5;
}
