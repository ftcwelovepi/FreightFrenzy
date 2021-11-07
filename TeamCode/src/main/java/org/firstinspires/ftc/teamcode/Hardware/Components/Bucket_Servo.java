package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bucket_Servo {
    private static Servo s;
    private static double bucketTimeIncrement = 25;
    private static double pauseTime = System.currentTimeMillis();
    private static boolean bucketBack = true;

    private static double low = 0;
    private static double high = 1;

    private static double position = 0.0;
    private static double glideTarget = 0.0;

    private static boolean gliding = false;

    public static void initialize(HardwareMap hwMap) {
        if (s == null && !hwMap.allDeviceMappings.contains( s )){
            s = hwMap.get( Servo.class, "bucket" );
        }
    }

    public static void update() {
        if (gliding) {
            if (pauseTime + bucketTimeIncrement > System.currentTimeMillis()) {
                return;
            }
            if (glideTarget < position) {
                position -= 0.05;
            } else if (glideTarget > position) {
                position += 0.05;
            } else {
                gliding = false;
            }

            pauseTime = System.currentTimeMillis();
        }
        setPosition( position );
    }

    public static void glide(boolean oppositeEnd) {
        bucketTimeIncrement = 25;
        gliding = true;
        if (oppositeEnd) {
            glideTarget = (getPosition() < 0.5 ? 1 : 0);
        } else {
            glideTarget = 1-getPosition();
        }
    }

    public static void glide(boolean oppositeEnd, double milis) {
        bucketTimeIncrement = milis;
        gliding = false;
        if (oppositeEnd) {
            glideTarget = (getPosition() < 0.5 ? 1 : 0);
        } else {
            glideTarget = 1-getPosition();
        }
    }

    public static void switchRelative () {
        position = 1-getPosition();
    }

    public static void switchToOppositeEnd () {
        if (getPosition() < 0.5) {
            position = 1;
        } else {
            position = 0;
        }
    }

    public static void setPosition (double pos) {
        if (!(pos >= 0 && pos <= 1)) return;
        position = pos;
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
