package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arduino extends SubsystemBase {
    DigitalOutput led = new DigitalOutput(0);
    
    public void changeLed(boolean isOn) {
        led.set(isOn);
    }
    
}
