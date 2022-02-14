package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shuffleboard extends SubsystemBase {
    
    public void boolInABox(String key, boolean bool) {
        SmartDashboard.putBoolean(key , bool);
    }

    public void field(double xMeters, double yMeters, Rotation2d rotation) {
        Field2d field = new Field2d();
        field.setRobotPose(xMeters, yMeters, rotation);
        SmartDashboard.putData(field);
    }
}
