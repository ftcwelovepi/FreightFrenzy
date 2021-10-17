package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.TeleOpOne;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.Template2;

@TeleOp(name="Testing", group="UltimateGoal")
public class Testing extends OpMode {

    final int milisButtonTimeOut = 500;

    // declaring variables
    MecanumDriveTrain vroom;

    /* Declare OpMode members. */
    HardwareFF robot = new HardwareFF(); // use the class created to define a RoverRuckus's hardware

    @Override
    public void init() {
        telemetry.addData("What", "Do u want");
        telemetry.update();
        robot.setHardwareMap( hardwareMap );

        vroom = new MecanumDriveTrain(robot, gamepad1,telemetry);

        Template2.setHardwareMap(robot);

        TeleOpOne frameWork = new TeleOpOne();

        frameWork.init();

        telemetry.addData("Haddi", "Haddi");
        telemetry.update();
    }

    @Override
    public void loop() {
        vroom.loop();
    }


}