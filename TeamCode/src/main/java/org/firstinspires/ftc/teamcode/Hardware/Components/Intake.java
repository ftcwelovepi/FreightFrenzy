package org.firstinspires.ftc.teamcode.Hardware.Components;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;

public class Intake {

    private static DcMotor s;
    private static DistanceSensor d;
    private static DigitalChannel redLED;
    private static DigitalChannel greenLED;

    public static void initialize(HardwareFF robot) {
        s = robot.intake;
        d = robot.distanceSensor;
        greenLED = robot.greenLED;
        redLED = robot.redLED;
        s.setPower( 0 );
    }

    private static double power = 0;

    public static double getPower () {
        return power;
    }

    public static void flipSwitch () {
        if (power == 0) {
            power = 1;
        } else {
            power = 0;
        }
    }

    public static void flipSwitchREVERSE () {
        if (power == 0) {
            power = -1;
        } else {
            power = 0;
        }
    }

    public static void flipSwitchREVERSE (double powerw) {
        if (power == 0) {
            power = -powerw;
        } else {
            power = 0;
        }
    }

    public static void stop() {
        power = 0;
    }

    public static void reverseDirection() {
        if (s.getDirection().equals( DcMotor.Direction.FORWARD )) {
            s.setDirection( DcMotor.Direction.REVERSE );
        } else {
            s.setDirection( DcMotor.Direction.FORWARD );
        }
    }

    public static void setPower (double powerw) {
        power = powerw;
    }

    public static void update () {
        if (d.getDistance( DistanceUnit.MM ) < 75 && SynchronizedMovement.get() == SynchronizedMovement.STALL){
            power = Math.max( power, 0 );
            redLED.setState( false );
            greenLED.setState( true );
        } else {
            redLED.setState( true );
            greenLED.setState( false );
        }
        s.setPower( power );
    }
}
