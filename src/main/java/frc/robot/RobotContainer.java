package frc.robot;

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
import frc.robot.subsystems.Shuffleboard;
import frc.robot.subsystems.Testing;
import edu.wpi.first.wpilibj2.command.Command;


public class RobotContainer {
  private DriveTrain driveTrain = new DriveTrain();
    private Arduino arduino = new Arduino();
    private Shooter shooter = new Shooter();
    private Intake intake = new Intake();
    private Indexer indexer = new Indexer();
    private Climber climber = new Climber();
    private LimeLight limeLight = new LimeLight();
    private NavX navX = new NavX();
    private BallManager ballManager = new BallManager();
    private BeamBreak beamBreak = new BeamBreak();
    private ColorSensor colorSensor = new ColorSensor();
    private Shuffleboard shuffleboard = new Shuffleboard();
    //private Testing testing = new Testing();
  // The robot's subsystems and commands are defined here...

  //Makes TeleOp command with all subsystems needed for teleOp
  private final DriveCommand m_teleopCommand = new DriveCommand(driveTrain, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard);
  private final Autonomous m_autoCommand = new Autonomous(driveTrain, shooter, navX, indexer);
  //private final TestingCommand m_testCommand = new TestingCommand(testing, shooter, navX);
  private final RobotInit m_InitCommand = new RobotInit(climber, indexer, intake);

  public RobotContainer() {
    
  }

  //public Command getAutonomousCommand() {
    //return m_autoCommand;
  //}
  public Command getTeleopCommand() {
    return m_teleopCommand;
  }
  public Command getInitCommand() {
    return m_InitCommand;
  }
  //public Command getTestCommand() {
    //return m_testCommand;
  //}
  public Command getAutoCommand() {
      return m_autoCommand;
  }
}
