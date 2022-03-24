package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autoCommands.Autonomous;
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
import frc.robot.subsystems.JacobSensor;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShuffleboardManager;
import frc.robot.subsystems.Testing;
import frc.robot.subsystems.Vision;
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
    //private Testing testing = new Testing();
    private ShuffleboardManager shuffleboard = new ShuffleboardManager();
    private JacobSensor jacobSensor = new JacobSensor();
    private Vision vision = new Vision();
    //private Testing testing = new Testing();
  // The robot's subsystems and commands are defined here...

  //Makes TeleOp command with all subsystems needed for teleOp
  private DriveCommand m_teleopCommand;
  private final Autonomous m_autoCommand = new Autonomous(driveTrain, shooter, navX, indexer, climber, intake, vision, ballManager);
  //private final TestingCommand m_testCommand = new TestingCommand(driveTrain, testing, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard);
  private final RobotInit m_InitCommand = new RobotInit(climber, indexer, intake, ballManager, shuffleboard);

  public RobotContainer() {
    
  }


  public void makeTeleOp() {
    m_teleopCommand = new DriveCommand(driveTrain, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard, jacobSensor);
  }

  public Command getTeleopCommand() {
    return m_teleopCommand;
  }
  public Command getInitCommand() {
    return m_InitCommand;
  }
  //public Command getTestCommand() {
    //return m_testCommand;
  //}
  //public Command getAutoCommand() {
  //    return m_autoCommand;
  //}
}
