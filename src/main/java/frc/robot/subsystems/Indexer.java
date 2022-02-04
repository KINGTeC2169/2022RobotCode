package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Indexer extends SubsystemBase {
    TalonSRX indexer = new TalonSRX(ActuatorMap.indexer);
    DoubleSolenoid indexerPiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
    //TODO: Change forward and reverse channels for all pneumatics
    
    public void suckUp() {
        indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
    }

    public void shoveBall() {
        indexerPiston.set(Value.kForward);
    }
    

}
