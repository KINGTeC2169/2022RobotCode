package frc.robot.subsystems;


import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ActuatorMap;

public class JacobSensor extends SubsystemBase{

    AnalogPotentiometer tape = new AnalogPotentiometer(ActuatorMap.pressure, 100, 0);
    
    /**Returns value of pressure sensor (Around 90-100 for normal balls) */
    public double getPressure() {
        return tape.get();
    }
}
