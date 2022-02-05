package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Testing;
import frc.robot.utils.Controls;

public class TestingCommand extends CommandBase {

    private Testing testing;
    private Shooter shooter;

    public TestingCommand(Testing testing, Shooter shooter) {
        this.testing = testing;
        addRequirements(testing);
        this.shooter = shooter;
        addRequirements(shooter);
    }
    
    @Override
    public void execute() {
        if(Controls.getLeftControllerBumper())
            shooter.setRPM(500);
        if(Controls.getControllerA())
            testing.extend();
        else if(Controls.getControllerB())
            testing.retract();


    }
}
