package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
     //TODO: get what the networktable is called from the pi
    private static NetworkTable Pi = NetworkTableInstance.getDefault().getTable("Ballology");
    private static double[] arr = {-1, -1, -1, -1};
    public int ballCount() {
        return 0;
    }
    public double[][] getBallLocation() {
        //array structure is 
        //[color (blue = 0, red = 1, none = -1), x, y, confidence]
        
        //python script will give the most confident blue and most confident red to stop
        //from overwhelming and to avoid arraylists

        //the max amount of rows will be 2 one for each ball color starting with blue then red

        //if ball is not seen color will go to 2 and all other values will be zero
        return new double[][]{Pi.getEntry("blue").getDoubleArray(arr), Pi.getEntry("red").getDoubleArray(arr)};
        //return new double[][]{{0, 200, 200, 50}, {1, 100, 100, 75}};
    }
    public boolean validRedTarget() {
        //gives if there is a red ball in frame
        return getBallLocation()[0][0] > 0;
    }
    public boolean validBlueTarget() {
        return getBallLocation()[1][0] > 0;
    }

    public double getBlueBallX() {
        return getBallLocation()[0][1];
    }

    public double getBlueBallY() {
        return getBallLocation()[0][2];
    }

    public double getRedBallX() {
        return getBallLocation()[1][1];
    }

    public double getRedBallY() {
        return getBallLocation()[1][2];
    }
}
