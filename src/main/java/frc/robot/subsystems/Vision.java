package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
     //TODO: get what the networktable is called from the pi
    private static NetworkTable Pi = NetworkTableInstance.getDefault().getTable("");

    public int ballCount() {
        return 0;
    }
    public double[][] getBallLocation() {
        //array structure is 
        //[color (blue = 0, red = 1, none = 2), x, y, confidence]
        // camera resolutino is 640 by 480 so the middle would be 320, 240 as x and y
        //python script will give the most confident blue and most confident red to stop from overwhelming and to avoid arraylists
        //if ball is not seen color will go to 2 and all other values will be zero
        return new double[][]{{1, 200, 200, 50}, {0, 100, 100, 75}};
    }
    public boolean validRedTarget() {
        //gives if there is a red ball in frame
        return true;
    }
    public boolean validBlueTarget() {
        return true;
    }
}
