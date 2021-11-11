package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;

public class Auto1 extends LinearOpMode{
    static final double     COUNTS_PER_MOTOR_REV    = /*767.2*/ 383.5 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_CM       = 9.6 ;     // This measurement is more exact than inches
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * Math.PI));
    HardwareFF robot;
    ElapsedTime runtime = new ElapsedTime();
    public void initHardware() {
        robot = new HardwareFF();
        robot.setHardwareMap(hardwareMap);
        robot.initComponents();
        robot.initWheels();
        while (!isStopRequested())
        {
            sleep(50);
            idle();
        }
        telemetry.addData("Ready! ", "Let's go");    //
        telemetry.update();


    }

    public void testDrive(double speed, double distance, DcMotor input) {
        int newTarget;
        MecanumWheels wheels = new MecanumWheels();
        if (opModeIsActive()) {
            wheels.UpdateInput(0, 1, 0);
            newTarget = input.getCurrentPosition() + (int)(distance * COUNTS_PER_CM);

            input.setTargetPosition(newTarget);

            // reset the timeout time and start motion.
            runtime.reset();

            input.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            input.setPower(wheels.getRearRightPower()*speed);
            while (opModeIsActive()  && (input.isBusy())) {

                telemetry.addData("BackRightPower",input.getPower());
                telemetry.addData("current position",input.getCurrentPosition());

                telemetry.update();
            }


            input.setPower(0);




            input.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {

    }
}
