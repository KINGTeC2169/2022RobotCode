package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;
import frc.robot.utils.Constants;

public class DriveTrain extends SubsystemBase {
    TalonSRX rMaster = new TalonSRX(ActuatorMap.rMaster);
    TalonSRX lMaster = new TalonSRX(ActuatorMap.lMaster);
    //TODO: Find out which motor controllers are Victors
    TalonSRX rSlave = new TalonSRX(ActuatorMap.rSlave);
    TalonSRX lSlave = new TalonSRX(ActuatorMap.lSlave); 

    private double endPos;

    //Drive for the right gearbox
    public void rDrive(double power) {
        //set slaves to follow
        rSlave.follow(rMaster);


        //set slaves to be inverted
        rMaster.setInverted(true);

        rMaster.set(ControlMode.PercentOutput, power);
    }

    //Drive for the left gearbox
    public void lDrive(double power) {
        //set slaves to follow
        lSlave.follow(lMaster);


        //set slaves to be inverted
        lMaster.setInverted(true);

        lMaster.set(ControlMode.PercentOutput, power);
    }

    public void driveFor(double inches, double speed) {
        endPos = Constants.TalonSRXCPR / Constants.wheelCirc;
        lMaster.setSelectedSensorPosition(0);
        for(double currentPos = 0.0; currentPos < endPos; currentPos = lMaster.getSelectedSensorPosition()) {
            rDrive(speed);
            lDrive(speed);
        }    
    }
    
}
