package frc.robot.commands;

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

        
    }

    @Override
    public void execute() {
    

    }


}
