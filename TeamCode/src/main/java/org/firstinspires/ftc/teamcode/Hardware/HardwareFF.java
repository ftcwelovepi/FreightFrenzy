package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareFF {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    public Servo bucket;
    public CRServo spinner;

    private boolean wheels;
    private boolean components;

    public HardwareMap hwMap = null;

    public void setHardwareMap (HardwareMap awhMap) {
        hwMap = awhMap;
        wheels = false;
        components = false;
    }

    public void initComponents () {
        if (components) {
            return;
        }

        bucket = hwMap.get(Servo.class, "bucket");
        spinner = hwMap.get(CRServo.class, "spinner");

        components = true;
    }

    public void initWheels () {

        if (wheels) {
            return;
        }

        frontLeft = hwMap.get(DcMotor.class, "front_left");
        frontRight = hwMap.get(DcMotor.class, "front_right");
        backRight = hwMap.get(DcMotor.class, "back_right");
        backLeft = hwMap.get(DcMotor.class, "back_left");

        frontLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        wheels = true;



    }

}