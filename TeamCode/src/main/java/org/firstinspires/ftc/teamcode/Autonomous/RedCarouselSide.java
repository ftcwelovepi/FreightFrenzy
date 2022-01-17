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
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;

import org.firstinspires.ftc.teamcode.Autonomous.OpenCVDetection;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "RedSide Carousel", group = "Freight Frenzy")
public class RedCarouselSide extends BaseAuto{

    @Override
    public void runOpMode() {
        autoInit();

        waitForStart();
        sleep(500);
        telemetry.addData("Position", pipeline.position.toString());
        telemetry.update();

        SynchronizedMovement position;

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
        SynchronizedMovement.move( SynchronizedMovement.UP );

        startingAngle = getAverageGyro();

        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();

        if (opModeIsActive()) {
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );

            //Carousel Move to and Spinn
            encoderMecanumDrive(0.5, 80, 3, 0.40, 1);
            robot.spinner.setPower( 0.7 );
            sleep( 2000 );

            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.spinner.setPower( 0 );

            encoderMecanumDrive(0.6, 20, 3, 0,-1);
            gyroTurn(0.7,startingAngle+40); //Turn to face it
            encoderMecanumDrive(0.6, 70, 3, 0,-1);
            //extend linear slidehan

            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {

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
            gyroTurn(0.7, startingAngle);
            //Sweep for cube two
            encoderMecanumDrive(0.6,40,3,-1,-0.4);
            gyroTurn(0.7, startingAngle+170);
            Intake.setPower( -1 );
            encoderMecanumDrive( 0.6, 70, 3, 0, 1 );
//            encoderMecanumDrive( 0.6, 70, 3, 0, -1 );
//            gyroTurn(0.7, startingAngle);
//            encoderMecanumDrive(0.6,70,3,1,0.4);
//            gyroTurn(0.7, startingAngle + 40);
//            //Place second block
//            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {
//                SynchronizedMovement.run();
//                Slides.update();
//                Bucket_Servo.update();
//                telemetry.addData("Stage", SynchronizedMovement.getStage());
//                telemetry.addData("Encoder", Slides.getEncoders());
//                telemetry.addData("Power", Slides.getPower());
//                telemetry.update();
//            }
//            Slides.update();
//            Bucket_Servo.update();
////            BEGINNING OF REGULAR RUN
//            gyroTurn(0.7, startingAngle);
//            encoderMecanumDrive(0.6,135,3,-1,-0.4);
//            gyroTurn(0.7, startingAngle);
//            moveConstGyroandDist( 0.6, 120, -1, 0, startingAngle );
//            END OR REGULAR RUNNING

//            encoderMecanumDrive(.4,170,3,0,-1);

            //encoderMecanumDrive( .4, 20, 3, 1, -0.4 ); Uncomment for one block run

            //End of One Block code

            //Drive to Wobble
//            encoderMecanumDrive( 0.6, 120, 3, 0, 1 );
//            encoderMecanumDrive( 0.6,135,3,1,0.4 );
//            gyroTurn(0.7,startingAngle+40);
//
//            //Drop and Drive back to outpost
//            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {
//
//                SynchronizedMovement.run();
//                Slides.update();
//                Bucket_Servo.update();
//                telemetry.addData("Stage", SynchronizedMovement.getStage());
//                telemetry.addData("Encoder", Slides.getEncoders());
//                telemetry.addData("Power", Slides.getPower());
//                telemetry.update();
//            }
//            Slides.update();
//            Bucket_Servo.update();
//            gyroTurn(0.7, startingAngle);
//            encoderMecanumDrive(0.6,135,3,-1,-0.4);
//            gyroTurn(0.7, startingAngle);
//            moveConstGyroandDist( 0.6, 120, -1, 0, startingAngle );
//
//
//            gyroTurn( 0.7, startingAngle + 180 );
//            //robot dance
//            gyroTurn(.7, startingAngle + 170);
//            gyroTurn(.7, startingAngle + 190);
//            gyroTurn(.7, startingAngle + 170);
//            gyroTurn(.7, startingAngle+190);
//            gyroTurn(.7, startingAngle + 180);
        }
    }
}
