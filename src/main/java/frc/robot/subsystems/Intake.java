package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Intake extends SubsystemBase {
    TalonSRX intake = new TalonSRX(ActuatorMap.intake);
    DoubleSolenoid intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 2);
    private static boolean isUp;
    
    public void suck(boolean isSucking) {
        if(isSucking)
            intake.set(ControlMode.PercentOutput, Constants.intakeSpeed);    
        //I don't know how fast intake should be, but it will only ever need one speed. Either it is sucking or it isn't.    
    }

    public void moveIntake() {
        if(isUp) {
            intakePiston.set(Value.kForward);
            isUp = false;
        }
        else {
            intakePiston.set(Value.kReverse);
            isUp = true;
        }
    }

    //We will also need something for pneumatics that control intake up and down, idk how to do that

    //I now know a *little* about pneumatics- as a treat
}
