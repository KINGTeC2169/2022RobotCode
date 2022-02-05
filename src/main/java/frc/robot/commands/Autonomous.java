package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends CommandBase {
    
    private DriveTrain driveTrain;
    private Shooter shooter;

    private Timer timer;

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

        //Shoot:

        //Drive:
    }
}
