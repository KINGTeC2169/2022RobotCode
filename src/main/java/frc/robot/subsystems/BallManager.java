package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallManager extends SubsystemBase {

    private static boolean[] ballCount = new boolean[2];

    public void newBall() {
        ballCount[0] = true;
    }
    public void cycleBall() {
        if(ballCount[0]) {
            ballCount[1] = true;
            ballCount[0] = false;
        } 
    }

    public void shootBall() {
        ballCount[1] = false;
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
