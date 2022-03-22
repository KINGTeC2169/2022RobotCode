package frc.robot.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CompressorTank;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Constants;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends CommandBase {
    
    private DriveTrain driveTrain;
    private Shooter shooter;
    private NavX navx;
    private Indexer indexer;
    private Climber climber;
    private Intake intake;

    private double turnAngle;
    

    private Timer timer = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, Climber climber, Intake intake) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.shooter = shooter;
        addRequirements(shooter);
        this.navx = navx;
        addRequirements(navx);
        this.indexer = indexer;
        addRequirements(indexer);
        this.climber = climber;
        addRequirements(climber);
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        //"Your code goes here" - CodeHS

        //Initialization (dont know if we need this or not, i just added it anyways)
        
        if(climber.getCurrent() < 8) {
            climber.retractArmSlow();
        }
        else {
            climber.stopArm();
            climber.setZero();
        }

        //Drive
        if(counter == 0) {
            intake.down();
            intake.suck(true);
            if(driveTrain.getRightPos() < 50000)
                driveTrain.rDrive(.7);
            if(driveTrain.getLeftPos() < 50000)
                driveTrain.lDrive(.7);
            else
                counter++;
        }


        if(counter == 1) {
            intake.suck(false);
            counter++;
        }

    }
}
