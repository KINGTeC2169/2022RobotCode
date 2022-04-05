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
import frc.robot.subsystems.ShuffleboardManager;
import frc.robot.subsystems.Vision;
import frc.robot.utils.Constants;
import frc.robot.utils.PID;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class JakeFourBallAuto extends CommandBase {
    
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
    private ShuffleboardManager shuffleboard;

    //These variables you can change *********************************************************************************************************
    // variable inches is driving one inch

    //this sets the movement speed for auto -- at some point it will get unstable but if it takes to long move this up -- we are in high gear
    private double movementSpeed = .5;

    //this is for distance gone when the robot does not actually pick up a ball but its stops to stop from hitting the wall - this really should not be changed
    private double wallCrashTime = (76 * inches);
    //this is the first shot rpm setpoint -- please change this this is a random number
    private double firstShotRPM = 4000;
    //how long it should limelight aim
    private double timeToAim = 1.5;
    //how long it should suck to move ball to the shooter
    private double suckingTime = 2;
    //this is the turn after the 2 og balls are shot -- this is untested and should be changed
    private double firstTurnDistance = (24 * inches);
    //this is how far the robot persues the human player pen IIIIIIFFFFF it misses the ball by human player -- this is untested change
    private double humanPlayerDistance = (200 * inches);
    //how long to wait for player to give ball
    private double waitTimeForHuman = 3;
    //the second shot rpm - usually is the same as the first
    private double secondShotRPM = 4000;

    //****************************************************************************************************************************************
    

    private int team; // hey team
    private boolean holyShitTheresABall; // i wish
    private boolean zeroing = true;
    private boolean intakeDown = true;
    private boolean singleInit = true; //init bruv
    private boolean sameBall;
    private double desiredRPM;
    private double lastKnownRPM;
    private boolean snakeVenom; //why do we have this
    //public static final int BLUE = 1;
    //public static final int RED = 2;
    public static final double inches = 427.4745225579834;

    PID autoAim = new PID(.05, 0.00005, .005);
    

    private Timer timer = new Timer();
    private Timer shotTimer = new Timer();
    private Timer suckTime = new Timer();
    private Timer feedingTime = new Timer();
    private Timer aimTime = new Timer();
    private Timer waitTime = new Timer();
    private Timer thePowerOfAnIntelNeuralComputeStick2 = new Timer();
    private int counter;
    private double leftDriveDis;
    private double rightDriveDis;
    private boolean done = false;
    private boolean killIt = false;

    public JakeFourBallAuto(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, 
    Climber climber, Intake intake, Vision vision, BallManager ballManager, LimeLight limeLight, BeamBreak beamBreak, ShuffleboardManager shuffleboard) {
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
        this.shuffleboard = shuffleboard;
        addRequirements(shuffleboard);
        singleInit = true;
    }

    @Override
    public void initialize() {
        counter = 0;
        driveTrain.shiftThatDog();
        driveTrain.setZero();
        ballManager.startAuto();
        autoAim.setSetpoint(0);
        driveTrain.rDrive(0);
        driveTrain.lDrive(0);
        System.out.println("4 Ball Auto??");
    }
    @Override
    public void execute() {
        CompressorTank.disable();
        //"Your code goes here" - CodeHS
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
        driveTrain.rampOff();

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
        //Drive
        if(counter == 0) {
            shooter.setCoolerestRPM(firstShotRPM);
            intake.suck(true);
            indexer.suckUp(true);
            if(!ballManager.getFirstPositionBall()) {
                if(driveTrain.getLeftPos() > -wallCrashTime) {
                    driveTrain.lDrive(movementSpeed);
                }
                if(driveTrain.getRightPos() > -wallCrashTime) {
                    driveTrain.rDrive(movementSpeed);
                }
                else {
                    driveTrain.stop();
                    intake.suck(false);
                    ballManager.newBall();
                    counter++;
                }
            } else {
                driveTrain.stop();
                intake.suck(false);
                ballManager.newBall();
                counter++;
            }
        }
        if(counter == 1) {
            shooter.setCoolerestRPM(firstShotRPM);
            driveTrain.setZero();
            counter++;
        }

        if(limeLight.rpm() != 0) {
            desiredRPM = limeLight.rpm();
            lastKnownRPM = limeLight.rpm();
        }
        else {
            desiredRPM = lastKnownRPM;
        }

        if(counter == 2) {
            intake.up();
            if(limeLight.getLeftDistance() + limeLight.getRightDistance() == 0 || (limeLight.getRightDistance() > 0 && limeLight.getLeftDistance() > 0)) {
                driveTrain.rDrive(movementSpeed);
                driveTrain.lDrive(-movementSpeed);
                shotTimer.stop();
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
                driveTrain.rDrive(-autoAim.getOutput());
                driveTrain.lDrive(autoAim.getOutput());
            }
    
            if(shotTimer.get() > timeToAim) {
                indexer.up();
                feedingTime.start();
                if(feedingTime.get() >= 0.5) {
                    feedingTime.stop();
                    feedingTime.reset();
                    indexer.down();
                    ballManager.shootBall();
                    indexer.suckUp(true);
                    ballManager.cycleBall();
                    suckTime.start();
                    shotTimer.reset();
                }   
            }

            if(suckTime.get() >= suckingTime) {
                indexer.suckUp(false);
                suckTime.stop();
                suckTime.reset();
            }
            if(ballManager.getNumberOfBalls() == 0) {
                indexer.down();
                driveTrain.setZero();
                counter++;
            }
        }
        if(counter == 3) {
            intake.suck(true);
            indexer.suckUp(true);
            if(driveTrain.getLeftPos() < firstTurnDistance)
                driveTrain.lDrive(movementSpeed);
            else
                done = true;

            if(driveTrain.getRightPos() < firstTurnDistance) 
                driveTrain.rDrive(-movementSpeed);
            else
                done = true;
            if(done) {
                driveTrain.stop();
                done = false;
                counter++;
                driveTrain.setZero();
            }
        }

        if(counter == 4) {
            if(driveTrain.getLeftPos() < humanPlayerDistance)
                driveTrain.lDrive(movementSpeed);
            else {
                waitTime.start();
                if(waitTime.get() >= waitTimeForHuman) {
                    waitTime.stop();
                    waitTime.reset();
                    killIt = true;
                }
            }
            if(driveTrain.getRightPos() < humanPlayerDistance)
                driveTrain.rDrive(movementSpeed);
            else {
                waitTime.start();
                    if(waitTime.get() >= waitTimeForHuman) {
                        waitTime.stop();
                        waitTime.reset();
                        killIt = true;
                    }
                }
            if(ballManager.getNumberOfBalls() != 0) {
                driveTrain.stop();
                suckTime.start();
                if(ballManager.getFirstPositionBall() && !ballManager.getSecondPositionBall()) 
                    ballManager.cycleBall();
                
                if(suckTime.get() >= waitTimeForHuman) {
                    suckTime.stop();
                    suckTime.reset();
                    intake.suck(false);
                    intake.up();
                    leftDriveDis = driveTrain.getLeftPos();
                    rightDriveDis = driveTrain.getRightPos();
                    driveTrain.setZero();
                    counter++;
                }
            }
            if(killIt) 
                driveTrain.stop();
                counter = 26;
        }

        if(counter == 4) {
            shooter.setCoolerestRPM(secondShotRPM);
            if(driveTrain.getLeftPos() >= -leftDriveDis)
                driveTrain.lDrive(-movementSpeed);
            else
                done = true;
            if(driveTrain.getRightPos() >= -rightDriveDis)
                driveTrain.rDrive(-movementSpeed);
            else
                done = true;
            if(done) {
                driveTrain.stop();
                counter++;
            }

        }

        if(counter == 5) {
            if(limeLight.getLeftDistance() + limeLight.getRightDistance() == 0 || (limeLight.getRightDistance() > 0 && limeLight.getLeftDistance() > 0)) {
                shooter.setCoolerestRPM(secondShotRPM);
                driveTrain.rDrive(movementSpeed);
                driveTrain.lDrive(-movementSpeed);
                shotTimer.stop();
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
                driveTrain.rDrive(-autoAim.getOutput());
                driveTrain.lDrive(autoAim.getOutput());
            }
    
            if(shotTimer.get() > timeToAim) {
                indexer.up();
                feedingTime.start();
                if(feedingTime.get() >= 0.5) {
                    feedingTime.stop();
                    feedingTime.reset();
                    indexer.down();
                    ballManager.shootBall();
                    indexer.suckUp(true);
                    ballManager.cycleBall();
                    suckTime.start();
                    shotTimer.reset();
                }   
            }

            if(suckTime.get() >= suckingTime) {
                indexer.suckUp(false);
                suckTime.stop();
                suckTime.reset();
            }
            if(ballManager.getNumberOfBalls() == 0) {
                indexer.down();
                driveTrain.setZero();
                counter++;
            }
            
        }


    }
}
