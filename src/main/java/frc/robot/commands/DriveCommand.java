package frc.robot.commands;

import java.sql.DriverAction;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
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
    
        leftY = Controls.getLeftStickY();
        rightX = Controls.getRightStickX();
        rightTwist = Controls.getRightStickTwist();

        if(Math.abs(rightTwist) > 0) {
            leftPower += rightTwist;
            rightPower += -rightTwist;
        }

        leftPower += leftY;
        rightPower += leftY;

        if(rightX > 0) {
            rightPower -= rightX;
            if(rightPower <= 0)
                rightPower = 0;
        }
        else if(rightX < 0) {
            leftPower += rightX;
            if(leftPower <= 0)
                leftPower = 0;
        }
        //System.out.println(leftPower);

        driveTrain.lDrive(leftPower);
        driveTrain.rDrive(rightPower);

    }


}
