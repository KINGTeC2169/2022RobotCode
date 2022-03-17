package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.CompressorTank;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShuffleboardManager;
import frc.robot.utils.Constants;
import frc.robot.utils.Controls;
import frc.robot.utils.MathDoer;
import frc.robot.utils.PID;
//Hello
public class DriveCommand extends CommandBase {
    private Timer timer = new Timer();
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
    private ColorSensor colorSensor;
    private ShuffleboardManager shuffleboard;

    private double leftY;
    private double rightX;
    private double rightTwist;
    private double quickStopAcummolatss;
    private boolean isIntaking;
    private boolean sameBall;
    private boolean isManualLimeLight = true;
    private boolean isManualBalls = true;
    private double desiredRPM;
    private boolean climbingTime;
    private boolean weGotA2319 = true;
    private boolean goingUp;
    private boolean goingDown;
    private boolean shooting;
    //I hate these scuffed boolean methods

    PID limeDrive = new PID(.05, 0.00005, .005);
 
    //Adds all subsystems to the driving command
    public DriveCommand(DriveTrain driveTrain, Arduino arduino, Shooter shooter, Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navX, BallManager ballManager, BeamBreak beamBreak, ColorSensor colorSensor, ShuffleboardManager shuffleboard) {
        timer.start();
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.arduino = arduino;
        addRequirements(arduino);
        this.shooter = shooter;
        addRequirements(shooter);
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
        this.colorSensor = colorSensor;
        addRequirements(colorSensor);
        this.shuffleboard = shuffleboard;
        addRequirements(shuffleboard);
    }

