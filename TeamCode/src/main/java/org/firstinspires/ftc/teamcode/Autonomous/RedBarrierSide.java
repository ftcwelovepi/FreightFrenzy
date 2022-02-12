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
    double baseTurn = 0.7;

    @Override
    public void runOpMode() {
        autoInit();

        if (robot.voltageSensor.getVoltage() < 13) {
            basePower = 0.8;
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

            encoderMecanumDrive(basePower, 20, 3, 1, 0);
            gyroTurn(baseTurn, startingAngle+120);
            SynchronizedMovement.move( position );
            encoderMecanumDrive(basePower,47,3,0,-1);

            //extend linear slidehan

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

            Slides.update();
            Intake.update();
            Bucket_Servo.update();

            encoderMecanumDrive(basePower,55,3,0,1);
            gyroTurn( baseTurn, startingAngle+180 );
            encoderMecanumDrive(baseTurn, 25, 3, 1, 0);
            encoderMecanumDrive( basePower, 75, 3, 0, 1 );
            block();
            //End of the one block place, ends robot in the warehouse

            block();
            block();

        }
    }

    public void block () {

        //go forward till detect
        Intake.setPower( -1 );
        Intake.update();

        int forward = 0;
        while (!(robot.distanceSensor.getDistance( DistanceUnit.MM ) < 75) && opModeIsActive() && forward < 30) {
            if (forward == 25) {
                Intake.setPower( -1 );
                Intake.update();
                encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
                gyroTurn( baseTurn, startingAngle+170 );
                gyroTurn( baseTurn, startingAngle+180 );
                encoderMecanumDrive( 0.4, 10, 3, -1, 0 );
            } else if (forward == 20) {
                Intake.setPower( 0.7 );
                Intake.update();
            }
            double pauseTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - 100 < pauseTime && opModeIsActive()){
                //wait 100 ms
            }
            encoderMecanumDrive( 0.4, 5, 3, 0, 1, false );
            forward += 5;
        }
        Intake.setPower( 1 );
        Intake.update();
        encoderMecanumDrive( basePower, forward, 3, 0, -1 );

        gyroTurn(baseTurn, startingAngle+180);
        encoderMecanumDrive( basePower, 25, 3, 1, 0 );
        encoderMecanumDrive( basePower, 80, 3, 0, -1 );
        encoderMecanumDrive( basePower, 95, 3, -1, -0.7 );
        SynchronizedMovement.move( SynchronizedMovement.UP );
        gyroTurn(baseTurn,startingAngle+110); //Turn to face it
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
        gyroTurn(baseTurn,startingAngle+180);
        encoderMecanumDrive( basePower, 20, 3, 1, 0 );
        encoderMecanumDrive( basePower, 110, 3, 1, 0.5 );
        encoderMecanumDrive( basePower, 75, 3, 0, 1 );
        //End of the one block place, ends robot in the warehouse

    }
}
