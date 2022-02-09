package frc.robot.utils;

public class ActuatorMap {
    public static final int lJoyStick = 1;
    public static final int rJoyStick = 2;
    public static final int controller = 0;

    //CAN Devices (PCM should default to 0, so hopefully we don't have to change it)
    public static final int lMaster = 0;
    public static final int rMaster = 2;
    public static final int lSlave = 1;
    public static final int rSlave = 3;

    public static final int shooter = 6;
    public static final int intake = 26;
    public static final int indexer = 26;
    public static final int climber = 1;
    
    //Digital Inputs/Outputs (DIO)
    public static final int beamBreak = 26;
    public static final int leftLed = 26;
    public static final int rightLed = 26;

    //Pnematics Control Module (PCM) 
    public static final int dog = 26;
}
