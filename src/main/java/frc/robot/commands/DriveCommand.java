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
    private double leftPower;
    private double rightPower;

    public DriveCommand(DriveTrain subsystem) {
        driveTrain = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        leftY = Controls.getLeftStickY();
        rightX = Controls.getRightStickX();
        rightTwist = Controls.getRightStickTwist();

        if(rightTwist > 0) {
            leftPower += rightTwist;
            rightPower += -rightTwist;
        }

        leftPower += leftY;
        rightPower += rightX;

        driveTrain.lDrive(leftPower);
        driveTrain.rDrive(rightPower);
    }

    @Override
    public void execute() {
    

    }


}
