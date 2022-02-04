package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Constants;
import frc.robot.utils.Controls;
import frc.robot.utils.MathDoer;

public class DriveCommand extends CommandBase {
    private Timer timer;
    private double indexerTimeSave;
    
    private DriveTrain driveTrain;
    private Arduino arduino;
    private Shooter shooter;
    private Intake intake;
    private Indexer indexer;
    private Climber climber;
    private LimeLight limeLight;
    private NavX navX;
    private BallManager ballManager;
    private BeamBreak beamBreak;

    private double leftY;
    private double rightX;
    private double rightTwist;
    private double quickStopAcummolatss;
    private double shooterLimit;
    private boolean isIntaking;
    private boolean sameBall;
   
    //Adds all subsystems to the driving command
    public DriveCommand(DriveTrain driveTrain, Arduino arduino, Shooter shooter, Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navX, BallManager ballManager, BeamBreak beamBreak) {
        timer.start();
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.arduino = arduino;
        addRequirements(arduino);
        this.shooter = shooter;
        addRequirements(arduino);
        this.intake = intake;
        addRequirements(intake);
        this.indexer = indexer;
        addRequirements(indexer);
        this.climber = climber;
        addRequirements(climber);
        this.limeLight = limeLight;
        addRequirements(limeLight);
        this.navX = navX;
        addRequirements(navX);
        this.ballManager = ballManager;
        addRequirements(ballManager);
        this.beamBreak = beamBreak;
        addRequirements(beamBreak);
    }

