package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;

public class ComponentTesting_BS extends Template{
    public void init() {
        robot.initWheels();
        Bucket_Servo.initialize( robot );
    }

    public void a(boolean pressed) {
        if (pressed) {
            Bucket_Servo.glide( true );
        }
    }

    public void b (boolean pressed) {

    }

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

    public void loop() {
        Bucket_Servo.update();
    }

    public void updateTelemetryDM() {
        telemetryDM.put( "Bucket Servo Target Position", String.valueOf(Bucket_Servo.getTargetPosition()) );
        telemetryDM.put( "Bucket Servo Actual Position", String.valueOf(Bucket_Servo.getPosition()) );
    }

    public String getName() {
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "BS Component Testing";
    }
}
