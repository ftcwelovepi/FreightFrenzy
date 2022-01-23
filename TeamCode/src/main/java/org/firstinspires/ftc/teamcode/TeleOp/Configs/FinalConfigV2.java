package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.ThreadedWait;

public class FinalConfigV2 extends Template{

    String stage = "Nothing Yet";
    public boolean overrideSlides = false;
    public boolean press = false;
    public int spinnerFlip = 1;

    ThreadedWait wait = new ThreadedWait( 300 );

    public void init() {
        robot.initWheels();
        robot.initComponents();
        Slides.initialize( robot );
        Intake.initialize( robot );
        Bucket_Servo.initialize( robot );
        Spinner.initialize( robot );
        SynchronizedMovement.reset();
    }

    @Override
    public void ljoyb(boolean pressed) {
        if (pressed)
            Bucket_Servo.glideToPosition( 1 );
    }

    @Override
    public void lb(boolean pressed) {
        if (pressed) {
            Spinner.flipSwitch();
        }
    }

    @Override
    public void rb(boolean pressed) {
        if (pressed) {
            spinnerFlip = -1*spinnerFlip;
        }
    }

    @Override
    public void rt(float pressure) {
        if (pressure > 0.1)
            Spinner.startRamp();
    }

    @Override
    public void du(boolean pressed) {
//        if (pressed) {
//            if (Slides.getEncoders() <= Slides.getMid()) {
//                press = true;
//                Slides.setPower( 1 );
//                if (Slides.getEncoders() >= 20) {
//                    Bucket_Servo.glideToPosition( 0.4 );
//                }
//                Intake.setPower( 0.5 );
//            }
//        }
//        if (press && Slides.getEncoders() >= Slides.getMid()) {
//            Intake.setPower( 0 );
//            press = false;
//        }
    }

    public void dr (boolean pressed) {
        if (pressed) {
            Bucket_Servo.glideToPosition( 1 );
        }
    }

    public void dd(boolean pressed) {
        if (pressed) {
            SynchronizedMovement.move( SynchronizedMovement.UP );
            SynchronizedMovement.down();
        }
    }

    @Override
    public void ljoy(float x, float y) {
        if (SynchronizedMovement.get() == SynchronizedMovement.STALL && !press) {
            Slides.setPower( -y );
        }
    }

    @Override
    public void rjoy(float x, float y) {
        if (SynchronizedMovement.get() == SynchronizedMovement.STALL) {
            Intake.setPower( y );
        }
    }

    public void loop() {
        Spinner.update();
        Bucket_Servo.update();
        Intake.update();
        Slides.update();
        SynchronizedMovement.run();
        updateLoop();
    }

    public void updateLoop() {
        if (Slides.getEncoders() <= 20 && Slides.getPower() > 0) {
            Intake.setPower( -0.5 );
        }else if (Slides.getEncoders() >= 20 && Slides.getPower() > 0) {
            Bucket_Servo.glideToPosition(Math.max( 0.4, Bucket_Servo.getTargetPosition() ));
        }

    }

    @Override
    public void updateTelemetryDM() {
        telemetryDM.put( "Slides Power", String.valueOf( Slides.getPower() ) );
        telemetryDM.put( "Spinner Power", String.valueOf( Spinner.getPower() ) );
        telemetryDM.put( "Intake Power", String.valueOf( Intake.getPower() ) );
        telemetryDM.put( "Bucket Position", String.valueOf( Bucket_Servo.getPosition() ) );
        telemetryDM.put( "Slides Position", String.valueOf(Slides.getEncoders()) );
        telemetryDM.put( "Carousel Velo", String.valueOf( robot.spinner.getCurrentPosition() ) );
        telemetryDM.put( "Sensor", String.valueOf(robot.distanceSensor.getDistance( DistanceUnit.MM )) );
        telemetryDM.put( "::::::::::::",":::::::::::" );
        telemetryDM.put( "Stage Message", stage );

    }

    public String getName() {
        return "2st final config version";
    }

    @NonNull
    @Override
    public String toString() {
        return "Final Config 2";
    }
}
