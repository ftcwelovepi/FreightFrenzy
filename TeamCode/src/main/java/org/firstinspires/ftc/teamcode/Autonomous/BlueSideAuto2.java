package org.firstinspires.ftc.teamcode.Autonomous;

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
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;
import org.firstinspires.ftc.teamcode.ThreadedWait;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Blue Side Auto 2", group = "Freight Frenzy")
public class BlueSideAuto2 extends BaseAuto{
    @Override
    public void runOpMode() {
        autoInit();

        telemetry.addData( "Autotest", "han" );
        telemetry.update();

        waitForStart();
        sleep(1500);
        telemetry.addData("Position", pipeline.position.toString());
        telemetry.update();
        sleep(500);
        if (pipeline.position== FreightFrenzyDeterminationPipeline.DuckPosition.LEFT){
            SynchronizedMovement.move(SynchronizedMovement.LOW);
            telemetry.addData("Going with BOTTOM", "LEFT");
        }
        else if (pipeline.position== FreightFrenzyDeterminationPipeline.DuckPosition.MIDDLE) {
            SynchronizedMovement.move(SynchronizedMovement.MID);
            telemetry.addData("Going with MID", "MIDDLE");
        }
        else {
            SynchronizedMovement.move(SynchronizedMovement.UP);
            telemetry.addData("Going with TOP", "RIGHT");
        }
        startingAngle = getAverageGyro();
        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();
        if (opModeIsActive()){
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );

            encoderMecanumDrive(0.4, 20, 3, 1, 0);
            gyroTurn(0.4, startingAngle+60);
            encoderMecanumDrive(0.4,55,3,0,-1);

//            robot.spinner.setPower( 1 );
//            robot.intake.setPower( 0.7 );
//            sleep( 3500 );
//            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.spinner.setPower( 0 );
//            encoderMecanumDrive(0.4, 10, 3, 0,-1);
//            robot.intake.setPower( 0 );
//
//            gyroTurn(0.7,startingAngle+40);
//            gyroTurn(0.7,90);
//            encoderMecanumDrive(0.4, 70, 3, 0,-1);
//            //extend linear slidehan
//
            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {

                SynchronizedMovement.run();
                Slides.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
                telemetry.update();
            }
            Slides.update();
            Bucket_Servo.update();
            encoderMecanumDrive(0.4,10,3,0,1);
            gyroTurn(.4,startingAngle);
            encoderMecanumDrive(.4,70,3,-1,0);
            encoderMecanumDrive(.4,110,3,0,1);
            encoderMecanumDrive(.4,60,3,1,0);
//            gyroTurn(0.4, startingAngle+180);

            //            gyroTurn(0.7, startingAngle);
//            encoderMecanumDrive(.4,135,3,-1,-0.4);
//            gyroTurn(0.7, startingAngle);
//            moveConstGyroandDist( .4, 110, -1, 0, startingAngle );
////            encoderMecanumDrive(.4,170,3,0,-1);
//            encoderMecanumDrive( .4, 50, 3, 1, -0.4 );
//            gyroTurn( 0.7, startingAngle + 180 );

        }
    }
}
