package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

import java.util.HashMap;

abstract public class Template2 {

    static HardwareFF robot;
    HashMap<String, String> telemetryDM = new HashMap<>();

    public static void setHardwareMap(HardwareFF robotR) {
        robot = robotR;
    }

    abstract void init();

    //Four buttons
    void a(boolean pressed) {}
    void b(boolean pressed) {}
    void x(boolean pressed) {}
    void y(boolean pressed) {}

    //D-pad
    void dd(boolean pressed) {}
    void du(boolean pressed) {}
    void dl(boolean pressed) {}
    void dr(boolean pressed) {}

    //Right side bumper and trigger
    void rb(boolean pressed) {}
    void rt(float pressure) {}

    //Left side bumper and trigger
    void lb(boolean pressed) {}
    void lt(float pressure) {}

    //right and left joystick and button
    void rjoy(float x, float y) {}
    void ljoy(float x, float y) {}
    void rjoyb(boolean pressed) {}
    void ljoyb(boolean pressed) {}

    void custom1() {}

    void updateTelemetryDM() {}

    void loop() {}

    void clearTelemetryDM() {
        telemetryDM.clear();
    };

     abstract String getName();


}
