package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShuffleboardManager extends SubsystemBase {
    private NetworkTableEntry number =
    Shuffleboard.getTab("SmartDashboard")
        .add("Max Speed", 1)
          .getEntry();

    /**Creates a BooleanBox */
    public void boolInABox(String key, boolean bool) {
        SmartDashboard.putBoolean(key , bool);
    }

    public void field(double xMeters, double yMeters, Rotation2d rotation) {
        Field2d field = new Field2d();
        field.setRobotPose(xMeters, yMeters, rotation);
        SmartDashboard.putData(field);
    }
    /**Adds text to shuffleboard */
    public void text(String key, String value) {
        SmartDashboard.putString(key, value);
    }
    /**Adds a number to shuffleboard */
    public void number(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }

    public double getSlider(String key) {
        return number
        .getDouble(0);

    }
}
