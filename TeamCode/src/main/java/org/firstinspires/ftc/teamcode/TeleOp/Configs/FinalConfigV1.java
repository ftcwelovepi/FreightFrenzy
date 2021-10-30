package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

public class FinalConfigV1 extends Template{

    public void init() {
        robot.initWheels();
    }

    public void loop() {

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
