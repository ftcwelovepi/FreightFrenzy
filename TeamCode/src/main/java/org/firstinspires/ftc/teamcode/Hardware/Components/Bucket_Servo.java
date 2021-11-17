package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.ThreadedWait;

public class Bucket_Servo {
    private static Servo s;
    private static double bucketTimeIncrement = 35;
    private static double pauseTime = System.currentTimeMillis();
    private static boolean bucketBack = true;

    private static double low = 0;
    private static double high = 1;

    private static double position = 0.4;
    private static double glideTarget = 0.4;

    private static boolean gliding = false;

    private static ThreadedWait wait = new ThreadedWait(35);

    public static void initialize(HardwareFF robot) {
        s = robot.bucket;
    }

    public static void update () {
        update(false);
    }

    public static void update (boolean autoSecure, double milis) {
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

            wait = new ThreadedWait(milis);
            wait.start();
        }
//        if (autoSecure && position < 0.5) glideToPosition( 0.7 );
//        if (!autoSecure && position > 0.5) glideToPosition(0.4);
        setPosition( );
    }

    public static void update (boolean autoSecure) {
        update(autoSecure, 35);
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

    public static void glide(boolean oppositeEnd) {
        bucketTimeIncrement = 35;
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

    public static void setPosition () {
        if ((position >= 0 && position <= 1)) s.setPosition( position );
    }

    public static double getTargetPosition () {
        return position;
    }

    public static double getPosition () {
        return s.getPosition();
    }

    public static void scaleServo(double min, double max) {
        s.scaleRange( min, max );
    }

    public static void scaleLocal(double min, double max) {
        low = min;
        high = max;
    }
}
