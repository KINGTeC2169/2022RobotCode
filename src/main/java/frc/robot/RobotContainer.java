package frc.robot;


import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autoCommands.Autonomous;
import frc.robot.autoCommands.AutonomousButDumb;
import frc.robot.autoCommands.FourBallAuto;
import frc.robot.commands.Death;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.RobotInit;
import frc.robot.commands.ShuffleData;
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
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;


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
    private SendableChooser autoChooser = new SendableChooser<String>();
  // The robot's subsystems and commands are defined here...

  //Makes TeleOp command with all subsystems needed for teleOp
  private DriveCommand m_teleopCommand;
  private Command m_autoCommand;
  private ShuffleData m_shuffleData;
  private Death m_death;
  private final TestingCommand m_testCommand = new TestingCommand(driveTrain, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard);
  private final RobotInit m_InitCommand = new RobotInit(ballManager);

  public RobotContainer() {
    m_shuffleData = new ShuffleData(driveTrain, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard, jacobSensor);
    m_death = new Death(driveTrain, shooter, intake, indexer);
    autoChooser.setDefaultOption("Normal", "Normal");
    autoChooser.addOption("Dumb Wall Mode", "Dumb");
    autoChooser.addOption("Four Ball Auto?", "Four");
    SmartDashboard.putData(autoChooser);
  }


  public void makeTeleOp() {
    m_teleopCommand = new DriveCommand(driveTrain, arduino, shooter, intake, indexer, climber, limeLight, navX, ballManager, beamBreak, colorSensor, shuffleboard, jacobSensor);
  }

  public void makeAuto() {
    if(autoChooser.getSelected().equals("Normal")) {
      m_autoCommand = new Autonomous(driveTrain, shooter, navX, indexer, climber, intake, vision, ballManager, limeLight, beamBreak, shuffleboard);
    } else if(autoChooser.getSelected().equals("Dumb")) {
      m_autoCommand = new AutonomousButDumb(driveTrain, shooter, navX, indexer, climber, intake, vision, ballManager, limeLight, beamBreak, shuffleboard);
    } else if(autoChooser.getSelected().equals("Four")) {
      m_autoCommand = new FourBallAuto(driveTrain, shooter, navX, indexer, climber, intake, vision, ballManager, limeLight, beamBreak, shuffleboard);
    }
   
  }

  //public void makeTest() {
    //m_testCommand;
  //}

  public Command getTeleopCommand() {
    return m_teleopCommand;
  }
  public Command getInitCommand() {
    return m_InitCommand;
  }
  public Command getTestCommand() {
    //System.out.println("Death");
    return m_testCommand;
  }
  public Command getAutoCommand() {
      return m_autoCommand;
  }

  public Command getShuffleData() {
    return m_shuffleData;
  }

  public CommandBase Die() {
    return m_death;
  }
}
