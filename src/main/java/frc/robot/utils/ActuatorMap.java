package frc.robot.utils;


public class ActuatorMap {
    public static final int lJoyStick = 1;
    public static final int rJoyStick = 2;
    public static final int controller = 0;

    //CAN Devices (PCM should default to 0, so hopefully we don't have to change it)
    public static final int lMaster = 4;
    public static final int rMaster = 9;
    public static final int lSlave = 5;
    public static final int rSlave = 8;

    public static final int shooter = 3;
    public static final int intake = 6;
    public static final int indexer = 7;
    public static final int climber = 2;
    
    //Digital Inputs/Outputs (DIO)
    public static final int beamBreak = 0;
    public static final int leftLed = 7;
    public static final int rightLed = 8;

    //Pneumatics Control Module (PCM) 
    public static final int intakePistonOne = 3;
    public static final int intakePistonTwo = 6;
    public static final int climberPistonOne = 2;
    public static final int climberPistonTwo = 7;
    public static final int winch = 0;
    public static final int dog = 4;
    public static final int feederPiston = 5;
}
