package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private static NetworkTable limeLightBack = NetworkTableInstance.getDefault().getTable("limelight-back");
    private static NetworkTable limeLightFront = NetworkTableInstance.getDefault().getTable("limelight-front");

    public double getFrontXPercent() {
        return limeLightFront.getEntry("tx").getDouble(0);
    }

    public double getFrontYPercent() {
        return limeLightFront.getEntry("ty").getDouble(0);
    }

    public double getBackXPercent() {
        return limeLightBack.getEntry("tx").getDouble(0);
    }

    public double getBackYPercent() {
        return limeLightBack.getEntry("ty").getDouble(0);
    }
    
    public void setFrontPipeline(int pipelineID) {
        limeLightFront.getEntry("pipeline").setNumber(pipelineID);
    }

    public void setBackPipeline(int pipelineID) {
        limeLightBack.getEntry("pipeline").setNumber(pipelineID);
    }
    
}
