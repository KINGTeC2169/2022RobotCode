package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Intake extends SubsystemBase {
    VictorSPX intake = new VictorSPX(ActuatorMap.intake);
    DoubleSolenoid intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, ActuatorMap.intakePistonOne, ActuatorMap.intakePistonTwo);
    
    /**Sets intake to suck in */
    public void suck(boolean slurp) {
        if(slurp) 
            intake.set(ControlMode.PercentOutput, Constants.intakeSpeed);   
        else
            intake.set(ControlMode.PercentOutput, 0);
    }

    /**Sets intake to outtake */
    public void reverseSuck(boolean slurp) {
        if(slurp) 
            intake.set(ControlMode.PercentOutput, -Constants.intakeSpeed);   
        else
            intake.set(ControlMode.PercentOutput, 0);
    }

    /**Moves the intake down */
    public void down() {
        intakePiston.set(Value.kForward);
    }
    /**Pulls the intake up */
    public void up() {
        intakePiston.set(Value.kReverse);
    }
    /**Sets intake solenoid to off position */
    public void off() {
        intakePiston.set(Value.kOff);
    }
}
