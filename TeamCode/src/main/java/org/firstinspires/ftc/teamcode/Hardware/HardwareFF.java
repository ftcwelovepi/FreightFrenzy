package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareFF {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    public DcMotor spinner;
    public DcMotor slides;
    public DcMotor intake;
    public Servo bucket;

    private static boolean wheels;
    private static boolean components;
    private static boolean slide;
    private static boolean intakeB;
    public BNO055IMU imu;

    public HardwareMap hwMap = null;

    public void setHardwareMap (HardwareMap awhMap) {
        hwMap = awhMap;
        wheels = false;
        components = false;
        slide = false;
        intakeB = false;
    }

    public void initIntake() {
        if (intakeB) return;

        intake = hwMap.get( DcMotor.class, "intake" );
        intakeB = true;
    }

    public void initComponents () {
        if (components) return;

        bucket = hwMap.get(Servo.class, "bucket");
        spinner = hwMap.get(DcMotor.class, "spinner");

        spinner.setDirection(DcMotor.Direction.REVERSE);

        components = true;
    }

    public void initSlides () {
        if (slide) return;

        slides = hwMap.get( DcMotor.class, "slides" );
        slides.setDirection( DcMotor.Direction.FORWARD );

        slide = true;
    }

    public void initWheels () {

        if (wheels) return;

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