package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

import java.util.HashMap;

abstract public class Template {

    static final int milisButtonTimeOut = 250;
    static long pauseMilis = System.currentTimeMillis();
    static HardwareFF robot;
    public HashMap<String, String> telemetryDM = new HashMap<>();
    public static void setHardwareMap(HardwareFF robotR) {
        robot = robotR;
    }
    public abstract void init();
    String className;

    //Four buttons
    public void a(boolean pressed) {}
    public void b(boolean pressed) {}
    public void x(boolean pressed) {}
    public void y(boolean pressed) {}

    //D-pad
    public void dd(boolean pressed) {}
    public void du(boolean pressed) {}
    public void dl(boolean pressed) {}
    public void dr(boolean pressed) {}

    //Right side bumper and trigger
    public void rb(boolean pressed) {}
    public void rt(float pressure) {}

    //Left side bumper and trigger
    public void lb(boolean pressed) {}
    public void lt(float pressure) {}

    //right and left joystick and button
    public void rjoy(float x, float y) {}
    public void ljoy(float x, float y) {}
    public void rjoyb(boolean pressed) {}
    public void ljoyb(boolean pressed) {}

    public void custom1() {}

    public static void resetButton() {
        pauseMilis = System.currentTimeMillis();
    }

    //Returns whether buttons can be pressed button timeout
    //Implement in the teleOp
    public static boolean canPress() {
        return pauseMilis + milisButtonTimeOut < System.currentTimeMillis();
    }

    public void updateTelemetryDM() {}

    public abstract void loop();
    public abstract void stop();

    public void clearTelemetryDM() {
        telemetryDM.clear();
    };

    public abstract String getName();

}
