package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;

public class Shooter extends SubsystemBase {
    TalonFX shooter = new TalonFX(ActuatorMap.shooter);
    boolean hitRPM;
    double currentPower;
    double previousPower = 0.0;
    double previousError = 0.0;
    

    public Shooter() {
        //TODO: how fast for ramp up
        //shooter.configOpenloopRamp(.5);
        shooter.configClosedloopRamp(2.5);
        //shooter.configClosedLoopPeriod(P, 3);
        shooter.configVelocityMeasurementPeriod(SensorVelocityMeasPeriod.Period_100Ms);
        //shooter.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true,5,0,0));
    }

    public void shoot(double power) {
        CompressorTank.disable();
        shooter.set(ControlMode.PercentOutput, power);
    }

    public double getRPM() {
        return (600 * shooter.getSelectedSensorVelocity() / Constants.TalonFXCPR) * (24.0/18.0);
    }

    public void setRPM(double rpm) {
        boolean isASnake = false;
        if(rpm < 0) {
            rpm *= -1;
            isASnake = true;
        }
        if(Math.abs(getRPM()) >= rpm) {
            shooter.set(ControlMode.PercentOutput, 0);
            hitRPM = true;
        } else {
            shooter.set(ControlMode.PercentOutput, isASnake ? -1 : 1);
            hitRPM = false;
        }
    }

    public void setCoolRPM(double rpm) {
        CompressorTank.disable();
        double power = 0;
        double offBy = rpm - getRPM();
        power += (offBy / rpm) * 10;
        if(rpm > 0) {
            shooter.set(ControlMode.PercentOutput, power);
        }
        else if(rpm < 0) {
            shooter.set(ControlMode.PercentOutput, -power);
        }
        
    }

    public void setCoolerRPM(double rpm) {
        CompressorTank.disable();
        double error = rpm - getRPM(); // Error = Target - Actual
        double power = previousPower + (error*.000022);
        power += (error - previousError) / 2;
        
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

    public boolean hitRPM() {
        return hitRPM;

    }

    public void stopShooter() {
        shooter.set(ControlMode.PercentOutput, 0);
    }
    public double getCurrent() {
        return shooter.getSupplyCurrent();
    }
}