    @Override
    public void execute() {
        //this is just a little bit of drive code. as a treat

        //------------------------------------------------Driving controls - other parts of TeleOp are farther below------------------------------
        double leftPower = 0;
        double rightPower = 0;
        double turnPower = 0;
        double overPower = 0;

        leftY = Controls.getLeftStickY();
        rightX = Controls.getRightStickX();
        rightTwist = Controls.getRightStickTwist();


        //sets the power of the left stick 
        leftPower += leftY;
        rightPower += leftY;

        //0.1 because of joystick TWIST deadzone are you happy now Jake
        if(Math.abs(rightTwist) > 0.1) {

            //checks if you're spinning in place
            if(Math.abs(leftY) < .2) {
                //dont worry about it. I have no idea (weird math that Cheesy Poofs use that we stole)
                quickStopAcummolatss = .9 * quickStopAcummolatss + .2 * MathDoer.limit(rightTwist, 1);
            }

            //basiclly a boolean to make it turn in place later in the code
            overPower = 1.0;

            //sets power of motors to quickTurn
            leftPower -= rightTwist;
            rightPower += rightTwist;
        }
       else{
           //basically a boolean again to adjust turning
           overPower = 0;

           //adjusts the amount you turn right and left in normal driving mode. Also subtracts the quick stop so we stop quick after 
           //quick turn
           turnPower = Math.abs(leftY) * rightX * Constants.turnSensitivity - quickStopAcummolatss;

           //decrease the quick stop after it is used to bring back to 0
           if (quickStopAcummolatss > 1) {
                quickStopAcummolatss -= 1;
            } else if (quickStopAcummolatss < -1) {
                quickStopAcummolatss += 1;
            } else {
                quickStopAcummolatss = 0.0;
            }
       }

       //Adds to the power that the motors need to run at
        rightPower -= turnPower;
        leftPower += turnPower;

        //this is later in the code. corrects overpowering motors when quick turning
        if (leftPower > 1.0) {
            rightPower -= overPower * (leftPower - 1.0);
            leftPower = 1.0;
        } else if (rightPower > 1.0) {
            leftPower -= overPower * (rightPower - 1.0);
            rightPower = 1.0;
        } else if (leftPower < -1.0) {
            rightPower += overPower * (-1.0 - leftPower);
            leftPower = -1.0;
        } else if (rightPower < -1.0) {
            leftPower += overPower * (-1.0 - rightPower);
            rightPower = -1.0;
        }

        //Auto aim locking mechanic
        if(Controls.getLeftStickBottom()) {
            if(limeLight.getRightXPercent() > 0 || limeLight.getLeftXPercent() > 0) {
                rightPower += limeLight.getRightXPercent() / 75;
                leftPower -= limeLight.getRightXPercent() / 75;
            }
            else if(limeLight.getLeftXPercent() < 0 || limeLight.getRightXPercent() < 0) {
                rightPower -= limeLight.getLeftXPercent() / 75;
                leftPower += limeLight.getLeftXPercent() / 75;
            }
        }
              
        
        //applies the powers to the motors
        driveTrain.lDrive(leftPower);
        driveTrain.rDrive(rightPower);

        /*--------------------------------Literally every other part of TeleOp------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
        
        //Shooter- it shoots.
        double rTrigger = Controls.getRightControllerTrigger();
        double lTrigger = Controls.getLeftControllerTrigger();

        //Shoots based on which trigger is pressed, one set of LEDs is set up
        //TODO: adjust shooter speed based on limelight distance
        if(rTrigger > lTrigger) {
            if(rTrigger < shooterLimit)
                shooter.shoot(rTrigger);
            else
                shooter.shoot(shooterLimit);
            arduino.changeLed(false);
        }
        else if(lTrigger > rTrigger)
            if(lTrigger < shooterLimit)
                shooter.shoot(lTrigger);
            else
                shooter.shoot(shooterLimit);
            arduino.changeLed(true);
        
        //Indexer-- literally no idea how they want to control indexing
        if(isIntaking && ballManager.getNumberOfBalls() == 0) {
            indexer.suckUp();
        }
        if(ballManager.getFirstPositionBall() && !ballManager.getSecondPositionBall()) {
            if(indexerTimeSave == 0.0) 
                indexerTimeSave = timer.get();
            //5.0 is amount of time indexer runs
            if(timer.get() - indexerTimeSave < 2600) {
                indexer.suckUp();
            } else {
                //Added this to reset timer after indexer runs
                indexerTimeSave = 0.0;
                ballManager.cycleBall();
            }
            
                
        }
        //TODO: this list i guess
        //Current issues i havent bothered to fix rn: indexerTimeSave doesn't get reset to 0, time is
        //in milliseconds i think, we have it set to 5, i dont think 5 milliseconds is enough..., we dont
        //call cycle ball, so ballManager won't update

        //I made this and then realised it doesn't work but i dont have time to fix it rn, so yeah
        if(ballManager.getFirstPositionBall() && ballManager.getSecondPositionBall()) {
            if(Controls.getLeftControllerBumper()) {
                
            }
            if(indexerTimeSave == 0.0) 
                    indexerTimeSave = timer.get();
                if(timer.get() - indexerTimeSave < 5.0)
                    indexer.suckUp();
        }


        //BeamBreak- adds a ball to ballManager when it sees a new one
        if(beamBreak.isBall() && !sameBall) {
            ballManager.newBall();
            sameBall = true;
        } 
        if(!beamBreak.isBall()) {
            sameBall = false;
        }
    

        //Intake- controls sucking in balls and moving intake up and down
        if(ballManager.getNumberOfBalls() < 2 && Controls.getRightStickTop()) {
            intake.suck(Controls.getRightStickTop());
            isIntaking = true;
        } else {
            isIntaking = false;
        }
        //controls intake cylinder
        if(Controls.getRightStickBottom())
            intake.moveIntake();
        
        
        //Moves cylinder for indexing/shooting
        if(Controls.getLeftControllerBumper())
            indexer.shoveBall();

        //Climber -- Y = arm goes up, A = arm goes down, RightBumper = move cylinder
        if(Controls.getControllerY()) {
            climber.extendArm();
        } else if(Controls.getControllerA()) {
            climber.retractArm();
        }
        if(Controls.getRightControllerBumper()) {
            climber.movePiston();
        }

        //Prints speed and limelight distance, this is something that will eventually only be shown in shuffleboard, I just want to test it.
        System.out.println("Speed: " + navX.getSpeed() + "\tLLDistance: " + limeLight.getRightDistance());


    }


}
