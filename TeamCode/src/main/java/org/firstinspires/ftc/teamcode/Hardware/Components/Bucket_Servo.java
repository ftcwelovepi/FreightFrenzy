package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.ThreadedWait;

public class Bucket_Servo {
    private static Servo s;
    private static double pauseTime = System.currentTimeMillis();
    private static boolean bucketBack = true;

    private static double position = 0;
    private static double glideTarget = 0;

    private static double lowRest = 0.3, lowSecure = 0.5, highSecure = 0.7, highDrop = 1;

    private static boolean gliding = false;

    private static ThreadedWait wait = new ThreadedWait(35);

    public static double getLowRest () {
        return lowRest;
    }

    public static double getLowSecure () {
        return lowSecure;
    }

    public static double getHighSecure () {
        return highSecure;
    }

    public static double getHighDrop () {
        return highDrop;
    }

    public static void initialize(HardwareFF robot) {
        s = robot.bucket;
    }

    public static void update () {
        update(false);
    }

    public static void update (boolean autoSecure) {
        if (gliding) {
            if (!wait.get()) {
                return;
            }
            if (glideTarget+0.01 < position) {
                position -= 0.05;
            } else if (glideTarget-0.01 > position) {
                position += 0.05;
            } else {
                gliding = false;
            }

            wait = new ThreadedWait(45);
            wait.start();
        }
//        if (autoSecure && position < 0.5) glideToPosition( 0.7 );
//        if (!autoSecure && position > 0.5) glideToPosition(0.4);
        setPosition( );
    }

    public static void moveForward () {
        moveForward(0.05);
    }

    public static void moveForward (double amount) {
        position += (position!=1.0 ? amount : 0);
    }

    public static void moveBackward () {
        moveBackward(0.05);
    }

    public static void moveBackward (double amount) {
        position -= (position!=1.0 ? amount : 0);
    }

    public static void glideToPosition (double position) {
        gliding = true;
        glideTarget = position;
        wait.start();
    }

    public static void glideFlipSwitch () {
        if (position < 0.5)
            glideToPosition( 1 );
        else
            glideToPosition( 0 );
    }

    public static void glide(boolean oppositeEnd) {
        if (oppositeEnd) {
            glideTarget = (s.getPosition() < 0.5 ? 1 : 0);
        } else {
            glideTarget = 1-s.getPosition();
        }
        glideToPosition( glideTarget );
    }

    public static void switchRelative () {
        position = 1-s.getPosition();
    }

    public static void switchToOppositeEnd () {
        if (s.getPosition() < 0.5) {
            position = 1;
        } else {
            position = 0;
        }
    }

    public static int iterationsTotal = 10;
    public static int iterations = 10;

    public static void jitterSet (int iterationsM) {
        iterationsTotal = iterationsM;
        iterations = 0;
    }
    private static void jitter () {
        if (!wait.isAlive()&&iterations<iterationsTotal) {
            wait = new ThreadedWait( 100 );
            wait.start();
            glideToPosition( (position!=0 ? 0 : 0.4) );
            iterations++;
        }
    }

   public static void setPosition( double p) {
        position = p;
        setPosition();
   }

    public static void setPosition () {
        if ((position >= 0 && position <= 1)) s.setPosition( position );
    }

    public static double getTargetPosition () {
        return position;
    }

    public static double getPosition () {
        return s.getPosition();
    }
}
