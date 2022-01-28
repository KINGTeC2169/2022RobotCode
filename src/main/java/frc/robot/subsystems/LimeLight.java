package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private static NetworkTable limeLight = NetworkTableInstance.getDefault().getTable("limelight");

    public double getXPercent() {
        return limeLight.getEntry("tx").getDouble(0);
    }

    public double getYPercent() {
        return limeLight.getEntry("ty").getDouble(0);
    }

    
}
