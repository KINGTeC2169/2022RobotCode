package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Testing;
import frc.robot.utils.Controls;

public class TestingCommand extends CommandBase {

    private Testing testing;

    public TestingCommand(Testing testing) {
        this.testing = testing;
        addRequirements(testing);
    }
    
    @Override
    public void execute() {
        if(Controls.getControllerA())
            testing.extend();
        else if(Controls.getControllerB())
            testing.retract();




    }
}
