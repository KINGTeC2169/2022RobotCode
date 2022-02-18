package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.utils.Constants;


public class RobotInit extends CommandBase {
    private Climber climber;
    private Indexer indexer;
    private Intake intake;
    private boolean isDone = false;
    private Timer timer = new Timer();

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
        timer.start();
        while(timer.get() < 2) {
            intake.up();
        }
        timer.stop();

        //climber.movePistonForward();
        indexer.down();
        //TODO: 26 is a placeholder for current, make sure to change
        //for(double current = climber.getCurrent(); current < Constants.climberCurrent; current = climber.getCurrent()) {
            //climber.retractArm();
        //}
        climber.setZero();
        isDone = true;
    }
    @Override
    public boolean isFinished() {
        return isDone;
    }
}
