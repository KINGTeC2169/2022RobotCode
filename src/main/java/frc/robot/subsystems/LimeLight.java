package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

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
        if(getRightYPercent() == 0)
            return 0.0;
        return (63.593059725) / Math.tan(((37 + getRightYPercent()) * Math.PI)/180);
    }
    public double getLeftDistance() {
        if(getLeftYPercent() == 0)
            return 0.0;
        return (63.593059725) / Math.tan(((37 + getLeftYPercent()) * Math.PI)/180);
    }
    
    public void setRightPipeline(int pipelineID) {
        limeLightRight.getEntry("pipeline").setNumber(pipelineID);
    }

    public void setLeftPipeline(int pipelineID) {
        limeLightLeft.getEntry("pipeline").setNumber(pipelineID);
    }

    public double rpm() {
        if(getLeftDistance() > 0 && getRightDistance() == 0) {
            //return equation
            return -1;
        }
        else if(getRightDistance() > 0 && getLeftDistance() == 0) {
            //return equation
            return -1;
        }
        else
            return -1;
    }
    
}
