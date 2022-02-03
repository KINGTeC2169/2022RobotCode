package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorSensor;

public class BeamBreakSensorTestCommand extends CommandBase {

    private DigitalInput balls = new DigitalInput(0);

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        System.out.println(balls.get());
    }
}
