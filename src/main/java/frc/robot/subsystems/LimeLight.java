package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase{

    private double previousPower;
    public static double rimHeight = 104; // Height of upper HUB rim in inches
    public static final double launchHeight = 26; // Height of ball when last contacting ramp
    public static final double fgInInchesPerSec = -386.2204724; // The acceleration due to gravity in in/sec
    public static final double rampAngle = 67.5; // in degrees
    public static final double flywheelRadius = .0508; // in meters
    public static final double wheelMass = 1.22; // in kg
    public static final double wheelRadius = .0508; // Wheel radius in meters
    public static final double ballRadius = .16025; // in meters
    public static final double ballMass = .269; // in kg
    public static final double ballI = .00254; // Moment of inertia for a spherical shell (with cargo dimensions plugged in)

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
    public double autoAimPower() {
        double error = getLeftXPercent() + getRightXPercent();
        //double power = previousPower + (error* .00000125);
        double power = previousPower + (error* .00014);
        previousPower = power;
       if(previousPower > 1){
        previousPower = 1;
      }
      else if (previousPower < -1){
        previousPower = -1;
      }
      return power;

    }

    public double getRightDistance() {
        if(getRightYPercent() == 0)
            return 0.0;
        return ((63.593059725) / Math.tan(((29.4 + getRightYPercent()) * Math.PI)/180))/* + 131.0*/;
    }
    public double getLeftDistance() {
        if(getLeftYPercent() == 0)
            return 0.0;
        return ((63.593059725) / Math.tan(((29.4 + getLeftYPercent()) * Math.PI)/180 ))/* + 131.0*/;
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
            return getRPM(getLeftDistance());
        }
        else if(getRightDistance() > 0 && getLeftDistance() == 0) {
            //return equation
            return getRPM(getRightDistance());
        }
        else
            return 0;
    }

    // Returns the time the ball will be in the air for a predicted shot at distance from center of hub
    public static double getShotDuration(double distance) {
        // Numerator of time function
        double numerator = 2 * (-1 /Math.tan(Math.toRadians(rampAngle)) * (rimHeight - launchHeight) + distance);
        // Denominator of time function 
        double denominator = -fgInInchesPerSec * (1/Math.tan(Math.toRadians(rampAngle)));
        // Returns the time, in seconds, that the ball will be in the air
        return Math.sqrt(numerator / denominator);
    }

    // Returns the velocity of a shot that will travel DISTANCE horizontally with the pre-specified rampAngle
    private static double getShotVelocity(double distance) {
        return distance / (getShotDuration(distance) * Math.cos(Math.toRadians(rampAngle)));
    }

    // Makes calls to functions above to return the predicted WHEEL RPM needed for predicted shot
    private static double getRPM(double distance) {
        // Outside part of formula for RPM
        double outsidePart = (2 * getShotVelocity(distance) *2.54/100) / (wheelMass * wheelRadius);
        // Inside part of formula for RPM
        double insidePart = wheelMass + ballMass + (ballI / (ballRadius*ballRadius));
        // Combine insidePart and outsidePart to get rad/sec of flywheel
        double radPerSec = outsidePart * insidePart;
        // Return RPM
        return radPerSec / 2 / Math.PI * 60;
    }
    
}
