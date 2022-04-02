package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Climber extends SubsystemBase {
    TalonFX climber = new TalonFX(ActuatorMap.climber);
    DoubleSolenoid climberAdjuster = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, ActuatorMap.climberPistonOne, ActuatorMap.climberPistonTwo);
    Solenoid ratchet = new Solenoid(PneumaticsModuleType.CTREPCM, ActuatorMap.winch);
    
    public Climber() {
        climber.configOpenloopRamp(0);
    }

    /**Extends arms at a speed of 1 */
    public void extendArm() {
        climber.set(ControlMode.PercentOutput, -1);
    }

    /**Extends arms at a speed of 0.4 */
    public void extendArmSlow() {
        climber.set(ControlMode.PercentOutput, -0.4);
    }

    /**Turns off arms */
    public void stopArm() {
        climber.set(ControlMode.PercentOutput, 0);
    }

    /**Retracts arms at a speed of 1 */
    public void retractArm() {
        climber.set(ControlMode.PercentOutput, 1);
    }

    /**Retracts arms at a speed of 0.4 */
    public void retractArmSlow() {
        climber.set(ControlMode.PercentOutput, 0.4);
    }

    
    /**Extends climber piston */
    public void movePistonUp() {
        climberAdjuster.set(Value.kForward);
    }
    /**Retracts climber piston */
    public void movePistonDown() {
        climberAdjuster.set(Value.kReverse);
    }
    /**Sets climber solenoid to off position */
    public void pistonOff() {
        climberAdjuster.set(Value.kOff);
    }

    
    /**Returns value of climber encoder */
    public double getSensorPos() {
        return climber.getSelectedSensorPosition();
    }
    /**Returns true if climber has reached top of encoder limit */
    public boolean isTop() {
        return climber.getSelectedSensorPosition() <= Constants.climberLimit;
    }
    /**Returns true if climber has reached bottom of encoder limit */
    public boolean isBottom() {
        if(getCurrent() > 30) {
            System.out.println("Holy shit I'm climbing");
            return climber.getSelectedSensorPosition() >= 0;
        }
        else
            return climber.getSelectedSensorPosition() >= -25000;
    }
    /**Returns true if climber is exerting more current than limit */
    public boolean isBottomCurrent() {
        return getCurrent() > Constants.climberCurrent;
    }
    /**Toggles the climbing ratchet */
    public void toggLock() {
        ratchet.toggle();
    }

    public boolean lockStatus() {
        return ratchet.get();
    }

    /**Sets climber encoder value to 0 */
    public void setZero() {
        climber.setSelectedSensorPosition(0);
    }


    //These are just for shuffleboard
    public double getCurrent() {
        return climber.getSupplyCurrent();
    }
    public boolean ratchetStatus() {
        return ratchet.get();
    }

}
