package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class BeamBreak extends SubsystemBase {

    private DigitalInput beamBreak = new DigitalInput(ActuatorMap.beamBreak);
    //private AnalogInput balls = new AnalogInput(ActuatorMap.beamBreak);

    public boolean isBall() {
        return beamBreak.get();
    }

    //public double isBall2() {
        //return balls.getVoltage();
    //}

    
}
