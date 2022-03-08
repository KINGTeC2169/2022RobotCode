package frc.robot.subsystems;

import java.util.Timer;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class Arduino extends SubsystemBase {
    DigitalOutput ledLeft = new DigitalOutput(ActuatorMap.leftLed);
    DigitalOutput ledRight = new DigitalOutput(ActuatorMap.rightLed);
    Timer time = new Timer();
    
    public void changeLed(boolean isOn) {
        ledLeft.set(isOn);
    }

    public void blinkLeft() {

    }

    public void blinkRight() {

    }

    public void rightOn() {
        ledRight.set(true);
    }
    
    public void leftOn() {
        ledLeft.set(false);
    }

    public void leftOff() {

    }

    public void rightOff() {

    }
}
