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

@Autonomous(name = "Red Barrier Side", group = "Freight Frenzy")
public class RedBarrierSide extends BaseAuto{

    double basePower = 0.8;

    @Override
    public void runOpMode() {
        autoInit();

        if (robot.voltageSensor.getVoltage() < 13) {
            basePower = 0.7;
        }

        waitForStart();
        sleep(500); //Wait to collect data
        telemetry.addData("Position", pipeline.position.toString());
        telemetry.update();

        SynchronizedMovement position;

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

            encoderMecanumDrive(0.4, 20, 3, 1, 0);
            gyroTurn(0.4, startingAngle+120);
            SynchronizedMovement.move( position );
            encoderMecanumDrive(basePower,55,3,0,-1);

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

            encoderMecanumDrive(basePower,55,3,0,1);
            gyroTurn( 0.4, startingAngle+180 );
            encoderMecanumDrive(0.4, 20, 3, 1, 0);

            gyroTurn(0.6, startingAngle+180);
            encoderMecanumDrive( basePower, 75, 3, 0, 1 );
            Intake.setPower( -1 );
            Intake.update();

            //go forward till detect
            int forward = 0;
            while (!(robot.distanceSensor.getDistance( DistanceUnit.MM ) < 75) && opModeIsActive() && forward < 30) {
                if (forward == 25) {
                    encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
                    gyroTurn( 0.6, startingAngle+170 );
                    gyroTurn( 0.6, startingAngle+180 );
                    encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
                }
                encoderMecanumDrive( 0.4, 5, 3, 0, 1 );
                forward += 5;
            }
            encoderMecanumDrive( 0.4, forward, 3, 0, -1 );
            Intake.setPower( 1 );
            Intake.update();

            gyroTurn(0.6, startingAngle+180);
            encoderMecanumDrive( basePower, 20, 3, 1, 0 );
            encoderMecanumDrive( basePower, 75, 3, 0, -1 );
            encoderMecanumDrive( basePower, 95, 3, -1, -0.5 );
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
            encoderMecanumDrive( basePower, 20, 3, 1, 0 );
            encoderMecanumDrive( basePower, 110, 3, 1, 0.5 );
            encoderMecanumDrive( basePower, 90, 3, 0, -1 );
            //End of the one block place, ends robot in the warehouse
            
            block();
            block();

        }
    }

    public void block () {

        //go forward till detect
        int forward = 0;
        while (!(robot.distanceSensor.getDistance( DistanceUnit.MM ) < 75) && opModeIsActive() && forward < 30) {
            if (forward == 25) {
                encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
                gyroTurn( 0.6, startingAngle+170 );
                gyroTurn( 0.6, startingAngle+180 );
                encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
            }
            encoderMecanumDrive( 0.4, 5, 3, 0, 1 );
            forward += 5;
        }
        encoderMecanumDrive( 0.4, forward, 3, 0, -1 );
        Intake.setPower( 1 );
        Intake.update();

        gyroTurn(0.6, startingAngle+180);
        encoderMecanumDrive( basePower, 20, 3, 1, 0 );
        encoderMecanumDrive( basePower, 75, 3, 0, -1 );
        encoderMecanumDrive( basePower, 95, 3, -1, -0.5 );
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
        encoderMecanumDrive( basePower, 20, 3, 1, 0 );
        encoderMecanumDrive( basePower, 110, 3, 1, 0.5 );
        encoderMecanumDrive( basePower, 90, 3, 0, -1 );
        //End of the one block place, ends robot in the warehouse

    }
}
