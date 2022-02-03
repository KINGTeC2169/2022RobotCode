package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class Climber extends SubsystemBase {
    TalonFX climber = new TalonFX(ActuatorMap.climber);
    DoubleSolenoid climberAdjuster = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 5);
    DoubleSolenoid climberAdjuster2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 6, 7);
    private static boolean isExtended = true;

    public void extendArm() {
        climber.set(ControlMode.PercentOutput, 0.5);
    }

    public void retractArm() {
        climber.set(ControlMode.PercentOutput, -0.5);
    }
    
    public void movePiston() {
        if(isExtended) {
            climberAdjuster.set(Value.kReverse);
            climberAdjuster2.set(Value.kReverse);
            isExtended = false;
        }
        else {
            climberAdjuster.set(Value.kForward);
            climberAdjuster2.set(Value.kForward);
            isExtended = true;
        }
    }
}
