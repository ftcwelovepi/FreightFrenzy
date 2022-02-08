package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryInternal;

public class ComponentTesting_Grabber extends Template {

    public DcMotor grabberMotor;
    public CRServo grabberExtender;
    public Servo grabberClaw;

    public void init() {
        robot.initGrabber();
        grabberMotor = robot.grabberMotor;
        grabberExtender = robot.grabberExtender;
        grabberClaw = robot.grabberClaw;
    }

    @Override
    public void a(boolean pressed) {
        if (pressed)
            grabberClaw.setPosition( (grabberClaw.getPosition() > 0.5 ? 0 : 1 ) );
    }

    @Override
    public void ljoy(float x, float y) {
        grabberMotor.setPower( y );
    }

    @Override
    public void rjoy(float x, float y) {
        grabberExtender.setPower( y );
    }

    public void loop() {
        telemetryDM.put( "Grabber motor power", String.valueOf( grabberMotor.getPower() ) );
        telemetryDM.put( "Grabber crservo extender", String.valueOf( grabberExtender.getPower() ) );
        telemetryDM.put( "Grabber Servo claw", String.valueOf( grabberClaw.getPosition() ) );
    }

    public void stop() {

    }

    public String getName() {
        return null;
    }
}
