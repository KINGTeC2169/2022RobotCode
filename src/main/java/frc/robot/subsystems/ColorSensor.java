package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {

    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    ColorSensorV3 sensor = new ColorSensorV3(i2cPort);

    public double num() {
        return sensor.getGreen();
    }
    
}