    @Override
    public void execute() {
        //CompressorTank.disable();
        //Parental controls
        //isManualBalls = true;
        //isManualLimeLight = true;

        //Manual Mode Manager - basically turns manual modes on and off
        
        if(Controls.getLeftControllerStick()) {
            if(isManualBalls) {
                isManualBalls = false;
                ballManager.reset();
            } else {
                isManualBalls = true;
            }
        }
        if(Controls.getRightControllerStick()) {
            if(isManualLimeLight) {
                isManualLimeLight = false;
            } else {
                isManualLimeLight = true;
            }
        }

        
        
        
        /*--------------------------------Driving------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
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
        /*
        if(Controls.getLeftStickBottom()) {
            if(limeLight.getRightXPercent() != 0 || limeLight.getLeftXPercent() != 0) {
                rightPower -= limeLight.getRightXPercent() / 50;
                leftPower += limeLight.getRightXPercent() / 50;
                rightPower -= limeLight.getLeftXPercent() / 50;
                leftPower += limeLight.getLeftXPercent() / 50;
            }
        }
        */
        if(Controls.getLeftStickBottom()) {
            limeDrive.setSetpoint(0);
            limeDrive.calculate(limeLight.getRightXPercent() + limeLight.getLeftXPercent());
            rightPower += limeDrive.getOutput();
            leftPower -= limeDrive.getOutput();
            //TODO: this is an experiment, probably wont work
            driveTrain.rampOn();
        }
        else {
            driveTrain.rampOff();
        }
         //applies the powers to the motors
         driveTrain.lDrive(leftPower);
         driveTrain.rDrive(rightPower);
 
         if(Controls.getLeftStickTopPressed())
             driveTrain.shiftThatDog();
        

        /*--------------------------------Shooting------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        --------------------------------------------240,000--------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
        //ledManager
        if(shooter.isShootingLeft()) {
            arduino.rightOn();
        } else {
            arduino.rightOff();
        }
        if(shooter.isShootingRight()) {
            arduino.leftOn();
        } else {
            arduino.leftOff();
        }
        //Shooter- it shoots.
        double rTrigger = Controls.getRightControllerTrigger();
        double lTrigger = Controls.getLeftControllerTrigger();
        if(!isManualLimeLight) {
            


            //Shoots based on which trigger is pressed, one set of LEDs is set up
            if(limeLight.rpm() != 0) {
                desiredRPM = limeLight.rpm();
            }
            else {
                //Error: limelight malfunction- shoot from edge of tarmac
                desiredRPM = 3600;
            }


            if(rTrigger > lTrigger) {
                if(colorSensor.isEnemyColor())
                    shooter.shoot(-rTrigger);
                else
                    shooter.setCoolerestRPM(-desiredRPM);
            }
            else if(lTrigger > rTrigger) {
                if(colorSensor.isEnemyColor())
                    shooter.shoot(lTrigger);
                else {
                    shooter.setCoolerestRPM(desiredRPM);
                }

            } else {
                shooter.stopShooter();
                CompressorTank.enable();
            }

        } else {


            if(rTrigger > lTrigger) {
                shooter.shoot(-rTrigger);
                //shooter.setCoolerestRPM(-3600);
            } else if(rTrigger < lTrigger) {
                shooter.shoot(lTrigger);
                //shooter.setCoolerestRPM(3600);
            } else {
                CompressorTank.enable();
                shooter.stopShooter();
            }
    }
              
        

/*--------------------------------Indexing------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
        //BeamBreak- adds a ball to ballManager when it sees a new one
        if(beamBreak.isBall() && !sameBall) {
            ballManager.newBall();
            sameBall = true;
        } 
        if(!beamBreak.isBall()) {
            sameBall = false;
        }
        if(isManualBalls) {
            if(Controls.getRightControllerBumper()) {
                indexer.suckUp(Controls.getRightControllerBumper());
                //TODO: wtf do we do this??
                //arduino.changeLed(false);
            } else {
                indexer.reverseSuckUp(Controls.getRightStickBottom());
                //arduino.changeLed(true);
            }
            
            if(Controls.getLeftControllerBumperPressed() || indexer.isShoveBallRunning()) {
                indexer.shoveBall();
            }
            


        } else {

             //Indexer
             if(isIntaking && ballManager.getNumberOfBalls() == 0 || (ballManager.getSecondPositionBall() && !ballManager.getFirstPositionBall()) && isIntaking) {
                indexer.suckUp(true);
            } else {
                indexer.suckUp(false);
            }
            if(ballManager.getFirstPositionBall() && !ballManager.getSecondPositionBall()) {
                if(indexerTimeSave == 0.0) 
                    indexerTimeSave = timer.get();
                //5.0 is amount of time indexer runs
                if(timer.get() - indexerTimeSave < 2) {
                    indexer.suckUp(true);
                } else {
                    //Added this to reset timer after indexer runs
                    indexer.suckUp(false);
                    indexerTimeSave = 0.0;
                    ballManager.cycleBall();
                }  
            }

            //Moves cylinder for indexing/shooting
            if(Controls.getLeftControllerBumper() || indexer.isShoveBallRunning()) {
                    indexer.shoveBall();
                    ballManager.shootBall();
            }

        }
    

        


        /*--------------------------------Intake------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
        if(Controls.getDPad() == 90)
            intake.up();
        else if(Controls.getDPad() == 270)
            intake.down();
        else {
            intake.off();
        }
        if(isManualBalls) {
            if(!Controls.getRightStickBottom()) {
            intake.suck(Controls.getRightStickTop());
            } else {
            intake.reverseSuck(Controls.getRightStickBottom());
            }

        } else {

            if(ballManager.getNumberOfBalls() < 2) {
                intake.suck(Controls.getRightStickTop());
                isIntaking = Controls.getRightStickTop();
            } else {
                intake.suck(false);
                isIntaking = false;
            }
        }
        
        
        
        


/*--------------------------------Climber------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/
        //Climber -- Y = arm goes up, A = arm goes down, RightBumper = move cylinder 


        
        if(weGotA2319) {
            if(climber.getCurrent() < 8) {
                climber.retractArmSlow();
            }
            else {
                weGotA2319 = false;
                climber.stopArm();
                climber.setZero();
            }
        } else {/*else {
        
            if(Controls.getControllerYPressed() && !goingDown) {
                goingUp = true;
                goingDown = false;
            }
            else if(Controls.getControllerAPressed() && !goingUp) {
                goingDown = true;
                goingUp = false;
            }

            if(goingUp && !climber.isTop()) {
                climber.extendArm();
                System.out.println("balls");
            }
            else {
                climber.stopArm();
                goingUp = false;
            }


            if(goingDown && !climber.isBottom()) {
                climber.retractArm();
                System.out.println("balls 2");
            }
            else {
                climber.stopArm();
                goingDown = false;
            }
*/

            
            if(Controls.getControllerA() && !climber.isBottom()) {
                climber.retractArm();
            }
            else if (Controls.getControllerY() && !climber.isTop()) {
                climber.extendArm();
            }
            else 
                climber.stopArm();
            
        }

        
        if(Controls.getDPad() == 180)
            climber.retractArmSlow();
        
