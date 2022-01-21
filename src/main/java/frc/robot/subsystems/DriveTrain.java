package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.ActuatorMap;

public class DriveTrain {
    TalonSRX rMaster = new TalonSRX(ActuatorMap.rMaster);
    TalonSRX lMaster = new TalonSRX(ActuatorMap.lMaster);

    TalonSRX rSlaveOne = new TalonSRX(ActuatorMap.rSlaveOne);
    TalonSRX rSlaveTwo = new TalonSRX(ActuatorMap.rSlaveTwo);
    TalonSRX lSlaveOne = new TalonSRX(ActuatorMap.lSlaveOne);
    TalonSRX lSlaveTwo = new TalonSRX(ActuatorMap.lSlaveTwo);

    //Drive for the right gearbox
    public void rDrive(int power) {
        //set slaves to follow
        rSlaveOne.follow(rMaster);
        rSlaveTwo.follow(rMaster);

        //set slaves to be inverted
        rSlaveOne.setInverted(true);
        rSlaveTwo.setInverted(true);

        rMaster.set(ControlMode.PercentOutput, power);
    }

    //Drive for the left gearbox
    public void lDrive(int power) {
        //set slaves to follow
        lSlaveOne.follow(lMaster);
        lSlaveTwo.follow(lMaster);

        //set slaves to be inverted
        lSlaveOne.setInverted(true);
        lSlaveTwo.setInverted(true);

        lMaster.set(ControlMode.PercentOutput, power);
    }
    
}
