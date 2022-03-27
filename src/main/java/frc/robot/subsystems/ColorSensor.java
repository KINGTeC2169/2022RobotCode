package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {

    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    ColorSensorV3 sensor = new ColorSensorV3(i2cPort);

    public boolean isRed() {
        return sensor.getBlue() < sensor.getRed();
    }

    public boolean isBlue() {
        return sensor.getBlue() > sensor.getRed();
    }

    /**Returns true if enemy ball is detected. Values will be inconsisted if no ball is present */
    public boolean isEnemyColor() {
        return false;
        
        /*
        if(isBlue()) {
            if(DriverStation.getAlliance().equals(Alliance.Blue)) {
                return false;
            }
            return true;
        } else {
            if(DriverStation.getAlliance().equals(Alliance.Red)) {
                return false;
            }
            return true;
        }
        */
    }
    
}
