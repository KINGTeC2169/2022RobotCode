package frc.robot.commands;
import java.util.*;


import edu.wpi.first.math.controller.PIDController;
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
import frc.robot.subsystems.JacobSensor;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShuffleboardManager;
import frc.robot.utils.Constants;
import frc.robot.utils.Controls;
import frc.robot.utils.MathDoer;
import frc.robot.utils.PID;

public class DriveCommand extends CommandBase {
    private Timer timer = new Timer();
    
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
    private JacobSensor jacobSensor;

    private double setpoint;
    private double distance;
    private double leftY;
    private double rightX;
    private double rightTwist;
    private double quickStopAcummolatss;
    private boolean isIntaking;
    private boolean isManualLimeLight;
    private boolean isManualBalls;
    private double desiredRPM;
    private double lastKnownRPM;
    private boolean climbingTime;
    private boolean weGotA2319 = true;
    private boolean shootingRange = false;
    private double maxLeftVelo;
    private double maxRightVelo;
    //I hate these scuffed boolean methods

    ArrayList<Double> roborioSnack = new ArrayList<Double>();
    PID limeDrive = new PID(.05, 0.00005, .005);

    PIDController leftDrive = new PIDController(1, 0, 0);
    PIDController rightDrive = new PIDController(1, 0, 0);
 
