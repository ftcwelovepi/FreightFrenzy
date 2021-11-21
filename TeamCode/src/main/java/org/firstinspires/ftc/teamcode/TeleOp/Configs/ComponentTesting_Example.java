package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;

public class ComponentTesting_Example extends Template {
    //Initializes the component we want to test
    public void init() {
        Intake.initialize( robot );
    }

    //Is called when gamepad 2 presses a
    @Override
    public void a(boolean pressed) {
        if (pressed)
            Intake.flipSwitch();
    }

    //Loops during tele-op
    public void loop() {
        Intake.update();
    }

    public String getName() {
        return "Example Config";
    }
}
