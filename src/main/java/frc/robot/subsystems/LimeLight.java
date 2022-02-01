package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private static NetworkTable limeLightBack = NetworkTableInstance.getDefault().getTable("limelight-back");
    private static NetworkTable limeLightFront = NetworkTableInstance.getDefault().getTable("limelight-front");

    public double getRightXPercent() {
        return limeLightFront.getEntry("tx").getDouble(0);
    }

    public double getRightYPercent() {
        return limeLightFront.getEntry("ty").getDouble(0);
    }

    public double getLeftXPercent() {
        return limeLightBack.getEntry("tx").getDouble(0);
    }

    public double getLeftYPercent() {
        return limeLightBack.getEntry("ty").getDouble(0);
    }

    public double getRightDistance() {
        return (65) / Math.tan(((45+getRightYPercent()) * Math.PI)/180);
    }
    public double getLeftDistance() {
        return (65) / Math.tan(((45+getLeftYPercent()) * Math.PI)/180);
    }
    
    public void setRightPipeline(int pipelineID) {
        limeLightFront.getEntry("pipeline").setNumber(pipelineID);
    }

    public void setLeftPipeline(int pipelineID) {
        limeLightBack.getEntry("pipeline").setNumber(pipelineID);
    }
    
}
