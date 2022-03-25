package frc.robot.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CompressorTank;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import frc.robot.utils.Constants;
import frc.robot.utils.PID;

import java.rmi.server.RMIClassLoader;

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
    private LimeLight limeLight;
    private BeamBreak beamBreak;


    private int team;
    private boolean holyShitTheresABall;
    private boolean zeroing = true;
    private boolean intakeDown = true;
    private boolean singleInit = true;
    private boolean sameBall;
    private double desiredRPM;
    private double lastKnownRPM;
    //public static final int BLUE = 1;
    //public static final int RED = 2;
    public static final double inches = 106.8687;

    PID autoAim = new PID(.05, 0.00005, .005);
    

    private Timer timer = new Timer();
    private Timer shotTimer = new Timer();
    private Timer suckTime = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, 
    Climber climber, Intake intake, Vision vision, BallManager ballManager, LimeLight limeLight, BeamBreak beamBreak) {
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
        this.limeLight = limeLight;
        addRequirements(limeLight);
        this.beamBreak = beamBreak;
        addRequirements(beamBreak);
    }

    @Override
    public void execute() {
        //"Your code goes here" - CodeHS
        if(singleInit) {
            driveTrain.setZero();
            ballManager.startAuto();
            autoAim.setSetpoint(0);
        }
        else
            singleInit = false;

        //Initialization
        /*
        if(DriverStation.getAlliance().equals(Alliance.Blue)) {
            team = BLUE;
        }
        else if(DriverStation.getAlliance().equals(Alliance.Red)) {
            team = RED;
        }
        */
        if(intakeDown) {
          timer.start();
            if(timer.get() < 2) {
                intake.down();
            }
            else {
                intakeDown = false;
                timer.stop();
            }  
        }
        

        if(zeroing) {
            if(climber.getCurrent() < 8) {
            climber.retractArmSlow();
            }
            else {
                zeroing = false;
                climber.stopArm();
                climber.setZero();
            }
        }
        
        if(beamBreak.isBall() && !sameBall) {
            ballManager.newBall();
            sameBall = true;
        } 
        if(!beamBreak.isBall()) {
            sameBall = false;
        }

        double rightPower = 0;
        double leftPower = 0;
        /*
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
        if(counter == 0 && holyShitTheresABall) {
            System.out.println("balls i guess");
        }
        */
        //Drive
        if(counter == 0) {
            intake.suck(true);
            if(!ballManager.getFirstPositionBall()) {
                if(driveTrain.getLeftPos() < 24 * inches) {
                    driveTrain.lDrive(.5);
                }
                if(driveTrain.getRightPos() < 24 * inches) {
                    driveTrain.rDrive(.5);
                }
                else
                    driveTrain.stop();
                    ballManager.newBall();
            }
            else {
                driveTrain.stop();
                intake.suck(false);
                counter++;
            }
        }


        if(limeLight.rpm() != 0) {
            desiredRPM = limeLight.rpm();
            lastKnownRPM = limeLight.rpm();
        }
        else {
            //Error: limelight malfunction- shoot from edge of tarmac
            desiredRPM = lastKnownRPM;
        }

        if(counter == 1) {
            if(limeLight.getLeftDistance() + limeLight.getRightDistance() == 0 || (limeLight.getRightDistance() > 0 && limeLight.getLeftDistance() > 0)) {
                driveTrain.rDrive(0.3);
                driveTrain.lDrive(-0.3);
                shotTimer.stop();
                shotTimer.reset();
            }
            if(limeLight.getLeftDistance() != 0) {
                shotTimer.start();
                shooter.setCoolerestRPM(desiredRPM);
                autoAim.calculate(limeLight.getLeftXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(-autoAim.getOutput());
            }
            else if(limeLight.getRightDistance() != 0){
                shotTimer.start();
                shooter.setCoolerestRPM(-desiredRPM);
                autoAim.calculate(limeLight.getRightXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(autoAim.getOutput());
            }
        }

        if(shotTimer.get() > 5) {
            indexer.shoveBall();
            ballManager.shootBall();
            indexer.suckUp(true);
            ballManager.cycleBall();
            suckTime.start();
            shotTimer.reset();
        }

        if(suckTime.get() >= 3) {
            indexer.suckUp(false);
            suckTime.stop();
            suckTime.reset();
        }
        






    }
}
