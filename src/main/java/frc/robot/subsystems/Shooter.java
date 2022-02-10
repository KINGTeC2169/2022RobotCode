package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Shooter extends SubsystemBase {
    TalonFX shooter = new TalonFX(ActuatorMap.shooter);

    public Shooter() {
        //TODO: how fast for ramp up
        shooter.configClosedloopRamp(5);
    }

    public void shoot(double power) {
        shooter.set(ControlMode.PercentOutput, power);
    }

    public double getRPM() {
        return 600 * shooter.getSelectedSensorVelocity() / Constants.TalonFXCPR;
    }

    public void setRPM(double rpm) {
        shooter.set(ControlMode.Velocity, (rpm * Constants.TalonFXCPR) / 600);
    }
}
