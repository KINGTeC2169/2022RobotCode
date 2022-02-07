package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends CommandBase {
    
    private DriveTrain driveTrain;
    private Shooter shooter;

    private Timer timer = new Timer();
    private int counter;

    public Autonomous(DriveTrain driveTrain, Shooter shooter) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        //"Your code goes here" - CodeHS

        //Auto plan: shoot then drive backwards
        //Yeah thats it for now

        //Initialization (dont know if we need this or not, i just added it anyways)
        if(counter == 0) {

            counter++;
        }

        //Shoot:
        if(counter == 1) {

            counter++;
        }
        //Drive:
        if(counter == 2) {
            driveTrain.driveFor(24, 0.5);
            counter++;
        }

    }
}
