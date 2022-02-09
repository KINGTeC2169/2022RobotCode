package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;


public class RobotInit extends CommandBase {
    private Climber climber;
    private Indexer indexer;
    private Intake intake;

    public RobotInit(Climber climber, Indexer indexer, Intake intake) {
        this.climber = climber;
        addRequirements(climber);
        this.indexer = indexer;
        addRequirements(indexer);
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        climber.movePistonForward();
        indexer.down();
        intake.up();
    }
}
