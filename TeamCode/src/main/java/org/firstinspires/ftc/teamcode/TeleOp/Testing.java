package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDriveTrain;

@TeleOp(name="Testing", group="UltimateGoal")
public class Testing extends OpMode {

    // declaring variables
    MecanumDriveTrain vroom;

    /* Declare OpMode members. */
    HardwareFF robot = new HardwareFF(); // use the class created to define a RoverRuckus's hardware

    @Override
    public void init() {
        telemetry.addData("What", "Do u want");
        telemetry.update();
        robot.init(hardwareMap);
        vroom = new MecanumDriveTrain(robot, gamepad1,telemetry);

        telemetry.addData("Haddi", "Haddi");
        telemetry.update();
    }

    @Override
    public void loop() {
        vroom.loop();
    }
}