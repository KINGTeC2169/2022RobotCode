package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
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
import frc.robot.subsystems.ShuffleboardManager;
import frc.robot.subsystems.Testing;

public class TestingCommand extends CommandBase {

    private DriveTrain driveTrain;
    private Testing testing;
    private Arduino arduino;
    private Shooter shooter;
    private Intake intake;
    private Indexer indexer;
    private Climber climber;
    private LimeLight limeLight;
    private NavX navx;
    private BallManager ballManager;
    private BeamBreak beamBreak;
    private ColorSensor colorSensor;
    private ShuffleboardManager shuffleboard;
    private Timer timer = new Timer();
        

    public TestingCommand(DriveTrain driveTrain, Testing testing, Arduino arduino, Shooter shooter, Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navx, BallManager ballManager, BeamBreak beamBreak, ColorSensor colorSensor, ShuffleboardManager shuffleboardManager) {
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
        this.testing = testing;
        addRequirements(testing);
        this.arduino = arduino;
        addRequirements(arduino);
        this.shooter = shooter;
        addRequirements(shooter);
        this.intake = intake;
        addRequirements(intake);
        this.indexer = indexer;
        addRequirements(indexer);
        this.climber = climber;
        addRequirements(climber);
        this.limeLight = limeLight;
        addRequirements(limeLight);
        this.navx = navx;
        addRequirements(navx);
        this.ballManager = ballManager;
        addRequirements(ballManager);
        this.beamBreak = beamBreak;
        addRequirements(beamBreak);
        this.colorSensor = colorSensor;
        addRequirements(colorSensor);
        this.shuffleboard = shuffleboardManager;
        addRequirements(shuffleboardManager);
    }
    
    @Override
    public void execute() {
        timer.start();
        double time = timer.get();
        if(time < 3) {
            driveTrain.rDrive(.5);
            driveTrain.lDrive(.5);
        }
        else if(time < 6) {
            driveTrain.rDrive(0);
            driveTrain.lDrive(0);
            
        }
        else if(time < 9) {

        }
        
        
    }
}
