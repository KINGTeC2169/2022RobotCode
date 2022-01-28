package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NavX extends SubsystemBase {
    AHRS navx = new AHRS(SPI.Port.kMXP);
    
    public double getAngle() {
        return navx.getAngle();
    }
    
}
