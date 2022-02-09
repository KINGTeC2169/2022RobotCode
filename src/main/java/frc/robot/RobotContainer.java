package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.Autonomous;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.RobotInit;
import frc.robot.commands.TestingCommand;
import frc.robot.subsystems.Arduino;
import frc.robot.subsystems.BallManager;
import frc.robot.subsystems.BeamBreak;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Testing;
import edu.wpi.first.wpilibj2.command.Command;


public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //Makes TeleOp command with all subsystems needed for teleOp
  private final DriveCommand m_teleopCommand = new DriveCommand(new DriveTrain(), new Arduino(), new Shooter(), new Intake(), new Indexer(), new Climber(), new LimeLight(), new NavX(), new BallManager(), new BeamBreak(), new ColorSensor());
  private final Autonomous m_autoCommand = new Autonomous(new DriveTrain(), new Shooter(), new NavX());
  private final TestingCommand m_testCommand = new TestingCommand(new Testing(), new Shooter(), new NavX());
  private final RobotInit m_InitCommand = new RobotInit(new Climber(), new Indexer(), new Intake());

  public RobotContainer() {
    
  }

  public Command getAutonomousCommand() {
    return m_autoCommand;
  }
  public Command getTeleopCommand() {
    return m_teleopCommand;
  }
  public Command getInitCommand() {
    return m_InitCommand;
  }
}
