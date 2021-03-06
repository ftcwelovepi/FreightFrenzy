package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;
import org.firstinspires.ftc.teamcode.ThreadedWait;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;


/**
 * This file provides necessary functionality for We Love Pi Team's 2021 Freight
 * Frenzy robot's Mecanum Wheel Drivetrain
 */


public class MecanumDriveTrainFieldCentric {

    public double max_stick = 1.0;
    public double min_stick = -1.0;

    // Declare Drive Train motors
    protected DcMotor frontLeft = null;
    protected DcMotor frontRight = null;
    protected DcMotor backLeft = null;
    protected DcMotor backRight = null;
    protected MecanumWheels wheels = new MecanumWheels();


    // Global variables to be initialized in the constructor
    private Telemetry telemetry = null;
    private HardwareFF hardwareMap = null;
    private Gamepad gamepad1 = null;
    private Gamepad gamepad2 = null;
    boolean malinDrive = false; //
    boolean malinPastState = false;
    double speedlimiter = 1;
    public BNO055IMU imu;
    public static double startingAngle = Math.PI/2;
    private double CUM = -0.2; //Coefficient for Undoing Momentum

    private double measuredAngle;

    // Constructors

    public MecanumDriveTrainFieldCentric(HardwareFF robot, Gamepad gamepadinput, Telemetry telemetry) {
        frontLeft = robot.frontLeft;
        frontRight = robot.frontRight;
        backLeft = robot.backLeft;
        backRight = robot.backRight;
        gamepad1 = gamepadinput;
        robot.initImu();
        imu = robot.imu;
        this.telemetry = telemetry;

    }

    public void stop() { //inverse the direction of the motor to apply a quick stopping force
        double frontLeftPower = frontLeft.getPower();
        double frontRightPower = frontRight.getPower();
        double backLeftPower = backLeft.getPower();
        double backRightPower = backRight.getPower();

        double pauseTime = System.currentTimeMillis();

        frontLeft.setPower(frontLeftPower * CUM);
        frontRight.setPower(frontRightPower * CUM);
        backLeft.setPower(backLeftPower * CUM);
        backRight.setPower(backRightPower * CUM);
        while (System.currentTimeMillis() - 100 < pauseTime){ //there's probably an error here (Nathan R. Liu)
            //wait 100 ms
        }
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    // During teleop this function runs repeatedly
    public void loop() {

        double left_x = 0.0;
        double left_y = 0.0;
        double right_x = 0.0;
        double left_toggle = 0.7;
        double right_toggle = 0.4;

        // dpad overrides other joysticks
        if (gamepad1.dpad_left) {
            left_x = min_stick;
        } else if (gamepad1.dpad_right) {
            left_x = max_stick;
        } else if (gamepad1.dpad_up) {
            left_y = max_stick;
        } else if (gamepad1.dpad_down) {
            left_y = min_stick;
        } else if(gamepad1.right_trigger >= 0.1){
            left_x = gamepad1.left_stick_x * right_toggle;
            left_y = -gamepad1.left_stick_y * right_toggle;
            right_x = -gamepad1.right_stick_x * right_toggle * (gamepad1.left_trigger >= 0.1 ?  left_toggle : 1);
            max_stick = max_stick * right_toggle;
            min_stick = min_stick * right_toggle;
        }
        else {
            left_x = gamepad1.left_stick_x;
            left_y = -gamepad1.left_stick_y;
            right_x = -gamepad1.right_stick_x;
        }

        if(gamepad1.left_bumper) {
            left_x = -left_x;
        } else {
            left_x = +left_x;
        }

        right_x*=0.8;

        if (gamepad1.right_bumper && !malinPastState){
            malinDrive = !malinDrive;
        }

        if (gamepad1.a) {
            startingAngle = getAverageGyro();
        }

        malinPastState = gamepad1.right_bumper;

        // Update the joystick input to calculate  wheel powers
        Point p = translate( new Point( left_x, left_y ), getAverageGyro());
        wheels.UpdateInput(p.x, p.y, right_x);

        //TODO Fix the stop condition, it stops way to fast, if the user wants to do small movements it will jitter badly
//        if ((Math.abs(gamepad1.left_stick_x) < 0.1 && Math.abs(gamepad1.left_stick_y) < 0.1 && Math.abs(gamepad1.right_stick_x) < 0.1)) {
//            stop();
//        }

        if (!malinDrive){
            frontLeft.setPower(wheels.getFrontLeftPower());
            frontRight.setPower(wheels.getFrontRightPower());
            backRight.setPower(wheels.getRearRightPower());
            backLeft.setPower(wheels.getRearLeftPower());
        }else{
            frontLeft.setPower(-wheels.getRearRightPower());
            frontRight.setPower(-wheels.getRearLeftPower());
            backRight.setPower(-wheels.getFrontLeftPower());
            backLeft.setPower(-wheels.getFrontRightPower());
        }

    }

    //Pass in desired direction from gpad controls
    //Return power ratio to motors in x, y form
    //Give current direction it is faced
    public static Point translate(Point point, double theta) {

        double desiredR = Math.sqrt(point.x*point.x + point.y*point.y);
        double desiredAngle = Math.atan2(point.y, point.x);

        double differenceAngle = desiredAngle - theta + startingAngle;
        point.x = Math.cos(differenceAngle)*desiredR;
        point.y = Math.sin(differenceAngle)*desiredR;

        return point;
    }

    public double getAverageGyro(){
        /*int sum = robot.realgyro.getIntegratedZValue() + robot.realgyro2.getIntegratedZValue();
        return sum/2;*/
        Orientation angles = imu.getAngularOrientation( AxesReference.INTRINSIC, AxesOrder.ZYX, RADIANS);
        return angles.firstAngle;
    }

    class Point {
        double x, y;
        Point (double x, double y) {
            this.x = x; this.y = y;
        }
    }
}