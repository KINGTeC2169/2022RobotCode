package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallManager extends SubsystemBase {

    private static String[] ballCount = {"emtpy", "empty"};

    public void addBall(String ball) {
        ballCount[1] = ballCount[0];
        ballCount[0] = ball;

    }

    public void removeBall() {
        if(ballCount[1].equals("empty")) {
            ballCount[0] = "empty";
        } else if(!ballCount[0].equals("empty")) {
            ballCount[1] ="empty";
        }
    }

    public int getNumberOfBalls() {
        int counter = 0;
        for(int i = 0; i < ballCount.length; i++) {
            if(!ballCount[i].equals("empty")) {
                counter++;
            }
        }
        return counter;
    }

    public String getNextBallColor() {
        if(!ballCount[1].equals("empty")) {
            return ballCount[1];
        } else if(!ballCount[0].equals("empty")) {
            return ballCount[0];
        }
        return "empty";
    }

    public String getFirstBall() {
        return ballCount[0];
    }

    public String getSecondBall() {
        return ballCount[1];
    }




    
}
