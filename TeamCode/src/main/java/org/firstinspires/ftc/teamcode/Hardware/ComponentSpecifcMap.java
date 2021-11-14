package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREV2mDistance;

public class ComponentSpecifcMap {

    public HardwareMap hwMap = null;
    public DistanceSensor sensor;
    public Rev2mDistanceSensor sensorTimeOfFlight;

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        sensor = hwMap.get( DistanceSensor.class, "distance" );
        sensorTimeOfFlight = (Rev2mDistanceSensor) sensor;
    }
}
