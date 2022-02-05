package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Testing extends SubsystemBase {
    DoubleSolenoid testDouble = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

    public void extend() {
        testDouble.set(Value.kForward);
    }

    public void retract() {
        testDouble.set(Value.kReverse);
    }
    
}
