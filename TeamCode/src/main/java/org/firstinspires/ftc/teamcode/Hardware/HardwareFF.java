package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class HardwareFF {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    public DcMotorEx spinner;
    public DcMotor slides;
    public DcMotor intake;
    public Servo bucket;

    private static boolean wheels;
    private static boolean components;
    private static boolean slide;
    private static boolean intakeB;
    private static boolean spinnerB;
    private static boolean bucketB;

    public DistanceSensor distanceSensor;
    public Rev2mDistanceSensor sensorTimeOfFlight;

    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(5.2, 3.5, 3.5, 3.5);

    public BNO055IMU imu;
    VoltageSensor voltageSensor;
    public HardwareMap hwMap = null;

    public void setHardwareMap (HardwareMap awhMap) {
        hwMap = awhMap;
        wheels = false;
        components = false;
        slide = false;
        intakeB = false;
        spinnerB = false;
        bucketB = false;
    }

    public void initSensor() {
        distanceSensor = hwMap.get( DistanceSensor.class, "distance" );
        sensorTimeOfFlight = (Rev2mDistanceSensor) distanceSensor;
    }

    public void initImu() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hwMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);
    }

    public void initIntake() {
        if (intakeB || components) return;

        intake = hwMap.get( DcMotor.class, "intake" );
        intake.setDirection( DcMotor.Direction.REVERSE );
        intakeB = true;
    }

    public void initSpinner() {
        if (spinnerB || components) return;

        spinner = hwMap.get(DcMotorEx.class, "spinner");
        spinner.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER );
        spinner.setMode( DcMotor.RunMode.RUN_USING_ENCODER );

        // RUE limits max motor speed to 85% by default
        // Raise that limit to 100%
        MotorConfigurationType motorConfigurationType = spinner.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        spinner.setMotorType(motorConfigurationType);

        // Set PIDF Coefficients with voltage compensated feedforward value
        spinner.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(
                MOTOR_VELO_PID.p, MOTOR_VELO_PID.i, MOTOR_VELO_PID.d,
                MOTOR_VELO_PID.f * 12 / voltageSensor.getVoltage()
        ));

        spinnerB = true;
    }

    public void initBucket () {
        if (bucketB || components) return;
        bucket = hwMap.get(Servo.class, "bucket");
        bucket.setPosition(0.0);
        bucketB = true;
    }

    public void initComponents () {
        if (components) return;

        voltageSensor = hwMap.voltageSensor.iterator().next();

        initBucket();
        initSpinner();
        initIntake();
        initSlides();
        initSensor();

        spinner.setDirection(DcMotor.Direction.REVERSE);

        components = true;

    }

    public void initSlides () {
        if (slide || components) return;

        slides = hwMap.get( DcMotor.class, "slides" );
        slides.setDirection( DcMotor.Direction.REVERSE );
        slides.setMode( DcMotor.RunMode.STOP_AND_RESET_ENCODER );
        slides.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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