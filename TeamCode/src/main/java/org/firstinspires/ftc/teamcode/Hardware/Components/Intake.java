package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Intake {

    private static DcMotor s;

    public static void initialize(HardwareFF robot) {
        s = robot.intake;
    }

    private static double power = 0;

    public static double getPower () {
        return power;
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

    public static void setPower (double powerw) {
        power = powerw;
    }

    public static void update () {
        s.setPower( power );
    }
}
