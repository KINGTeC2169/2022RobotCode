package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;

public class Death extends CommandBase{
    private DriveTrain driveTrain;
    private Shooter shooter;

    public Death(DriveTrain driveTrain, Shooter shooter) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.shooter = shooter;
        addRequirements(shooter);
    }

    public void execute() {
        driveTrain.rDrive(0);
        driveTrain.lDrive(0);
        shooter.stopShooter();
    }
}
