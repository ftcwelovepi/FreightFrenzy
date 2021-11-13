package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import android.transition.Slide;

import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;

public class ComponentTesting_SL extends Template{

    boolean isRTPressed = false;
    boolean isLTPressed = false;

    public void init() {
        robot.initWheels();
        Slides.initialize( robot );
        Slides.scalePower( 0.7 );
    }

    public void rt (float pressure) {
        if (isRTPressed) Slides.setPower( pressure );
        if (pressure != 0) {
            isRTPressed = true;
        } else {
            isRTPressed = false;
        }
    }

    public void lt (float pressure) {
        if (isLTPressed) Slides.setPower( -pressure );
        if (pressure != 0) {
            isLTPressed = true;
        } else {
            isLTPressed = false;
        }
    }

    public void a(boolean pressed) {
        if (pressed) Slides.setEnhancedSlide(true);
    }

    public void loop() {
        if (!isRTPressed && !isLTPressed) Slides.setPower( 0 );
        Slides.update();
    }

    public String getName() {
        return "Slide Component Testing";
    }
}
