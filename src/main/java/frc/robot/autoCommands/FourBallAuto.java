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


import edu.wpi.first.wpilibj.Timer;

public class FourBallAuto extends CommandBase{
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


    private boolean zeroing = true;
    private boolean intakeDown = true;
    private boolean singleInit = true;
    private boolean sameBall;
    private int ballsShot = 0;
    private double desiredRPM;
    private double lastKnownRPM;
    private boolean snakeVenom;
    //public static final int BLUE = 1;
    //public static final int RED = 2;
    public static final double inches = 427.4745225579834;
    private static final double turnSpeed = 0.3;
    private static final double turnAngle = 10000; //bigger = more turn, smaller = less turn, don't worry about units, encoder ticks don't relate to angles
    private static final double defaultRPM = 3600;

    PID autoAim = new PID(.05, 0.00005, .005);

    

    private Timer timer = new Timer();
    private Timer loadBallTime = new Timer();
    private Timer feedingTime = new Timer();
    private Timer aimTime = new Timer();
    private Timer justChillin = new Timer();
    private int counter;

    public FourBallAuto(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, 
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
        driveTrain.setZero();
        ballManager.startAuto();
        autoAim.setSetpoint(0);
        driveTrain.rDrive(0);
        driveTrain.lDrive(0);
        //System.out.println("running auto init bruv");
    }
    @Override
    public void execute() {
        //"Your code goes here" - CodeHS

        CompressorTank.disable();

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
        


        //Constant sensing--------------------------------------------------------------
        if(beamBreak.isBall() && !sameBall) {
            ballManager.newBall();
            sameBall = true;
        } 
        if(!beamBreak.isBall()) {
            sameBall = false;
        }

        if(limeLight.getLeftDistance() + limeLight.getRightDistance() != 0) {
            shooter.setCoolerestRPM(limeLight.rpm());
        }
        else {
            shooter.setCoolerestRPM(defaultRPM);
        }
        //-------------------------------------------------------------------------------




        //Actual actions-----------------------------------------------------------------

        //Drive
        if(counter == 0) {
            intake.suck(true);
            indexer.suckUp(true);
            if(!ballManager.getFirstPositionBall()) {
                if(driveTrain.getLeftPos() > -(76 * inches)) {
                    driveTrain.lDrive(driveSpeed);
                }
                if(driveTrain.getRightPos() > -(76 * inches)) {
                    driveTrain.rDrive(driveSpeed);
                }
                else {
                    driveTrain.stop();
                    //driveTrain.setZero();
                    intake.suck(false);
                    ballManager.newBall();
                    counter = 8;
                }

            } else {
                driveTrain.stop();
                //driveTrain.setZero();
                intake.suck(false);
                ballManager.newBall();
                counter++;
            }
        }

        //look for limelight
        if(counter == 1) {
            intake.up();
            if(limeLight.getRightDistance() == 0) {
                driveTrain.rDrive(-turnSpeed);
                driveTrain.lDrive(turnSpeed);
            }
            else {
                aimTime.start();
                counter++;
            }
        }

        //target limelight
        if(counter == 2) {
            if(aimTime.get() < aimingTime) {
                autoAim.calculate(limeLight.getRightXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(-autoAim.getOutput());
            }
            else {
                driveTrain.stop();
                aimTime.stop();
                aimTime.reset();
                counter++;
            }
        }

        //shoots balls
        if(counter == 3 && ballsShot < 2) {
            feedingTime.start();
            if(feedingTime.get() <= .5) {
                indexer.up();
            }
            else if(feedingTime.get() <= 1) {
                indexer.down();
            }
            else {
                loadBallTime.start();
                if(loadBallTime.get() >= ballFeast || ballsShot == 1) {
                    ballsShot++;
                    feedingTime.stop();
                    feedingTime.reset();
                    loadBallTime.stop();
                    loadBallTime.reset();
                }
            }
        } else {
            counter = 4;
            ballsShot = 0;
            driveTrain.setZero();
        }

        //turns toward human player balls
        if(counter == 4) {
            indexer.suckUp(false);
            if(driveTrain.getLeftPos() < turnAngle) {
                driveTrain.lDrive(-turnSpeed);
            }
            if(driveTrain.getRightPos() > -turnAngle) {
                driveTrain.rDrive(turnSpeed);
            }
            else {
                driveTrain.stop();
                driveTrain.setZero();
                counter++;
            }
        }

        //drives to human player
        if(counter == 5) {
            intake.suck(true);
            indexer.suckUp(true);
            if(driveTrain.getLeftPos() > -(inchesToAkshit * inches)) {
                intake.down();
                driveTrain.lDrive(driveSpeed);
                driveTrain.rDrive(driveSpeed);
            }
            else {
                driveTrain.stop();
                counter++;
            }
        }

        //waits for human player
        if(counter == 6) {
            justChillin.start();
            if(justChillin.get() > whatIsAkshitDoing) {
                justChillin.stop();
                driveTrain.setZero();
                counter++;
            }
        }

        //drives back to hub
        if(counter == 7) {
            if(driveTrain.getLeftPos() < (inchesToAkshit * inches)) {
                intake.up();
                intake.suck(false);
                driveTrain.lDrive(-0.3);
                driveTrain.rDrive(-0.3);
            }
            else {
                driveTrain.stop();
                counter++;
            }
        }

        //look for limelight
        if(counter == 8) {
            if(limeLight.getRightDistance() == 0) {
                driveTrain.rDrive(-turnSpeed);
                driveTrain.lDrive(turnSpeed);
            }
            else {
                aimTime.start();
                counter++;
            }
        }

        //target limelight
        if(counter == 9) {
            if(aimTime.get() < aimingTime) {
                autoAim.calculate(limeLight.getRightXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(-autoAim.getOutput());
            }
            else {
                driveTrain.stop();
                aimTime.stop();
                aimTime.reset();
                counter++;
            }
        }

        //shoots human player's balls
        if(counter == 10 && ballsShot < 2) {
            feedingTime.start();
            if(feedingTime.get() <= .5) {
                indexer.up();
            }
            else if(feedingTime.get() <= 1) {
                indexer.down();
            }
            else {
                loadBallTime.start();
                if(loadBallTime.get() >= ballFeast || ballsShot == 1) {
                    ballsShot++;
                    feedingTime.stop();
                    feedingTime.reset();
                    loadBallTime.stop();
                    loadBallTime.reset();
                }
            }
        } else {
            counter = 26;
            ballsShot = 0;
            driveTrain.setZero();
        }

        //end auto
        if(counter == 26) {
            driveTrain.stop();
            shooter.stopShooter();
            intake.suck(false);
            indexer.suckUp(false);
            indexer.down();
        }

        /*
        shuffleboard.number("Vroom", driveTrain.rightPercent());
        shuffleboard.number("Left Vroom", driveTrain.leftPercent());
        shuffleboard.number("Left Pos", driveTrain.getLeftPos());
        shuffleboard.number("Right Pos", driveTrain.getRightPos());
        shuffleboard.boolInABox("Init", singleInit);
        shuffleboard.number("Counter", counter);
        shuffleboard.boolInABox("Shifty Shafts", driveTrain.dogStatus());
        shuffleboard.boolInABox("POS 1 auto", ballManager.getFirstPositionBall());
        shuffleboard.boolInABox("POS 2 auto", ballManager.getSecondPositionBall());
        shuffleboard.number("Auto RPM", desiredRPM);
        */


    }

    @Override
    public void end(boolean interrupted) {
        /* Test this if death doesn't work
        if(interrupted) {
            driveTrain.lDrive(0);
            driveTrain.rDrive(0);
            shooter.stopShooter();
            intake.suck(false);
            indexer.suckUp(false);
        }
        */
    }
    
}
