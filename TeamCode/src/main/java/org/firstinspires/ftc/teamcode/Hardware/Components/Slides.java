package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Slides {

    private static DcMotor s;
    private static int targetEncoders;

    public static void initialize (HardwareFF robot) {
        s = robot.slides;
    }

    public static void update () {

    }

}
