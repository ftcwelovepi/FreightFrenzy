package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.ComponentSpecifcMap;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

@TeleOp(name="Distance", group="FrieghtFrenzy")
public class DistanceTelOp extends OpMode {

    ComponentSpecifcMap robot = new ComponentSpecifcMap();

    @Override
    public void init() {
        robot.init( hardwareMap );
    }

    @Override
    public void loop() {
        telemetry.addData("deviceName",robot.sensor.getDeviceName() );
        telemetry.addData("range", String.format("%.01f mm", robot.sensor.getDistance( DistanceUnit.MM)));
        telemetry.addData("range", String.format("%.01f cm", robot.sensor.getDistance(DistanceUnit.CM)));
        telemetry.addData("range", String.format("%.01f m", robot.sensor.getDistance(DistanceUnit.METER)));
        telemetry.addData("range", String.format("%.01f in", robot.sensor.getDistance(DistanceUnit.INCH)));

        // Rev2mDistanceSensor specific methods.
        telemetry.addData("ID", String.format("%x", robot.sensorTimeOfFlight.getModelID()));
        telemetry.addData("did time out", Boolean.toString(robot.sensorTimeOfFlight.didTimeoutOccur()));

        telemetry.update();
    }
}
