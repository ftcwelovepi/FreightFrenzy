package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Slides {

    private static DcMotor s;
    private static DistanceSensor d;
    private static double max = 1, min = -1, power = 0, scale = 0.7;
    private static boolean enhancedSlide = true;
    private static int lowerboundMid = 450, lowerboundHigh = 600;

    public static void initialize (HardwareFF robot) {
        s = robot.slides;
        runWithEncoders();
//        d = robot.distanceSensor;
    }

    public static void setEnhancedSlide (boolean f) {
        enhancedSlide = f;
    }

    public static void runWithEncoders() {
        s.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER );
        s.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
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

    public static void setPower (double power) {
        if (enhancedSlide) {
            if (power > 0) {
                Slides.power = power*=scale;
            } else {
                Slides.power = power*=scale-0.5;
            }
        } else {
            Slides.power = power*=scale;
        }
    }

    public static void flipSwitch() {
        if (power != 0) setPower( 1 );
        else setPower( 0 );
    }

    public static void update () {
        if (s.getCurrentPosition() > lowerboundHigh + 30 && power > 0) power = 0;
        s.setPower( power );
    }

}
