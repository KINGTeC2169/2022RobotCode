package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.ShuffleboardManager;


public class RobotInit extends CommandBase {
    private Climber climber;
    private Indexer indexer;
    private Intake intake;
    private BallManager ballManager;
    private ShuffleboardManager shuffleboardManager;
    private boolean isDone = false;
    private Timer timer = new Timer();

    public RobotInit(Climber climber, Indexer indexer, Intake intake, BallManager ballManager, ShuffleboardManager shuffleboardManager) {
        this.climber = climber;
        addRequirements(climber);
        this.indexer = indexer;
        addRequirements(indexer);
        this.intake = intake;
        addRequirements(intake);
        this.ballManager = ballManager;
        addRequirements(ballManager);
        this.shuffleboardManager = shuffleboardManager;
        addRequirements(shuffleboardManager);
    }

    @Override
    public void execute() {
        ballManager.reset();
    }
    @Override
    public boolean isFinished() {
        return isDone;
    }
}
