package frc.robot;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command teleOp;
  private Command autoCommand;
  private Command testCommand;
  private RobotContainer m_robotContainer;
  private SendableChooser autoChooser;

  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    

    CameraServer.startAutomaticCapture(0).setFPS(30);
    //CameraServer.startAutomaticCapture(1).setVideoMode(PixelFormat.kMJPEG, 640, 320, 30);
    CameraServer.startAutomaticCapture(1).setFPS(30);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    //DON'T RUN ANY COMMANDS HERE OR I WILL EAT YOU
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    m_robotContainer.Die().schedule();

  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_robotContainer.makeAuto();
   
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    autoCommand = m_robotContainer.getAutoCommand();

    if(autoCommand != null)
      autoCommand.schedule(false);
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    m_robotContainer.makeTeleOp();
    //m_robotContainer.getInitCommand().schedule(false);

  
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    teleOp = m_robotContainer.getTeleopCommand();

    if(teleOp != null)
      teleOp.schedule();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    //CommandScheduler.getInstance().cancelAll();
    //m_robotContainer.makeTest();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    testCommand = m_robotContainer.getTestCommand();
    System.out.println(testCommand.toString());
    testCommand.schedule();
    //System.out.println("testinggggg");
  }
}
