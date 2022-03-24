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

    boolean turnIsDone;

    public DriveTrain() {
        rMaster.configOpenloopRamp(0);
        lMaster.configOpenloopRamp(0);
        rSlave.configOpenloopRamp(0);
        lSlave.configOpenloopRamp(0);
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

    //Drive for the right gearbox
    public void rDrive(double power) {
        //set slaves to follow
        rSlave.follow(rMaster);


        //set right motors to be inverted
        rMaster.setInverted(true);
        rSlave.setInverted(true);
        rMaster.set(ControlMode.PercentOutput, power);
    }

    //Drive for the left gearbox
    public void lDrive(double power) {
        //set slaves to follow
        lSlave.follow(lMaster);


        //set slaves to be inverted
        //lSlave.setInverted(true);

        lMaster.set(ControlMode.PercentOutput, power);
    }

    public void turn(double angle, double currentAngle) {
        if(currentAngle < angle) {
            turnIsDone = false;
            rDrive(0.5);
            lDrive(-0.5);
        }
        else {
            turnIsDone = true;
            rDrive(0);
            lDrive(0);
        }
    }

    public boolean turnisDone() {
        return turnIsDone;
    } 

    public void shiftThatDog() {
        dog.toggle();
    }

    public boolean dogStatus() {
        return dog.get();
    }

    public double getRPM() {
        return ((600 * rMaster.getSelectedSensorVelocity() / Constants.TalonFXCPR) + (600 * lMaster.getSelectedSensorVelocity() / Constants.TalonFXCPR)) / 2;
    }

    public double getSpeed() {
        return (getRPM() / 60) * Constants.wheelCirc;
    }
    
    public double getAngle(double distance, double duration) {
        return Math.atan((getSpeed() * duration) / distance);
    }

    public void setZero() {
        lMaster.setSelectedSensorPosition(0);
        rMaster.setSelectedSensorPosition(0);
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
