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
    
    /**Runs indexer forward based on input boolean*/
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

    /**Runs indexer backwards based on input boolean */
    public void reverseSuckUp(boolean stimulating) {
        if(stimulating)
            indexer.set(ControlMode.PercentOutput, Constants.indexSpeed);
        else
            indexer.set(ControlMode.PercentOutput, 0);
    }

    /**Pushes feeder up for certain amount of time*/
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

    /**Returns true if feeder is up */
    public boolean isShoveBallRunning() {
        return isShoveBallRunning;
    }

    /**Return true if indexer is running */
    public boolean isSuckingUp() {
        return isSuckingUp;
    }

    /**Extends feeder */
    public void up() {
        indexerPiston.set(true);
    }

    /**Retracts feeder */
    public void down() {
        indexerPiston.set(false);
    }
    

}