    //Adds all subsystems to the driving command
    public DriveCommand(DriveTrain driveTrain, Arduino arduino, Shooter shooter, Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navX, BallManager ballManager, BeamBreak beamBreak, ColorSensor colorSensor, ShuffleboardManager shuffleboard, JacobSensor jacobSensor) {
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
        this.jacobSensor = jacobSensor;
        addRequirements(jacobSensor);
    }
    @Override
    public void initialize() {
        ballManager.reset();
        navX.reset();
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

        //This is a roborio snack. Run it during a match for a fun surprise
        /* 
        if(Controls.getControllerA()) {
            for(int i = 0; i < 10000000000L; i++) {
                roborioSnack.add(i * 0.4254);
            }
        }
        */
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
        distance = limeLight.getLeftDistance() + limeLight.getRightDistance();
        setpoint = driveTrain.getAngle(distance, LimeLight.getShotDuration(distance));
        //System.out.println(driveTrain.getSpeed());
        if(Controls.getLeftStickBottom()) {
            limeDrive.setSetpoint(0);
            //System.out.println(setpoint);
            limeDrive.calculate(limeLight.getRightXPercent() + limeLight.getLeftXPercent());
            rightPower += limeDrive.getOutput();
            leftPower -= limeDrive.getOutput();
            //driveTrain.rampOn();
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
                lastKnownRPM = limeLight.rpm();
            }
            else {
                //Error: limelight malfunction- shoot from edge of tarmac
                desiredRPM = lastKnownRPM;
            }


            if(rTrigger > lTrigger) {
                if(colorSensor.isEnemyColor() && ballManager.getSecondPositionBall())
                    shooter.setCoolerestRPM(-1500);
                else
                    shooter.setCoolerestRPM(-desiredRPM);
                    //System.out.println(desiredRPM);
            }
            else if(lTrigger > rTrigger) {
                if(colorSensor.isEnemyColor() && ballManager.getSecondPositionBall())
                    shooter.setCoolerestRPM(1500);
                else {
                    shooter.setCoolerestRPM(desiredRPM);
                    //System.out.println("Left");
                }

            } else {
                shooter.stopShooter();
                //System.out.println("Stopping");
                CompressorTank.enable();
            }
            if(Math.abs(Math.abs(shooter.getRPM()) - Math.abs(desiredRPM)) < 75) {
                shootingRange = true;
            } else {
                shootingRange = false;
            }

        } else {


            if(Controls.babyBackRibs() && lTrigger > rTrigger) {
                //System.out.println("aaaAAAAA");
                shooter.setCoolerestRPM(3950);
            } else if (Controls.babyBackRibs() && rTrigger > lTrigger) {
                shooter.setCoolerestRPM(-3750);
            } else if(rTrigger > lTrigger) {
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
        if(beamBreak.isBall()) {
            ballManager.setFirstPositionBall(true);

        } else {
            ballManager.setFirstPositionBall(false);
        }
        if(colorSensor.isBall()) {
            ballManager.setSecondPositionBall(true);
        } else {
            ballManager.setSecondPositionBall(false);
        }
        if(isManualBalls) {
            if(Controls.getRightControllerBumper()) {
                indexer.suckUp(Controls.getRightControllerBumper());
                //TODO: wtf do we do this?? get better :)
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
                indexer.suckUp(true);
            } 
            
            /*
            if(Controls.getRightStickBottom() && (ballManager.getNumberOfBalls() == 2 || ballManager.getNumberOfBalls() == 0)) {
                indexer.reverseSuckUp(true);
                ballManager.setFirstPositionBall(false);
            }
            else 
                indexer.reverseSuckUp(false);
                */

            //Moves cylinder for indexing/shooting
            if((Controls.getLeftControllerBumper() /*&& !indexer.isSuckingUp()*/) || indexer.isShoveBallRunning()) {
                indexer.shoveBall();
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
            if(Controls.getRightStickBottom()) {
                intake.reverseSuck(true);
            }
            else {
                if(ballManager.getNumberOfBalls() < 2) {
                    intake.suck(Controls.getRightStickTop());
                    isIntaking = Controls.getRightStickTop();
                } else {
                    intake.suck(false);
                    isIntaking = false;
                }
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

        
        if(weGotA2319) {
            if(climber.getCurrent() < 8) {
                climber.retractArmSlow();
            }
            else {
                weGotA2319 = false;
                climber.stopArm();
                climber.setZero();
            }
        } else {
            
            if(Controls.getControllerA() && !climber.isBottom()) {
                climber.retractArm();
            }
            else if (Controls.getControllerY() && !climber.isTop()) {
                climber.extendArm();
            }
            else 
                climber.stopArm();
        }

        /*
        if(Controls.getControllerAPressed()) {
            limeLight.changeRPM(-50);
        }

        if(Controls.getControllerYPressed()) {
            limeLight.changeRPM(50);
        }    
        */

        if(Controls.getDPad() == 180)
            climber.retractArmSlow();
        
        if(Controls.getDPad() == 0)
            climber.extendArmSlow();
        
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

        if(Controls.getLeftControllerTrigger() == 0 && Controls.getRightControllerTrigger() == 0) {
            if(climber.lockStatus()) {
                arduino.on();
            }
            else {
                arduino.off();
            }
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
        shuffleboard.number("LimeLight RPM", desiredRPM);
        shuffleboard.number("Climber Sensor", climber.getSensorPos());
        shuffleboard.boolInABox("Is Enemy ball", colorSensor.isEnemyColor());
        shuffleboard.boolInABox("Red", colorSensor.isRed());
        shuffleboard.boolInABox("Blue", colorSensor.isBlue());
        shuffleboard.boolInABox("Ratchet", climber.ratchetStatus());
        //shuffleboard.number("RPM adjustmeny", limeLight.getRPMAdjusted());\
        shuffleboard.boolInABox("Sees R", limeLight.getRightXPercent() > 0);
        shuffleboard.boolInABox("Sees L", limeLight.getLeftXPercent() > 0);
        shuffleboard.boolInABox("isBallInShooter", colorSensor.isBall());
        shuffleboard.boolInABox("Is ready to shoot", shootingRange);
        if(maxRightVelo < driveTrain.getRightVelocity()) {
            maxRightVelo = driveTrain.getRightVelocity();
        }
        if(maxLeftVelo < driveTrain.getLeftVelocity()) {
            maxLeftVelo = driveTrain.getLeftVelocity();
        }
        shuffleboard.number("Left Velocity", LimeLight.getShotDuration(limeLight.getLeftDistance()));
        shuffleboard.number("Right Velocity", LimeLight.getShotDuration(limeLight.getRightDistance()));
        shuffleboard.number("NavX", navX.getAngle());
    }

    @Override
    public void end(boolean interrupted) {
        /* Test this if death doesn't work
        if(interrupted) {
            driveTrain.stop();
            shooter.stopShooter();
            intake.suck(false);
            indexer.suckUp(false);
        }
        */
    }
}


