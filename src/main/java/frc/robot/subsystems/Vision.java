package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
     //TODO: get what the networktable is called from the pi
    private static NetworkTable Pi = NetworkTableInstance.getDefault().getTable("");
    public double ballLocationX() {
        return 0;
    }
    public int ballCount() {
        return 0;
    }
    public boolean isRed() {
        return false;
    }
}
