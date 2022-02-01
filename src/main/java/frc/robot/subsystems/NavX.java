package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NavX extends SubsystemBase {
    AHRS navx = new AHRS(SPI.Port.kMXP);
    
    public double getAngle() {
        return navx.getAngle();
    }
    
    public double getSpeed() {
        return Math.sqrt(Math.pow(navx.getVelocityX(), 2) + Math.pow(navx.getVelocityY(), 2) + Math.pow(navx.getVelocityZ(), 2));
    }


}
