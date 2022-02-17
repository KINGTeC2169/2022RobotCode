package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Shooter extends SubsystemBase {
    TalonFX shooter = new TalonFX(ActuatorMap.shooter);
    boolean hitRPM;
    

    public Shooter() {
        //TODO: how fast for ramp up
        shooter.configClosedloopRamp(5);
    }

    public void shoot(double power) {
        CompressorTank.disable();
        shooter.set(ControlMode.PercentOutput, power);
    }

    public double getRPM() {
        return (600 * shooter.getSelectedSensorVelocity() / Constants.TalonFXCPR) * (24.0/18.0);
    }

    public void setRPM(double rpm) {
        shooter.set(ControlMode.Velocity, (rpm * Constants.TalonFXCPR) / 600.0);
        if(getRPM() >= rpm) {
            hitRPM = true;
        } else {
            hitRPM = false;
        }
    }
    public boolean hitRPM() {
        return hitRPM;

    }
}
