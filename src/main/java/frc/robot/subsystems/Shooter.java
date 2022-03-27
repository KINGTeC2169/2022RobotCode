package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;
import frc.robot.utils.PID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;

public class Shooter extends SubsystemBase {
    TalonFX shooter = new TalonFX(ActuatorMap.shooter);

    double currentPower;
    double intstagrill;
    double previousPower = 0.0;
    double previousError = 0.0;
    PID rpmLoop = new PID(.0004, .0008, .000005);
    

    public Shooter() {
        shooter.configOpenloopRamp(.5);
        shooter.configClosedloopRamp(0);
        shooter.configVelocityMeasurementPeriod(SensorVelocityMeasPeriod.Period_100Ms);
    }

    /**Sets shooter motor to 'power' value */
    public void shoot(double power) {
        CompressorTank.disable();
        shooter.set(ControlMode.PercentOutput, power);
    }

    /**Calculates RPM value of flywheel */
    public double getRPM() {
        return (600 * shooter.getSelectedSensorVelocity() / Constants.TalonFXCPR)  * (24.0/18.0);
    }

    /**Original PID loop for RPM setting, don't use this unless there is an unknown error with RPM setting. 
     * It's basically the same thing but worse */
    public void setCoolerRPM(double rpm) {
        CompressorTank.disable();
        double error = rpm - getRPM(); // Error = Target - Actual
        //double power = previousPower + (error* .00000125);
        double power = previousPower + (error* .0000014);
       shooter.set(ControlMode.PercentOutput, power);
       previousPower = power;
       previousError = error;
       if(previousPower > 1){
        previousPower = 1;
      }
      else if (previousPower < -1){
        previousPower = -1;
      }
    }

    /**Good PID loop for RPM setting. This is the one we actually use */
    public void setCoolerestRPM(double rpm) {
        CompressorTank.disable();
        rpmLoop.setSetpoint(rpm);
        rpmLoop.calculate(getRPM());
        shooter.set(ControlMode.PercentOutput, rpmLoop.getOutput());
    }

    public void stopShooter() {
        shooter.set(ControlMode.PercentOutput, 0);
    }
    public boolean isShootingLeft() {
        return shooter.getMotorOutputPercent() > 0;
    }
    public boolean isShootingRight() {
        return shooter.getMotorOutputPercent() < 0;
    }
    public double getCurrent() {
        return shooter.getSupplyCurrent();
    }
    public double getVoltage() {
        return shooter.getMotorOutputVoltage();
    }
}
