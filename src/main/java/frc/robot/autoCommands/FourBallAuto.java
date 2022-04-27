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
import frc.robot.utils.MathDoer;


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
    

    PID autoAim = new PID(.05, 0.00005, .005);

    

    private Timer timer = new Timer();
    private Timer loadBallTime = new Timer();
    private Timer feedingTime = new Timer();
    private Timer aimTime = new Timer();
    private Timer justChillin = new Timer();
    private Timer upTime = new Timer();
    private int counter;

    //**********************************************************************************************************************
    //Movement speed of robot in high gear when driving forward
    private final double driveSpeed = .3;
    //Zoom speed for driving
    private final double zoomSpeed = .6;
    //how long to aim the limelight
    private final double aimingTime = .65;
    //speed when turning at any point in the auto
    private static final double turnSpeed = 0.3;
    //Zoom Turn Speed
    private static final double zoomTurn = .6;
    //bigger = more turn, smaller = less turn, don't worry about units, encoder ticks don't relate to angles -- how far to turn - most likely wrong   
    private static final double turnAngle = MathDoer.turnTicks(64);;
    //the rpm the shooter is constantly  
    private static final double defaultRPM = -3600;
    //the time the indexer takes to get the next ball to the feeder
    private static final double ballFeast = .8;
    //the inches the robot is away from human player
    private static final double inchesToAkshit = (100 /(6*Math.PI) * Constants.TalonSRXCPR); //Dist 95
    //the amount of time it waits for human player to give it a ball
    private static final double whatIsAkshitDoing = 1.75;
    

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
            shooter.setCoolerestRPM(-limeLight.rpm());
        }
        else {
            shooter.setCoolerestRPM(defaultRPM);
        }
        //-------------------------------------------------------------------------------




        //Actual actions-----------------------------------------------------------------
        //System.out.println(counter);
        //Drive
        if(counter == 0) {
            intake.suck(true);
            indexer.suckUp(true);
            if(!ballManager.getFirstPositionBall()) {
                if(driveTrain.getLeftPos() > -(76 * inches * 1/3)) {
                    driveTrain.lDrive(zoomSpeed);
                }
                else if (driveTrain.getLeftPos() > -(76 * inches)) {
                    driveTrain.lDrive(driveSpeed);
                } 
                if(driveTrain.getRightPos() > -(76 * inches * 1/3)) {
                    driveTrain.rDrive(zoomSpeed);
                }
                else if (driveTrain.getRightPos() > -(76 * inches)) {
                    driveTrain.rDrive(driveSpeed);
                }
                else {
                    driveTrain.stop();
                    driveTrain.setZero();
                    intake.suck(false);
                    ballManager.newBall();
                    counter = 8;
                }

            } else {
                driveTrain.stop();
                driveTrain.setZero();
                intake.suck(false);
                ballManager.newBall();
                counter++;
            }
        }

        //look for limelight
        if(counter == 1) {
            intake.up();
            if(limeLight.getRightDistance() < 20) {
                driveTrain.rDrive(-zoomTurn);
                driveTrain.lDrive(zoomTurn);
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
            if(feedingTime.get() <= .3) {
                indexer.up();
            }
            else if(feedingTime.get() <= .8) {
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
                    if (ballsShot == 2) {
                        counter = 4;
                        ballsShot = 0;
                        driveTrain.setZero();
                    }
                }
                
            }
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
            upTime.start();
            if (upTime.get() < .001) {
                intake.up();
            } else {
                upTime.stop();
            }
            intake.suck(true);
            indexer.suckUp(true);
            if(driveTrain.getLeftPos() > -inchesToAkshit * 1/2) {
                intake.down();
                driveTrain.lDrive(zoomSpeed);
                driveTrain.rDrive(zoomSpeed);
            } 
            else if(driveTrain.getLeftPos() > -inchesToAkshit) {
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
            if(driveTrain.getLeftPos() < (inchesToAkshit-8192) * 5/6) {
                driveTrain.lDrive(-zoomSpeed);
                driveTrain.rDrive(-zoomSpeed);
            }
            else if(driveTrain.getLeftPos() < inchesToAkshit) {
                intake.up();
                intake.suck(false);
                driveTrain.lDrive(-driveSpeed);
                driveTrain.rDrive(-driveSpeed);
            }
            else {
                driveTrain.stop();
                counter++;
            }
        }

        //target limelight
        if(counter == 8) {
            aimTime.reset();
            if(limeLight.getRightDistance() < 20) {
                driveTrain.rDrive(-zoomTurn);
                driveTrain.lDrive(zoomTurn);
            }
            else {
                aimTime.start();
                counter++;
            }
        }

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
            else if(feedingTime.get() <= .8) {
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
            if (ballsShot == 2) {
                counter = 26;
                ballsShot = 0;
                driveTrain.setZero();
            }
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
