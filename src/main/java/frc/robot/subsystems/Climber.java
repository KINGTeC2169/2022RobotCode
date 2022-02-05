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
    DoubleSolenoid climberAdjusterTwo = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 6, 7);

    public void init() {
        climberAdjuster.set(Value.kForward);
        climberAdjusterTwo.set(Value.kForward);
    }

    public void extendArm() {
        climber.set(ControlMode.PercentOutput, 0.5);
    }

    public void retractArm() {
        climber.set(ControlMode.PercentOutput, -0.5);
    }
    
    public void movePiston() {
        //Apparently we have to 'wiggle' the pistons somehow
        climberAdjuster.toggle();
        climberAdjusterTwo.toggle();
        }
    }
