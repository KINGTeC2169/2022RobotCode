package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class DriveTrain extends SubsystemBase {
    TalonSRX rMaster = new TalonSRX(ActuatorMap.rMaster);
    TalonSRX lMaster = new TalonSRX(ActuatorMap.lMaster);

    VictorSPX rSlave = new VictorSPX(ActuatorMap.rSlave);
    VictorSPX lSlave = new VictorSPX(ActuatorMap.lSlave); 

    Solenoid dog = new Solenoid(PneumaticsModuleType.CTREPCM, ActuatorMap.dog);


    public DriveTrain() {
        rMaster.configOpenloopRamp(0);
        lMaster.configOpenloopRamp(0);
        rSlave.configOpenloopRamp(0);
        lSlave.configOpenloopRamp(0);
        
        rSlave.follow(rMaster);
        rMaster.setInverted(true);
        rSlave.setInverted(true);
        lSlave.follow(lMaster);
    }

    public void rampOn() {
        rMaster.configOpenloopRamp(0.1);
        lMaster.configOpenloopRamp(0.1);
        rSlave.configOpenloopRamp(0.1);
        lSlave.configOpenloopRamp(0.1);
    }

    public void rampOff() {
        rMaster.configOpenloopRamp(0);
        lMaster.configOpenloopRamp(0);
        rSlave.configOpenloopRamp(0);
        lSlave.configOpenloopRamp(0);
    }

    /**Sets power to right drive motors */
    public void rDrive(double power) {
        rMaster.set(ControlMode.PercentOutput, power);
    }

    /**Sets power to left drive motors */
    public void lDrive(double power) {
        lMaster.set(ControlMode.PercentOutput, power);
    }

    /**Sets all drive motors to off */
    public void stop() {
        rMaster.set(ControlMode.PercentOutput, 0);
        lMaster.set(ControlMode.PercentOutput, 0);
    }

    /**Hee hee hoo hoo, dog shifting... */
    public void shiftThatDog() {
        dog.toggle();
    }

    /**Sets shifter to low gear */
    public void downShift() {
        dog.set(true);
    }
    /**Sets encoder values for drivetrain to 0 */
    public void setZero() {
        lMaster.setSelectedSensorPosition(0);
        rMaster.setSelectedSensorPosition(0);
    }

    //This is stuff for currently unfinished attempt at shooting while moving
    public double getRPM() {
        return ((600 * rMaster.getSelectedSensorVelocity() / Constants.TalonFXCPR) + (600 * lMaster.getSelectedSensorVelocity() / Constants.TalonFXCPR)) / 2;
    }

    /**Returns speed of robot in inches/second */
    public double getSpeed() {
        return (getRPM() / 60) * Constants.wheelCirc;
    }
    
    public double getAngle(double distance, double duration) {
        return Math.atan((getSpeed() * duration) / distance);
    }

    

    //This is all just info for shuffleboard
    public boolean dogStatus() {
        return dog.get();
    }
    public double getLeftPos() {
        return lMaster.getSelectedSensorPosition();
    }
    public double getRightPos() {
        return rMaster.getSelectedSensorPosition();
    }
    public double leftPercent() {
        return lMaster.getMotorOutputPercent();
    }
    public double rightPercent() {
        return rMaster.getMotorOutputPercent();
    }
    public double rightVoltage() {
        return rMaster.getMotorOutputVoltage();
    }
    public double leftVoltage() {
        return lMaster.getMotorOutputVoltage();
    }
}
