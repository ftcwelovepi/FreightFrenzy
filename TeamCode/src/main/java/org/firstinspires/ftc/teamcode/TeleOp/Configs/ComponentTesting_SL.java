package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;

import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;

public class ComponentTesting_SL extends Template{

    int high = 600;
    int low = 50;

    double scale = 1;
    double power = 0;

    boolean isRTPressed = false;
    boolean isLTPressed = false;

    public void init() {
        robot.initWheels();
        robot.initSlides();
        robot.initIntake();
        robot.slides.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
    }

    public void rt (float pressure) {

    }

    public void lt (float pressure) {

    }

    @Override
    public void updateTelemetryDM() {
        telemetryDM.put( "Encoders", String.valueOf( robot.slides.getCurrentPosition() ) );
        telemetryDM.put( "Highbound",  String.valueOf(high));
        telemetryDM.put( "Lowbound", String.valueOf(low) );
        telemetryDM.put( "Motor Power", String.valueOf( robot.slides.getPower() ) );
        telemetryDM.put( "Power", String.valueOf( power ) );
        telemetryDM.put( "Scale", String.valueOf( scale ) );

    }

    public void a(boolean pressed) {
        if (pressed)
            scale-=0.1;
    }

    @Override
    public void b(boolean pressed) {
        if (pressed)
            scale += 0.1;
    }

    @Override
    public void x(boolean pressed) {

    }

    @Override
    public void du(boolean pressed) {
        if (pressed)
            high += 10;
    }

    public void dd(boolean pressed) {
        if (pressed)
            high -= 10;
    }

    public void dr(boolean pressed) {
        if (pressed)
            low += 10;
    }

    public void dl(boolean pressed) {
        if (pressed)
            low -= 10;
    }

    public void loop() {
        if (robot.slides.getCurrentPosition() >= high && power>0) power = 0;
        if (robot.slides.getCurrentPosition() <= low && power<0) power = 0;
        robot.slides.setPower( power * scale );
    }

    public String getName() {
        return "Slide Component Testing";
    }
}
