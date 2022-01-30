
package org.firstinspires.ftc.teamcode.toBeDeleted;

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
import org.firstinspires.ftc.teamcode.Autonomous.BaseAuto;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;

import org.firstinspires.ftc.teamcode.Autonomous.OpenCVDetection;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "RedSide Carousel Left", group = "Freight Frenzy")
public class RedCarouselLeft extends BaseAuto {

    @Override
    public void runOpMode() {
        /*
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
//
//        if (pipehan.equals("LEFT")){
//            position = SynchronizedMovement.LOW;
//            telemetry.addData("Going with BOTTOM", "LEFT");
//        }
//        else if (pipehan.equals("MID")) {
//            position = SynchronizedMovement.MID;
//            telemetry.addData("Going with MID", "MIDDLE");
//        }
//        else {
//            position = SynchronizedMovement.UP;
//            telemetry.addData("Going with TOP", "RIGHT");
//        }

        startingAngle = getAverageGyro();

        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();

        if (opModeIsActive()) {
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );

            //Carousel Move to and Spinn
            encoderMecanumDrive(0.55, 80, 3, 0.40, 1);
            Spinner.setVelocity(0.4);
            sleep( 800 );
            Spinner.setVelocity(1);
            sleep(700);

            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
            robot.spinner.setPower( 0 );

            encoderMecanumDrive(0.7, 20, 3, 0,-1);
            gyroTurn(0.7,startingAngle+40); //Turn to face it
            SynchronizedMovement.move( position );
            encoderMecanumDrive(0.7, 69, 3, 0,-1);
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
            encoderMecanumDrive(0.65, 10, 3, -0.2,1);
            gyroTurn(0.6, startingAngle);
            //Sweep for cube two
            encoderMecanumDrive(0.65,45,3,-1,-0.4);
            gyroTurn(0.6, startingAngle+170);
            Intake.setPower( -0.75 );
            Intake.update();
            encoderMecanumDrive( 0.65, 40, 3, -0.3, 1 );
            gyroTurn(0.6, startingAngle+223);
            encoderMecanumDrive( 0.65, 35, 3, 0, 1 );
            encoderMecanumDrive( 0.7, 25, 3, 0.7, -0.4 );
            gyroTurn(0.6, startingAngle+135);
            encoderMecanumDrive( 0.65, 17, 3, 0, -1 );
            Intake.setPower(0);
            Intake.update();
            SynchronizedMovement.move(SynchronizedMovement.UP);
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

            encoderMecanumDrive( 0.8, 40, 3, 1, 0.5 );
            gyroTurn(0.6, startingAngle+180);
            encoderMecanumDrive( 0.8, 85, 3, 1, 0.5 );
            encoderMecanumDrive( 0.8, 70, 3, 0, 1 );









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

         */
    }

}
