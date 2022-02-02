package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private static NetworkTable limeLightLeft = NetworkTableInstance.getDefault().getTable("limelight-back");
    private static NetworkTable limeLightRight = NetworkTableInstance.getDefault().getTable("limelight-front");

    public double getRightXPercent() {
        return limeLightRight.getEntry("tx").getDouble(0);
    }

    public double getRightYPercent() {
        return limeLightRight.getEntry("ty").getDouble(0);
    }

    public double getLeftXPercent() {
        return limeLightLeft.getEntry("tx").getDouble(0);
    }

    public double getLeftYPercent() {
        return limeLightLeft.getEntry("ty").getDouble(0);
    }

    public double getRightDistance() {
        return (63.593059725) / Math.tan(((37 + getRightYPercent()) * Math.PI)/180);
    }
    public double getLeftDistance() {
        return (63.593059725) / Math.tan(((37 + getLeftYPercent()) * Math.PI)/180);
    }
    
    public void setRightPipeline(int pipelineID) {
        limeLightRight.getEntry("pipeline").setNumber(pipelineID);
    }

    public void setLeftPipeline(int pipelineID) {
        limeLightLeft.getEntry("pipeline").setNumber(pipelineID);
    }
    
}
