package frc.robot.commands;

import java.sql.DriverAction;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.utils.Constants;
import frc.robot.utils.Controls;

public class DriveCommand extends CommandBase {
    
    private DriveTrain driveTrain;

    private double leftY;
    private double rightX;
    private double rightTwist;
   

    public DriveCommand(DriveTrain subsystem) {
        driveTrain = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
       

    }

    @Override
    public void execute() {
        double leftPower = 0;
        double rightPower = 0;
        double turnPower = 0;

        leftY = Controls.getLeftStickY();
        rightX = Controls.getRightStickX();
        rightTwist = Controls.getRightStickTwist();

        leftPower += leftY;
        rightPower += leftY;
        //System.out.println("1: " + leftY);
        //System.out.println(rightX);
        //0.1 because of joystick TWIST deadzone are you happy now Jake
        if(Math.abs(rightTwist) > 0.1) {
            leftPower -= rightTwist;
            rightPower += rightTwist;
        }
       else{
           turnPower = Math.abs(leftY) * rightX * Constants.turnSensitivity;
       }
        rightPower -= turnPower;
        leftPower += turnPower;
        
       // System.out.println(leftPower);

        driveTrain.lDrive(leftPower);
        driveTrain.rDrive(rightPower);

    }


}
