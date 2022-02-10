package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Indexer extends SubsystemBase {
    TalonSRX indexer = new TalonSRX(ActuatorMap.indexer);
    Solenoid indexerPiston = new Solenoid(PneumaticsModuleType.CTREPCM , ActuatorMap.feederPiston);
    Timer timer = new Timer();
    double saveTime;
    
    public void suckUp(boolean stimulating) {
        if(stimulating)
            indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
        else
            indexer.set(ControlMode.PercentOutput, 0);
    }

    public void shoveBall() {
        timer.start();
        if(timer.get() - saveTime > Constants.shoveBallTime) {
            indexerPiston.set(true);
            saveTime = timer.get();
        } else {
            indexerPiston.set(false);
            timer.stop();
            timer.reset();
        }


    }
    public void down() {
        indexerPiston.set(false);
    }
    

}
