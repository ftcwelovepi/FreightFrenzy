package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Path;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;

import org.firstinspires.ftc.teamcode.Autonomous.OpenCVDetection;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Red Carousel Side", group = "Freight Frenzy")
public class RedCarouselSide extends BaseAuto{

    @Override
    public void runOpMode() {
        autoInit();

        waitForStart();
        sleep(500);
        telemetry.addData("Position", pipeline.position.toString());
        telemetry.update();

        SynchronizedMovement position;
        // change position - uppercase
//        String pipehan = "LEFT";
//
        sleep(500);
        if (pipeline.position== FreightFrenzyDeterminationPipeline.DuckPosition.LEFT){
            position = SynchronizedMovement.LOW;
            telemetry.addData("Going with BOTTOM", "LEFT");
        }
        else if (pipeline.position== FreightFrenzyDeterminationPipeline.DuckPosition.MIDDLE) {
            position = SynchronizedMovement.MID;
            telemetry.addData("Going with MID", "MIDDLE");
        }
        else {
            position = SynchronizedMovement.UP;
            telemetry.addData("Going with TOP", "RIGHT");
        }

        startingAngle = getAverageGyro();

        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();

        if (opModeIsActive()) {
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );

            //Carousel Move to and Spinn

            encoderMecanumDrive(0.6, 60, 3, 0.40, 1);
            encoderMecanumDrive(0.2, 20, 3, 0.40, 1);

//            encoderMecanumDrive(0.55, 80, 3, 0.40, 1);
            Spinner.setVelocity(0.5);
            sleep( 1000 );
            Spinner.setVelocity(0.8);
            sleep(600);

            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.spinner.setPower( 0 );

            encoderMecanumDrive(0.7, 20, 3, 0,-1);
            gyroTurn(0.7,startingAngle+40); //Turn to face it
            SynchronizedMovement.move( position );
            encoderMecanumDrive(0.6, 60, 3, 0,-1);
            //extend linear slidehan

            while (SynchronizedMovement.getStage() != 4) {
                SynchronizedMovement.run();
                Slides.update();
                Intake.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
                telemetry.update();
            }
            Slides.update();
            Intake.update();
            Bucket_Servo.update();
            encoderMecanumDrive( 0.8, 69, 3, -1, 0 );
            gyroTurn(0.6, startingAngle+180);
            gyroTurn(0.6, startingAngle+180);
            encoderMecanumDrive( 0.8, 90, 3, 1, 0.5 );
            Intake.setPower( -1 );
            Intake.update();
            encoderMecanumDrive( 0.6, 90, 3, 0, 1 );

            //go forward till detect
            int forward = 0;
            while (!(robot.distanceSensor.getDistance( DistanceUnit.MM ) < 75) && opModeIsActive() && forward < 30) {
                encoderMecanumDrive( 0.4, 5, 3, 0, 1 );
                forward += 5;
            }
            encoderMecanumDrive( 0.4, forward, 3, 0, -1 );
            Intake.setPower( 1 );
            Intake.update();
            gyroTurn(0.6, startingAngle+180);
            encoderMecanumDrive( 0.8, 20, 3, 1, 0 );
            encoderMecanumDrive( 0.8, 75, 3, 0, -1 );
            encoderMecanumDrive( 0.8, 95, 3, -1, -0.5 );
            SynchronizedMovement.move( SynchronizedMovement.UP );
            gyroTurn(0.7,startingAngle+90); //Turn to face it
            while (SynchronizedMovement.getStage() != 6) {
                SynchronizedMovement.run();
                Slides.update();
                Intake.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
                telemetry.update();
            }
            gyroTurn(0.7,startingAngle+180);
            encoderMecanumDrive( 0.8, 20, 3, 1, 0 );
            encoderMecanumDrive( 0.8, 110, 3, 1, 0.5 );
            encoderMecanumDrive( 0.8, 90, 3, 0, 1 );

        }
    }
}
