package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

/**
 * Spinner Component
 * Has all the necessary methods for the Spinner
 */
public class Spinner {

    private static final double MOTOR_TICKS_PER_REV = 384.5;
    private static final double MOTOR_MAX_RPM = 435;
    private static DcMotorEx s;

    public static void initialize(HardwareFF robot) {
        s = robot.spinner;
    }

    private static double power = 0;

    public static void stop() {
        power = 0;
    }

    public static void flipSwitch () {
        if (power == 0) {
            power = 1;
        } else {
            power = 0;
        }
    }
    public static void flipSwitchREVERSE () {
        if (power == 0) {
            power = -1;
        } else {
            power = 0;
        }
    }

    public static void reverseDirection() {
        if (s.getDirection().equals( DcMotor.Direction.FORWARD )) {
            s.setDirection( DcMotor.Direction.REVERSE );
        } else {
            s.setDirection( DcMotor.Direction.FORWARD );
        }
    }

    public void setVelocity(double power){
        s.setVelocity(rpmToTicksPerSecond(power*MOTOR_MAX_RPM));
    }

    public static double getPower () {
        return power;
    }

    public static void setPower (double powerw) {
        power = powerw;
    }

    public static void update () {
        s.setPower( power );
    }

    public static double rpmToTicksPerSecond(double rpm) {
        return rpm * MOTOR_TICKS_PER_REV  / 60;
    }
}
