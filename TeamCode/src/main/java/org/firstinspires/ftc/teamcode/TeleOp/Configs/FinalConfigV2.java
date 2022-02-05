package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.ThreadedComponents;
import org.firstinspires.ftc.teamcode.ThreadedWait;

public class FinalConfigV2 extends Template{

    String stage = "Nothing Yet";
    public boolean intakeStage = true;
    public int spinnerFlip = 1;

    ThreadedWait wait = new ThreadedWait( 300 );
    ThreadedComponents threadedComponents = new ThreadedComponents();

    public void init() {
        robot.initWheels();
        robot.initComponents();
        Slides.initialize( robot );
        Intake.initialize( robot );
        Bucket_Servo.initialize( robot );
        Spinner.initialize( robot );
        SynchronizedMovement.reset();

        ThreadedComponents.run = true;
        threadedComponents.start();
        wait.start();
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

    //TODO: Automatically move the slides to the top position when pressed
    @Override
    public void du(boolean pressed) {

    }

    //TODO: Automatically move the slides down to mid position (Between low and mid)
    public void dr (boolean pressed) {
        if (pressed) {

        }
    }

    // If D-Pad down is pressed fling block with set Position and start a timer to wait for it to fling
    public void dd(boolean pressed) {
        if (pressed && !intakeStage) {
            intakeStage = true;
            Bucket_Servo.setPosition(1);
            startWaitThread(300); //TODO: refine the timing
        }
    }

    @Override
    public void ljoy(float x, float y) {

    }

    //Manual Control of the intake as long as it is during intake stage
    @Override
    public void rjoy(float x, float y) {
        if (intakeStage) {
            Intake.setPower( y );
        }
    }

    //Starts a new threaded wait
    public void startWaitThread (int milis) {
        wait = new ThreadedWait( milis );
        wait.start();
    }

    /*
    On loop if a block is detected during the intake stage then move the slides up
      Turing the intake will help it not get stuck and throw out additional blocks
      Start a wait so that the bucket does not immediately flip out as well

    After the intake stage is complete make sure the bucket stays in a posiiton above 0.4 in order to secure the block
      Use glide to position to not throw the block out

    Lastly once D-pad down is pressed, [ refer to above method ] set position of the bucket to 0 and move slides back
      Start a wait so that TOF sensor does not detect the bucket and think its a block
    */
    public void loop() {
        if (intakeStage && Intake.detect() && wait.get()) {
            Intake.setPower( -0.3 );
            Slides.setPower( 1 );
            intakeStage = false;
            startWaitThread(300);
        } else if (!intakeStage && wait.get()) {
            Bucket_Servo.glideToPosition( Math.max( Bucket_Servo.getTargetPosition(), 0.6 ) );
        } else if (intakeStage && wait.get()) {
            Bucket_Servo.setPosition(0);
            Slides.setPower( -1 ); //TODO: if the slides are low and position of the bucket is high then move slides up
            //This is to make sure that the bucket does not hit the intake on the way down
            startWaitThread( 500 );
        }
    }

    public void stop() {
        ThreadedComponents.run = false;
    }


    //Display all the necessary information
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
