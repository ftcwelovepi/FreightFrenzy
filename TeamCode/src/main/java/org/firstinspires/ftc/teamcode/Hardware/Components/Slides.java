package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Slides {

    private static DcMotor s;
    private static int targetEncoders;
    private static double max = 1, min = -1, power = 0, scale = 0.7;
    private static boolean enhancedSlide = true;

    public static void initialize (HardwareFF robot) {
        s = robot.slides;
    }

    public static void setEnhancedSlide (boolean f) {
        enhancedSlide = f;
    }

    public static void runWithEncoders() {
        s.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER );
        s.setMode( DcMotor.RunMode.RUN_TO_POSITION );
    }

    public static void runWithoutEncoders() {
        s.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );
    }

    public static void scalePower(double factor) {
        if (factor > 1 || factor < 0) return;
        scale = factor;
        min*=scale;
        max*=scale;
    }

    public static void resetPowerScale() {
        scale = 0.7;
        min = -1;
        max = 1;
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

    public static void update () {
        s.setPower( power );
    }

}
