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
    private static boolean isExtended = false;
    //TODO: Make sure all pneumatics start how they should. DO NOT TEST PNEUNMATICS UNTIL THIS IS CHECKED.
    
    public void suck(boolean isSucking) {
        if(isSucking)
            intake.set(ControlMode.PercentOutput, Constants.intakeSpeed);     
    }

    public void moveIntake() {
        if(isExtended) {
            intakePiston.set(Value.kForward);
            isExtended = false;
        }
        else {
            intakePiston.set(Value.kReverse);
            isExtended = true;
        }
    }
}
