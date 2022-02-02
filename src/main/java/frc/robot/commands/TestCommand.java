package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.Controls;

public class TestCommand extends CommandBase {

    public void execute() {
        System.out.println(Controls.getLeftStickTop());
    }
    
}
