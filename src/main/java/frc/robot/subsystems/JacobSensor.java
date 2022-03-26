package frc.robot.subsystems;


import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class JacobSensor extends SubsystemBase{

    AnalogPotentiometer tape = new AnalogPotentiometer(ActuatorMap.pressure, 100, 0);
    

    public double getPressure() {
        return tape.get();
            
    }
}
