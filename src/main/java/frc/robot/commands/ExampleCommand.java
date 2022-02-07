package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.NavX;

public class ExampleCommand extends CommandBase {
    private NavX navX;
    public  ExampleCommand(NavX subsystem) {
        navX = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        System.out.println(navX.getAngle());
        
    }
}
