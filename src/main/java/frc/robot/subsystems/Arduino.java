package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class Arduino extends SubsystemBase {
    DigitalOutput ledLeft = new DigitalOutput(ActuatorMap.leftLed);
    DigitalOutput ledRight = new DigitalOutput(ActuatorMap.rightLed);
    
    public void changeLed(boolean isOn) {
        ledLeft.set(isOn);
    }

    public void blinkLeft() {

    }

    public void blinkRight() {

    }

    public void rightOn() {

    }
    
    public void leftOn() {

    }

    public void leftOff() {

    }

    public void rightOff() {

    }
}
