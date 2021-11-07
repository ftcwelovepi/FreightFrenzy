package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bucket_Servo {
    private static Servo s;
    private static double bucketTime = 2000;
    private static double bucketTimeIncrement = bucketTime/;
    private static double pauseTime = System.currentTimeMillis();
    private static boolean bucketBack = true;

    private static double low = 0;
    private static double high = 1;

    private static double position = 0.0;

    public static void initialize(HardwareMap hwMap) {
        if (s == null && !hwMap.allDeviceMappings.contains( s )){
            s = hwMap.get( Servo.class, "bucket" );
        }
    }

    public static void update() {
        setPosition( position );
    }

    public static void glide() {

    }

    public static void glide(double totalTimeMilis) {

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
