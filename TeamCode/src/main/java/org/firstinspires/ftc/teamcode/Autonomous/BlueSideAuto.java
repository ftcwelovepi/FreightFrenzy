package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;
import org.firstinspires.ftc.teamcode.ThreadedWait;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Blue Side Auto", group = "Freight Frenzy")
public class BlueSideAuto extends LinearOpMode {
    static final double     COUNTS_PER_MOTOR_REV    = /*767.2*/ 383.5 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_CM       = 9.6 ;     // This measurement is more exact than inches
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * Math.PI));
    static final double     HEADING_THRESHOLD       = 1 ;
    static final double     P_TURN_COEFF            = 0.03;
    double startingAngle;
    HardwareFF robot;
    ElapsedTime runtime = new ElapsedTime();
    public void initHardware() {
        robot = new HardwareFF();
        robot.setHardwareMap(hardwareMap);
        robot.initComponents();
        robot.initWheels();
        robot.initImu();
        robot.initSlides();
//        while (!isStopRequested())
//        {
//            sleep(50);
//            idle();
//        }
        telemetry.addData("Ready! ", "Let's go");    //
        telemetry.update();


    }

    public void testDrive(double speed, double distance, DcMotor input) {
        int newTarget;
        MecanumWheels wheels = new MecanumWheels();
        if (opModeIsActive()) {
            wheels.UpdateInput(0, 1, 0);
            newTarget = input.getCurrentPosition() + (int)(distance * COUNTS_PER_CM);

            input.setTargetPosition(newTarget);

            // reset the timeout time and start motion.
            runtime.reset();

            input.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            input.setPower(wheels.getRearRightPower()*speed);
            while (opModeIsActive()  && (input.isBusy())) {

                telemetry.addData("Power",input.getPower());
                telemetry.addData("current position",input.getCurrentPosition());

                telemetry.update();
            }


            input.setPower(0);




            input.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public boolean areMotorsRunning(MecanumWheels wheels){
        for (int i = 0; i < wheels.wheelPowers.length;i++){
            if (wheels.wheelPowers[i]!=0){
                switch (i){
                    case 0:
                        if (!robot.frontLeft.isBusy()) {
                            telemetry.addData("front left motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 1:
                        if (!robot.frontRight.isBusy()) {
                            telemetry.addData("front right power",  wheels.wheelPowers[i]);
                            telemetry.addData("front right motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 2:
                        if (!robot.backLeft.isBusy()){
                            telemetry.addData("back left motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 3:
                        if (!robot.backRight.isBusy()) {
                            telemetry.addData("back right motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }


    public void encoderMecanumDrive(double speed, double distance , double timeoutS, double move_x, double move_y) {
        int     newFrontLeftTarget;
        int     newFrontRightTarget;
        int     newBackLeftTarget;
        int     newBackRightTarget;
        int     frontLeftSign;
        int     frontRightSign;
        int     backLeftSign;
        int     backRightSign;
        MecanumWheels wheels = new MecanumWheels();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            wheels.UpdateInput(move_x, move_y, 0);


            frontLeftSign = (int) (Math.abs(wheels.getFrontLeftPower())/wheels.getFrontLeftPower());
            frontRightSign = (int) (Math.abs(wheels.getFrontRightPower())/wheels.getFrontRightPower());
            backLeftSign = (int) (Math.abs(wheels.getRearLeftPower()) /wheels.getRearLeftPower());
            backRightSign = (int) (Math.abs(wheels.getRearRightPower())/wheels.getRearRightPower());



            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = robot.frontLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*frontLeftSign);
            newBackLeftTarget = robot.backLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*backLeftSign);
            newFrontRightTarget = robot.frontRight.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*frontRightSign);
            newBackRightTarget = robot.backRight.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*backRightSign);


            //Set target position
            robot.frontLeft.setTargetPosition(newFrontLeftTarget);
            robot.frontRight.setTargetPosition(newFrontRightTarget);
            robot.backLeft.setTargetPosition(newBackLeftTarget);
            robot.backRight.setTargetPosition(newBackRightTarget);



            // Turn On RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeft.setPower(Math.abs(wheels.getFrontLeftPower()*speed));
            robot.frontRight.setPower(Math.abs(wheels.getFrontRightPower()*speed));
            robot.backRight.setPower(Math.abs(wheels.getRearRightPower()*speed));
            robot.backLeft.setPower(Math.abs(wheels.getRearLeftPower()*speed));



            while (opModeIsActive() && (runtime.seconds() < timeoutS) && areMotorsRunning(wheels)) {

                telemetry.addData("FrontLeftPower",robot.frontLeft.getPower());
                telemetry.addData("FrontRightPower",robot.frontRight.getPower());
                telemetry.addData("BackRightPower",robot.backRight.getPower());
                telemetry.addData("BackLeftPower",robot.backLeft.getPower());
//                telemetry.addData("front left power",  wheels.wheelPowers[0]);
//                telemetry.addData("back left power",  wheels.wheelPowers[2]);
//                telemetry.addData("back right power",  wheels.wheelPowers[3]);
//                telemetry.addData("front right power",  wheels.wheelPowers[1]);
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }



    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.addData("current_heading", getAverageGyro());
            telemetry.update();
        }
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.frontLeft.setPower(leftSpeed);
        robot.frontRight.setPower(rightSpeed);
        robot.backLeft.setPower(leftSpeed);
        robot.backRight.setPower(rightSpeed);


        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - getAverageGyro();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getAverageGyro(){
        /*int sum = robot.realgyro.getIntegratedZValue() + robot.realgyro2.getIntegratedZValue();
        return sum/2;*/
        Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, DEGREES);
        double heading = angles.firstAngle;
        return heading;
    }
    @Override
    public void runOpMode() {
        initHardware();
        Slides.initialize(robot);
        Bucket_Servo.initialize(robot);

        telemetry.addData( "Autotest", "han" );
        telemetry.update();

        waitForStart();
        startingAngle = getAverageGyro();
        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();
        if (opModeIsActive()) {
            encoderMecanumDrive(0.4, 85, 3, -0.40, 1);
            robot.spinner.setPower( -1 );
            sleep( 3500 );
            robot.spinner.setPower( 0 );
            encoderMecanumDrive(0.4, 10, 3, 0,-1);

            gyroTurn(0.7,startingAngle-40);
//            gyroTurn(0.7,90);
            encoderMecanumDrive(0.4, 85, 3, 0,-1);
            //extend linear slidehan
            SynchronizedMovement.move(SynchronizedMovement.UP);
            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {

                SynchronizedMovement.run();
                Slides.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
//                telemetry.addData("")
                telemetry.update();
            }
            ThreadedWait waits = new ThreadedWait(1000);
            waits.start();

            while (!waits.get()) {
                Bucket_Servo.update();
            }

            SynchronizedMovement.move(SynchronizedMovement.DOWN);
            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {

                SynchronizedMovement.run();
                Slides.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
//                telemetry.addData("")
                telemetry.update();
            }
            waits = new ThreadedWait(1000);
            waits.start();

            while (!waits.get()) {
                Bucket_Servo.update();
            }

//            Slides.update();
//            Bucket_Servo.update();
            gyroTurn(0.7, startingAngle + 40);
            encoderMecanumDrive(.4,113,3,1,-1);
            encoderMecanumDrive(.4,80,3,0,-1);

        }
    }

}
