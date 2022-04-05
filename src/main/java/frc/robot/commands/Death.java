package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.Shooter;

public class Death extends CommandBase{
    private DriveTrain driveTrain;
    private Shooter shooter;
    private Intake intake;
    private LimeLight limeLight;
    private Indexer indexer;

    public Death(DriveTrain driveTrain, Shooter shooter, Intake intake, LimeLight limeLight, Indexer indexer) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.shooter = shooter;
        addRequirements(shooter);
        this.intake = intake;
        addRequirements(intake);
        this.limeLight = limeLight;
        addRequirements(limeLight);
        this.indexer = indexer;
        addRequirements(indexer);
    }

    public void execute() {
        driveTrain.stop();
        shooter.stopShooter();
        intake.suck(false);
        indexer.suckUp(false);
    }
}