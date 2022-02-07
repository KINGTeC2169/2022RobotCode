package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Testing;
import frc.robot.utils.Controls;

public class TestingCommand extends CommandBase {

    private Testing testing;
    private Shooter shooter;
    private NavX navx;
    private Timer timer = new Timer();
    private double saveTime;
    Field2d feildthing = new Field2d();
        

    public TestingCommand(Testing testing, Shooter shooter, NavX navx) {
        this.testing = testing;
        addRequirements(testing);
        this.shooter = shooter;
        addRequirements(shooter);
        this.navx = navx;
        addRequirements(navx);
        SmartDashboard.putData("field", feildthing);
    }
    
    @Override
    public void execute() {

        /*if(Controls.getLeftControllerBumper())
            shooter.setRPM(500);
        if(Controls.getControllerA())
            testing.extend();
        else if(Controls.getControllerB())
            testing.retract();
        */

        /*System.out.println("balls");
        if(Controls.getControllerXPressed()) {
            saveTime = timer.get();
            testing.singleOn();
        }
        if(saveTime != 0.0) {
            if(timer.get() > saveTime + 500)
                testing.singleOff();
        }*/

        feildthing.setRobotPose(navx.getDisplacmentX(), navx.getDisplacmentY(), navx.getRotation());
        
    }
}
