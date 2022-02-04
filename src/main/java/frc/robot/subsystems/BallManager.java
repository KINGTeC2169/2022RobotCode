package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallManager extends SubsystemBase {

    private static boolean[] ballCount = new boolean[2];

    public void newBall() {
        if(ballCount[1]) {
            ballCount[0] = true;
        } else {
            ballCount[1] = true;
        }

    }

    public void removeBall() {
        if(ballCount[0]) {
            ballCount[1] = true;
        } else {
            ballCount[1] = false;
        }
    }

    public int getNumberOfBalls() {
        int counter = 0;
        for(boolean ball : ballCount) {
            if(ball) {
                counter++;
            }
        }
        return counter;
    }

    public boolean getFirstPositionBall() {
        return ballCount[0];
    }

    public boolean getSecondPositionBall() {
        return ballCount[1];
    }




    
}
