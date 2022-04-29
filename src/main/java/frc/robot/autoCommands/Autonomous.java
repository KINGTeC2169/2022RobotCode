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
import frc.robot.utils.PID;

import edu.wpi.first.wpilibj.Timer;


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
    private ShuffleboardManager shuffleboard;


    private int team;
    private boolean holyShitTheresABall;
    private boolean zeroing = true;
    private boolean intakeDown = true;
    private boolean singleInit = true;
    private boolean sameBall;
    private double desiredRPM;
    private double lastKnownRPM;
    private boolean snakeVenom;
    //public static final int BLUE = 1;
    //public static final int RED = 2;
    public static final double inches = 427.4745225579834;

    PID autoAim = new PID(.05, 0.00005, .005);
    

    private Timer timer = new Timer();
    private Timer shotTimer = new Timer();
    private Timer suckTime = new Timer();
    private Timer feedingTime = new Timer();
    private Timer aimTime = new Timer();
    private Timer thePowerOfAnIntelNeuralComputeStick2 = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter, NavX navx, Indexer indexer, 
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
        driveTrain.downShift();
        driveTrain.setZero();
        ballManager.startAuto();
        autoAim.setSetpoint(0);
        driveTrain.rDrive(0);
        driveTrain.lDrive(0);
        System.out.println("running auto init bruv");
    }
    @Override
    public void execute() {
        //"Your code goes here" - CodeHS
        /*
        if(singleInit) {
            counter = 0;
            driveTrain.downShift();
            driveTrain.setZero();
            ballManager.startAuto();
            autoAim.setSetpoint(0);
            singleInit = false;
            driveTrain.rDrive(0);
            driveTrain.lDrive(0);
            System.out.println("running auto init");
        }*/
        if(intakeDown) {
          timer.start();
            if(timer.get() < 2) {
                intake.down();
                CompressorTank.disable();
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
        /*
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
        if(counter == 0 && holyShitTheresABall) {
            System.out.println("balls i guess");
        }
        */
        //Drive
        if(counter == 0) {
            intake.suck(true);
            indexer.suckUp(true);
            if(!ballManager.getFirstPositionBall()) {
                if(driveTrain.getLeftPos() > -(80 * inches)) {
                    driveTrain.lDrive(.5);
                }
                if(driveTrain.getRightPos() > -(80 * inches)) {
                    driveTrain.rDrive(.5);
                }
                else {
                    driveTrain.stop();
                    intake.suck(false);
                    ballManager.newBall();
                    counter++;
                }
            }
            
            else {
                driveTrain.stop();
                intake.suck(false);
                ballManager.newBall();
                counter++;
            }
        }
        if(counter == 1) {
            driveTrain.setZero();
            counter++;
        }
        if(counter == 2) {
            if(driveTrain.getLeftPos() < (2 * inches)) {
                indexer.suckUp(true);
                driveTrain.lDrive(-.3);
            }
            if(driveTrain.getRightPos() < (2 * inches)) {
                indexer.suckUp(true);
                driveTrain.rDrive(-.3);
            }
            else {
                driveTrain.stop();
                indexer.suckUp(false);
                counter++;
            }
        }


        if(limeLight.rpm() != 0) {
            desiredRPM = limeLight.rpm();
            lastKnownRPM = limeLight.rpm();
        }
        else {
            desiredRPM = lastKnownRPM;
        }

        if(counter == 3) {
            intake.up();
            if(limeLight.getLeftDistance() + limeLight.getRightDistance() == 0 || (limeLight.getRightDistance() > 0 && limeLight.getLeftDistance() > 0)) {
                driveTrain.rDrive(-0.3);
                driveTrain.lDrive(0.3);
                shotTimer.stop();
                //shotTimer.reset();
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
    
            if(shotTimer.get() > 3.5) {
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

            if(suckTime.get() >= 3) {
                indexer.suckUp(false);
                suckTime.stop();
                suckTime.reset();
            }
            if(ballManager.getNumberOfBalls() == 0) {
                counter++;
            }
        }
        if(counter == 4) {
            driveTrain.stop();
            shooter.stopShooter();
            intake.suck(false);
            indexer.suckUp(false);
            indexer.down();
            //counter++;
        }

        if(counter == 5) {
            driveTrain.rDrive(0.2);
            driveTrain.lDrive(-0.2);
            if(vision.heKindaValidTho()) {
                counter++;
            }
        }
        if(counter == 5) {
            thePowerOfAnIntelNeuralComputeStick2.start();
            driveTrain.rDrive(-0.2);
            driveTrain.lDrive(0.2);
            if(thePowerOfAnIntelNeuralComputeStick2.get() > 2) {
                driveTrain.stop();
                counter++;
            }
        }

        if(counter == 6) {
            driveTrain.rDrive(0.3);
            driveTrain.lDrive(0.3);
            intake.suck(true);
            indexer.suckUp(true);
            if(ballManager.getFirstPositionBall()) {
                intake.suck(false);
                driveTrain.stop();
                counter++;
            }
        }

        if(counter == 7) {
            if(limeLight.getLeftDistance() + limeLight.getRightDistance() == 0 || (limeLight.getRightDistance() > 0 && limeLight.getLeftDistance() > 0)) {
                driveTrain.rDrive(0.3);
                driveTrain.lDrive(-0.3);
                System.out.println("Limelight error");
                shotTimer.stop();
                //shotTimer.reset();
            }
            if((limeLight.getRightDistance() + limeLight.getLeftDistance()) != 0 && !snakeVenom) {
                aimTime.start();
                snakeVenom = true;
            }
            if(limeLight.getLeftDistance() != 0 && aimTime.get() < 1.5) {
                shotTimer.start();
                shooter.setCoolerestRPM(desiredRPM);
                autoAim.calculate(limeLight.getLeftXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(-autoAim.getOutput());
            }
            else if(limeLight.getRightDistance() != 0 && aimTime.get() < 1.5){
                shotTimer.start();
                shooter.setCoolerestRPM(-desiredRPM);
                autoAim.calculate(limeLight.getRightXPercent());
                driveTrain.rDrive(autoAim.getOutput());
                driveTrain.lDrive(autoAim.getOutput());
            }
    
            if(shotTimer.get() > 3.5) {
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

            if(suckTime.get() >= 3) {
                indexer.suckUp(false);
                suckTime.stop();
                suckTime.reset();
            }
            if(ballManager.getNumberOfBalls() == 0) {
                counter++;
            }
        }
        if(counter == 8) {
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
