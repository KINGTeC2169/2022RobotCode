package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallManager extends SubsystemBase {

    private static boolean[] ballCount = new boolean[2];

    /**Sets the index 0 to true */
    public void newBall() {
        ballCount[0] = true;
    }

    /**Sets index 1 to true and index 0 to false */
    public void cycleBall() {
        if(ballCount[0]) {
            ballCount[1] = true;
            ballCount[0] = false;
        } 
    }

    /**Sets index 1 to false */
    public void shootBall() {
        ballCount[1] = false;
    }

    /**Returns total number of balls in robot */
    public int getNumberOfBalls() {
        int counter = 0;
        for(boolean ball : ballCount) {
            if(ball) {
                counter++;
            }
        }
        return counter;
    }

    /**Returns value of index 0 */
    public boolean getFirstPositionBall() {
        return ballCount[0];
    }

    /**Returns value of index 1 */
    public boolean getSecondPositionBall() {
        return ballCount[1];
    }

    public void setFirstPositionBall(boolean iLoveBalls) {
        ballCount[0] = iLoveBalls;
    }

    /**Sets both spots to false */
    public void reset() {
        ballCount[0] = false;
        ballCount[1] = false; 
    }

    /**Sets index 1 to true, index 0 to false */
    public void startAuto() {
        ballCount[0] = false;
        ballCount[1] = true;
    }




    
}
