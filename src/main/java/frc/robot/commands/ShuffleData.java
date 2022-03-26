package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
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
import frc.robot.utils.Controls;

public class ShuffleData extends CommandBase{
    private DriveTrain driveTrain;
    private Arduino arduino;
    private Shooter shooter;
    private Intake intake;
    private Indexer indexer;
    private Climber climber;
    private LimeLight limeLight;
    private NavX navX;
    private BallManager ballManager;
    private BeamBreak beamBreak;
    private ColorSensor colorSensor;
    private ShuffleboardManager shuffleboard;
    private JacobSensor jacobSensor;

    public ShuffleData(DriveTrain driveTrain, Arduino arduino, Shooter shooter, 
    Intake intake, Indexer indexer, Climber climber, LimeLight limeLight, NavX navX, 
    BallManager ballManager, BeamBreak beamBreak, ColorSensor colorSensor, 
    ShuffleboardManager shuffleboard, JacobSensor jacobSensor) {

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
        this.navX = navX;
        addRequirements(navX);
        this.ballManager = ballManager;
        addRequirements(ballManager);
        this.beamBreak = beamBreak;
        addRequirements(beamBreak);
        this.colorSensor = colorSensor;
        addRequirements(colorSensor);
        this.shuffleboard = shuffleboard;
        addRequirements(shuffleboard);
        this.jacobSensor = jacobSensor;
        addRequirements(jacobSensor);
    }

    @Override
    public void execute() {
        shuffleboard.boolInABox("POS: 1", ballManager.getFirstPositionBall());
        shuffleboard.boolInABox("POS: 2", ballManager.getSecondPositionBall());
        //shuffleboard.boolInABox("Manual Balls", isManualBalls);
        //shuffleboard.boolInABox("Manual LimeLight", isManualLimeLight);
        shuffleboard.boolInABox("Is High Gear", !driveTrain.dogStatus());
        shuffleboard.number("Right Distance", limeLight.getRightDistance());
        shuffleboard.number("Left Distance", limeLight.getLeftDistance());
        shuffleboard.number("Shooter RPM", shooter.getRPM());
        shuffleboard.number("Shooter RPM again", shooter.getRPM());
        shuffleboard.boolInABox("BeamBreak", beamBreak.isBall());
        shuffleboard.number("Shooter Voltage", shooter.getVoltage());
        //shuffleboard.number("LimeLight RPM", desiredRPM);
        shuffleboard.number("Climber Sensor", climber.getSensorPos());
        shuffleboard.number("Climber Current", climber.getCurrent());
        shuffleboard.boolInABox("Is Enemy ball", colorSensor.isEnemyColor());
        shuffleboard.boolInABox("Red", colorSensor.isRed());
        shuffleboard.boolInABox("Blue", colorSensor.isBlue());
        shuffleboard.number("Pressure", jacobSensor.getPressure());
        shuffleboard.number("Also pressure", jacobSensor.getPressure());
        shuffleboard.boolInABox("Ratchet", climber.ratchetStatus());


        shuffleboard.number("Right output", driveTrain.rightPercent());
        shuffleboard.number("Left output", driveTrain.leftPercent());
        shuffleboard.number("Joystick power", Controls.getLeftStickY());
        //shuffleboard.number("Code right power", rightPower);
        //shuffleboard.number("Code left power", leftPower);
        shuffleboard.number("Right voltage", driveTrain.rightVoltage());
        shuffleboard.number("Left voltage", driveTrain.leftVoltage());
    }
}
