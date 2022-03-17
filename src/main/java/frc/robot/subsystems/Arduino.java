package frc.robot.subsystems;

import java.util.Timer;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class Arduino extends SubsystemBase {
    DigitalOutput ledLeft = new DigitalOutput(ActuatorMap.leftLed);
    DigitalOutput ledRight = new DigitalOutput(ActuatorMap.rightLed);
    
    public void changeLed(boolean isOn) {
        ledLeft.set(isOn);
    }

    public void rightOn() {
        ledRight.set(true);
    }
    
    public void leftOn() {
        ledLeft.set(true);
    }

    public void leftOff() {
        ledLeft.set(false);
    }

    public void rightOff() {
        ledLeft.set(false);
    }

    public void off() {
        ledRight.set(false);
        ledLeft.set(false);
    }

    public void on() {
        ledRight.set(true);
        ledLeft.set(true);
    }
}
