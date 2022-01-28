package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LimeLight;

public class LLDistanceCommand extends CommandBase {
    private LimeLight limeLight;
    public  LLDistanceCommand(LimeLight subsystem) {
       limeLight = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        System.out.println((65) / Math.tan(((45+limeLight.getYPercent()) * Math.PI)/180));
    }
}
