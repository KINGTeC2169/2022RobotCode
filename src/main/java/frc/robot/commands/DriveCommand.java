package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.DriveTrain;
import frc.robot.utils.Constants;
import frc.robot.utils.Controls;
import frc.robot.utils.MathDoer;

public class DriveCommand extends CommandBase {
    
    private DriveTrain driveTrain;
    private Arduino arduino;

    private double leftY;
    private double rightX;
    private double rightTwist;
    private double quickStopAcummolatss;
   

    public DriveCommand(DriveTrain subsystem, Arduino subsystem2) {
        driveTrain = subsystem;
        addRequirements(subsystem);
        arduino = subsystem2;
        addRequirements(subsystem2);
    }

    @Override
    public void execute() {
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

            //checks if your spinning in place
            if(Math.abs(leftY) < .2) {
                //dont worry about it. I have no idea
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

       //makes the motors turn
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
        


        if(Controls.getControllerA())
            arduino.changeLed(true);
        else   
            arduino.changeLed(false);

        //applies the powers to the motors
        driveTrain.lDrive(leftPower);
        driveTrain.rDrive(rightPower);

    }


}
