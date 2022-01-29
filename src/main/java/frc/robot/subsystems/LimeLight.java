package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private static NetworkTable limeLightBack = NetworkTableInstance.getDefault().getTable("limelight-back");
    private static NetworkTable limeLightFront = NetworkTableInstance.getDefault().getTable("limelight-front");

    public double getXPercent() {
        return limeLightFront.getEntry("tx").getDouble(0);
    }

    public double getYPercent() {
        return limeLightFront.getEntry("ty").getDouble(0);
    }
    
    public void setPipeline(int pipelineID) {
        limeLightFront.getEntry("pipeline").setNumber(pipelineID);
    }
    
}
