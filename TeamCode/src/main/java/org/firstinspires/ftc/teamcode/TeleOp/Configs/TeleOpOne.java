package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

public class TeleOpOne extends Template {


    public void init() {
        robot.initWheels();
    }

    public void loop() {

    }

    @Override
    public void a(boolean pressed) {
        if (pressed) {

        }
    }

    public void updateTelemetryDM() {
        telemetryDM.put( "Testing", "Testing 2" );
    }

    public String getName() {
        return "First Class";
    }

    @NonNull
    @Override
    public String toString() {
        return "First Class";
    }
}
