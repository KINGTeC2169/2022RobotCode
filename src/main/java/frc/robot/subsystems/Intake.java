package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Intake extends SubsystemBase {
    TalonSRX intake = new TalonSRX(ActuatorMap.intake);
    
    public void suck(boolean isSucking) {
        if(isSucking)
            intake.set(ControlMode.PercentOutput, Constants.intakeSpeed);    
        //I don't know how fast intake should be, but it will only ever need one speed. Either it is sucking or it isn't.    
    }

    //We will also need something for pneumatics that control intake up and down, idk how to do that
}
