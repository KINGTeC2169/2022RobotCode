package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class CompressorTank {
    static Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

    //Methods for turning compressor on and off
    public static void disable() {
        compressor.disable();
    }

    public static void enable() {
        compressor.enableDigital();
    }
}
