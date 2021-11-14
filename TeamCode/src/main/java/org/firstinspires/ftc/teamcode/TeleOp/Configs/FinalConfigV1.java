package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;

public class FinalConfigV1 extends Template{

    String stage = "Nothing Yet";
    int bucketStage = 1;

    public void init() {
        robot.initWheels();
        robot.initComponents();
        Slides.initialize( robot );
        Intake.initialize( robot );
        Bucket_Servo.initialize( robot );
        Spinner.initialize( robot );
    }

    @Override
    public void a(boolean pressed) {
        if (pressed) {
            if (bucketStage>3) {
                bucketStage = 0;
            }
            switch (++bucketStage) {
                case 1:
                    Bucket_Servo.glideToPosition( 0 );
                    break;
                case 2:
                    Bucket_Servo.glideToPosition( 0.4 );
                    break;
                case 3:
                    Bucket_Servo.glideToPosition( 1 );
                    break;
            }
        }
    }

    @Override
    public void b(boolean pressed) {
        if (pressed) {
            Intake.flipSwitch();
        }
    }

    @Override
    public void rb(boolean pressed) {
        if (pressed) {
            Spinner.flipSwitch();
        }
    }

    @Override
    public void du(boolean pressed) {
        if (pressed) {
            Bucket_Servo.moveForward();
        }
    }

    public void dd(boolean pressed) {
        if (pressed) {
            Bucket_Servo.moveBackward();
        }
    }

    @Override
    public void ljoy(float x, float y) {
        Slides.setPower( y );
    }

    public void loop() {
        Spinner.update();
        Bucket_Servo.update();
        Intake.update();
        Slides.update();
    }

    @Override
    public void updateTelemetryDM() {
        telemetryDM.put( "Slides Power", String.valueOf( Slides.getPower() ) );
        telemetryDM.put( "Spinner Power", String.valueOf( Spinner.getPower() ) );
        telemetryDM.put( "Intake Power", String.valueOf( Intake.getPower() ) );
        telemetryDM.put( "Bucket Position", String.valueOf( Bucket_Servo.getPosition() ) );
        telemetryDM.put( "::::::::::::",":::::::::::" );
        telemetryDM.put( "Stage Message", stage );

    }

    public String getName() {
        return "1st final config version";
    }

    @NonNull
    @Override
    public String toString() {
        return "Final Config";
    }
}