        if(Controls.getDPad() == 0)
            climber.extendArmSlow();
        
        if(Controls.babyBackRibs())
            climber.setZero();
        
        

        /*
        if(Controls.getRightControllerTrigger() > Controls.getLeftControllerTrigger()) {
            climber.extendArmTrigger(Controls.getRightControllerTrigger());
        }
        else if(Controls.getLeftControllerTrigger() > Controls.getRightControllerTrigger()) {
            climber.reverseArmTrigger(Controls.getLeftControllerTrigger());
        }   
        */
        if(!climbingTime) {
            if(Controls.getControllerX()) {
                climber.movePistonDown();
                climbingTime = true;
            }
            else {
                climber.movePistonUp();
            }
        }
        else {
            if(Controls.getControllerX()) {
                climber.movePistonDown();
            }
            else if(Controls.getControllerB()) {
                climber.movePistonUp();
            }
            else {
                climber.pistonOff();
            }
        }

            
        if(Controls.startYourEngines()) {
            climber.toggLock();
        }
        
        

        


/*--------------------------------ShuffleBoard------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------
        ------------------------------------------Just wanted to break it up a little more- *just a little*-------------------
        ----------------------------------------------------------------------------------------------------------------------
        ----------------------------------------------------------------------------------------------------------------------*/


        shuffleboard.boolInABox("POS: 1", ballManager.getFirstPositionBall());
        shuffleboard.boolInABox("POS: 2", ballManager.getSecondPositionBall());
        shuffleboard.boolInABox("Manual Balls", isManualBalls);
        shuffleboard.boolInABox("Manual LimeLight", isManualLimeLight);
        shuffleboard.boolInABox("Is High Gear", !driveTrain.dogStatus());
        shuffleboard.number("Right Distance", limeLight.getRightDistance());
        shuffleboard.number("Left Distance", limeLight.getLeftDistance());
        shuffleboard.number("Shooter RPM", shooter.getRPM());
        shuffleboard.number("Shooter RPM again", shooter.getRPM());
        shuffleboard.boolInABox("BeamBreak", beamBreak.isBall());
        //shuffleboard.number("Shooter Percent Output", (lTrigger > rTrigger ? lTrigger : rTrigger));
        //shuffleboard.number("Shooter Current", shooter.getCurrent());
        shuffleboard.number("Shooter Voltage", shooter.getVoltage());
        shuffleboard.number("LimeLight RPM", desiredRPM);
        shuffleboard.number("Climber Sensor", climber.getSensorPos());
        shuffleboard.number("Climber Current", climber.getCurrent());
        shuffleboard.number("Stator Current", climber.getStatorCurrent());
        shuffleboard.boolInABox("Is Enemy ball", colorSensor.isEnemyColor());
        shuffleboard.boolInABox("Red", colorSensor.isRed());
        shuffleboard.boolInABox("Blue", colorSensor.isBlue());


    }

}


