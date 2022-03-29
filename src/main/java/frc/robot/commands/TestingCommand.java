package frc.robot.commands;


import edu.wpi.first.wpilibj.Timer;
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

public class TestingCommand extends CommandBase {

    private DriveTrain driveTrain;
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
    private String test;
        

    public TestingCommand(DriveTrain driveTrain, Arduino arduino, Shooter shooter, Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navx, BallManager ballManager, BeamBreak beamBreak, ColorSensor colorSensor, ShuffleboardManager shuffleboardManager) {
        System.out.println("balls 2");
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
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
        System.out.println("balls");
        timer.start();
        double time = timer.get();
        if(time < 3) {
            driveTrain.rDrive(.5);
            driveTrain.lDrive(.5);
            test = "Drivetrain";
        }
        else if(time < 6) {
            driveTrain.stop();
            intake.down();
            intake.suck(true);
            indexer.suckUp(true);
            test = "indexer/intake";
        }
        else if(time < 9) {
            intake.suck(false);
            indexer.suckUp(false);
            intake.up();
            test = "intake pneumatics";
        }
        else if(time < 12) {
            shooter.setCoolerestRPM(1000);
            indexer.up();
            test = "shooter/feeder";
        }
        else if(time < 14) {
            shooter.stopShooter();
            indexer.down();
        }
        else if(time < 16) {
            climber.extendArmSlow();
            test = "climber arms";
        }
        else if(time < 18) {
            climber.retractArmSlow();
        }
        else if(time < 19) {
            climber.movePistonDown();
            test = "climber pneumatics";
        }
        else if(time < 20) {
            climber.movePistonUp();
        }
        else {
            timer.stop();
        }
        shuffleboard.text("Currently Testing", test);
        
    }
}
