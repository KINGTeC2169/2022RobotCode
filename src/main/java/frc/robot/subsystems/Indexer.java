package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Indexer extends SubsystemBase {
    TalonSRX indexer = new TalonSRX(ActuatorMap.indexer);

    public void suckUp(boolean isSucking) {
        if(isSucking)
            indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
    }
    //I don't anything about pneunatics still :)
}
