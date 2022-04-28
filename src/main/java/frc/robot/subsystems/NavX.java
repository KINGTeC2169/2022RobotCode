package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NavX extends SubsystemBase {
    AHRS navx = new AHRS(SPI.Port.kMXP);
    
    //We dont use the NavX, these are just here in case we ever want to
    //
    //


    public double getAngle() {
        return navx.getAngle();
    }
    public Rotation2d getRotation() {
        return navx.getRotation2d();
    }

    public void reset() {
        navx.reset();
    }
    
    public double getSpeed() {
        return Math.sqrt(Math.pow(navx.getVelocityX(), 2) + Math.pow(navx.getVelocityY(), 2));

    }
    public double getXVelocity() {
        return navx.getVelocityX();
    }
    public double getYVelocity() {
        return navx.getVelocityY();
    }
    public double getZVelocity() {
        return navx.getVelocityZ();
    }

    public float getDisplacmentX() {
        return navx.getDisplacementX();
    }
    public float getDisplacmentY() {
        return navx.getDisplacementY();
    }
    public float getDisplacmentZ() {
        return navx.getDisplacementZ();
    }


}
