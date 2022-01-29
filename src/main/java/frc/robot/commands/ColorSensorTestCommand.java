package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorSensor;

public class ColorSensorTestCommand extends CommandBase {
    private ColorSensor sensor;
    public  ColorSensorTestCommand(ColorSensor subsystem) {
        sensor = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        System.out.println(sensor.num());
    }
}
