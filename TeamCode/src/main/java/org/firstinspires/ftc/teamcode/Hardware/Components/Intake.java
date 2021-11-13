package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Intake {

    private static DcMotor s;

    public static void initialize(HardwareFF robot) {
        s = robot.intake;
    }
}
