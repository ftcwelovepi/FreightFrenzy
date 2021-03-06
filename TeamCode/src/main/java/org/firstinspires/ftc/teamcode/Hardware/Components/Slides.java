package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Slides {

    private static DcMotor s;
    private static DistanceSensor d;
    private static double max = 1, min = -1, power = 0, scale = 0.8;
    private static boolean enhancedSlide = true;
    private static boolean lockDown = false;
    private static int lowerboundMid = 450, lowerboundHigh = 700, transferPoint = 200, lowerboundLow = 200;

    public static void initialize (HardwareFF robot) {
        s = robot.slides;
        s.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
        s.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER );
        runWithEncoders();
        d = robot.distanceSensor;
        SynchronizedMovement.move(SynchronizedMovement.STALL);
    }

    public static int getTransferPoint () {
        return transferPoint;
    }

    public static int getHigh () {
        return lowerboundHigh;
    }

    public static int getMid () {
        return lowerboundMid;
    }

    public static int getLow () {
        return lowerboundLow;
    }

    public static void setEnhancedSlide (boolean f) {
        enhancedSlide = f;
    }

    public static void runWithEncoders() {
        if (s.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) return;
        s.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
    }

    public static void runToPosition() {
        if (s.getMode() == DcMotor.RunMode.RUN_TO_POSITION) return;
        s.setMode( DcMotor.RunMode.RUN_TO_POSITION );
    }

    public static void runWithoutEncoders() {
        s.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );
    }

    public static void scalePower(double factor) {
        if (factor > 1 || factor < 0) return;
        scale = factor;
    }

    public static void resetPowerScale() {
        scale = 0.7;
    }

    public static void stop() {
        power = 0;
    }

    public static void reverseDirection() {
        if (s.getDirection().equals( DcMotor.Direction.FORWARD )) {
            s.setDirection( DcMotor.Direction.REVERSE );
        } else {
            s.setDirection( DcMotor.Direction.FORWARD );
        }
    }

    public static double getPower() {
        return power;
    }

    public static int getEncoders() {
        return s.getCurrentPosition();
    }

    public static void setPower (double powerw) {
//        if (enhancedSlide) {
//            if (power > 0) {
//                power = powerw * scale;
//            } else {
//                power = powerw * (scale - 0.3);
//            }
//        } else {
//            power = powerw * scale;
//        }
        power = powerw * 0.7;
    }

    public static void goTo (int encoders) {
        s.setTargetPosition( encoders );
    }

    public static void flipSwitch() {
        if (power <= 0 ) setPower( 1 );
        else setPower( -1 );
    }

    public static void switchLock(boolean lock){
        lockDown = lock;
    }

    public static void update () {
        if (s.getCurrentPosition() > lowerboundHigh && power > 0) power = 0;
        if (s.getCurrentPosition() < 200 && power < 0) power = -0.1;
        if (s.getCurrentPosition() < 15 && power < 0) power = 0;
        s.setPower( power );
    }

}
