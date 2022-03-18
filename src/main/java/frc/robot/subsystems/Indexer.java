package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class Indexer extends SubsystemBase {
    VictorSPX indexer = new VictorSPX(ActuatorMap.indexer);
    Solenoid indexerPiston = new Solenoid(PneumaticsModuleType.CTREPCM , ActuatorMap.feederPiston);
    boolean isSuckingUp = false;

    Timer timer = new Timer();
    boolean isShoveBallRunning = false;
    
    public void suckUp(boolean stimulating) {
        if(stimulating) {
            indexer.set(ControlMode.PercentOutput, -Constants.indexSpeed);
            isSuckingUp = true;
        }
        else {
            indexer.set(ControlMode.PercentOutput, 0);
            isSuckingUp = false;
        }
    }

    public void reverseSuckUp(boolean stimulating) {
        if(stimulating)
            indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
        else
            indexer.set(ControlMode.PercentOutput, 0);
    }
    //TODO: Fix
    public void shoveBall() {
        if(timer.get() == 0) {
            timer.start();
        }
        if(timer.get() < Constants.shoveBallTime) {
            indexerPiston.set(true);
            isShoveBallRunning = true;

        } else {
            indexerPiston.set(false);
            isShoveBallRunning = false;
            timer.stop();
            timer.reset();
        }




    }
    public boolean isShoveBallRunning() {
        return isShoveBallRunning;
    }
    public boolean isSuckingUp() {
        return isSuckingUp;
    }

    public void up() {
        indexerPiston.set(true);
    }
    public void down() {
        indexerPiston.set(false);
    }
    

}
