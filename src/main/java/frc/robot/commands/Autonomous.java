package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Constants;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends CommandBase {
    
    private DriveTrain driveTrain;
    private Shooter shooter;
    private NavX navx;
    private Indexer indexer;
    private double turnAngle;

    private Timer timer = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.shooter = shooter;
        addRequirements(shooter);
        this.navx = navx;
        addRequirements(navx);
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void execute() {
        //"Your code goes here" - CodeHS

        //Auto plan: shoot then drive backwards
        //Yeah thats it for now

        //Initialization (dont know if we need this or not, i just added it anyways)
        if(counter == 0) {
            turnAngle = navx.getAngle() + 90;
            counter++;
        }

        //Speed up flywheel
        if(counter == 1) {
            //TODO: change to auto shoot distance and drone strike the taliban
            shooter.setRPM(Constants.taliban);
            if(shooter.hitRPM())
                counter++;
        }

        //Feed ball:

        if(counter == 2) {
            indexer.shoveBall();
            counter++;
        }

        //Turn:
        if(counter == 3) {
            driveTrain.turn(turnAngle, navx.getAngle());
            if(driveTrain.turnisDone())
                counter++;
        }

        //Drive:
        if(counter == 4) {
            driveTrain.driveToMyBalls(36, 0.5);
            if(driveTrain.driveToMyBallsisDone())
            counter++;
        }

    }
}
