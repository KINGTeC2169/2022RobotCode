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
    DoubleSolenoid intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 2);
    private static boolean isUp;

    public void suckUp(boolean isSucking) {
        if(isSucking)
            indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
    }
    //I don't anything about pneumatics still :)

    public void moveIntake() {
        if(isUp) {
            intakePiston.set(Value.kForward);
            isUp = false;
        }
        else {
            intakePiston.set(Value.kReverse);
            isUp = true;
        }
    }
    //I now know a *little* about pneumatics- as a treat
}
