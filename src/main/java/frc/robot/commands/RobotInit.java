package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallManager;


public class RobotInit extends CommandBase {

    private BallManager ballManager;


    public RobotInit(BallManager ballManager) {
        this.ballManager = ballManager;
        addRequirements(ballManager);
    }

    @Override
    public void execute() {
        ballManager.reset();
    }
}
