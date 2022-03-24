package frc.robot.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CompressorTank;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.utils.Constants;
import frc.robot.utils.PID;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Autonomous extends CommandBase {
    
    private DriveTrain driveTrain;
    private Shooter shooter;
    private NavX navx;
    private Indexer indexer;
    private Climber climber;
    private Intake intake;
    private Vision vision;
    private BallManager ballManager;


    private int team;
    private boolean holyShitTheresABall;
    public static final int BLUE = 1;
    public static final int RED = 2;

    PID ballHunt = new PID(.05, 0.00005, .005);
    

    private Timer timer = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, Climber climber, Intake intake, Vision vision, BallManager ballManager) {
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
        this.vision = vision;
        addRequirements(vision);
        this.ballManager = ballManager;
        addRequirements(ballManager);
    }

    @Override
    public void execute() {
        //"Your code goes here" - CodeHS

        //Initialization
        if(DriverStation.getAlliance().equals(Alliance.Blue)) {
            team = BLUE;
        }
        else if(DriverStation.getAlliance().equals(Alliance.Red)) {
            team = RED;
        }
        
        

        if(climber.getCurrent() < 8) {
            climber.retractArmSlow();
        }
        else {
            climber.stopArm();
            climber.setZero();
        }


        double rightPower = 0;
        double leftPower = 0;

        if((vision.validBlueTarget() && team == BLUE) || (vision.validRedTarget() && team == RED)) {
            holyShitTheresABall = true;
        } else {
            holyShitTheresABall = false;
        }

        if(holyShitTheresABall && ballManager.getNumberOfBalls() < 2) {
            ballHunt.setSetpoint(320);
            if(team == BLUE) {
                ballHunt.calculate(vision.getBlueBallX());
            }
            else {
                ballHunt.calculate(vision.getRedBallX());
            }
            rightPower += ballHunt.getOutput();
            leftPower -= ballHunt.getOutput();
            //driveTrain.rampOn();

            intake.suck(true);

            rightPower += 0.5;
            leftPower += 0.5;

            driveTrain.rDrive(rightPower);
            driveTrain.lDrive(leftPower);

            
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




    }
}
