package org.firstinspires.ftc.teamcode.Hardware.Components;

import androidx.appcompat.widget.WithHint;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.ThreadedWait;

public class Intake {

    private static DcMotor s;
    private static DistanceSensor d;
    private static DigitalChannel redLED;
    private static DigitalChannel greenLED;

    static int encoders;
    static ThreadedWait waits = new ThreadedWait( 500 );
    static boolean startedThread = false;

    public static void initialize(HardwareFF robot) {
        s = robot.intake;
//        s.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
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

//        if (!startedThread) {
//            waits = new ThreadedWait( 500 );
//            waits.start();
//            startedThread = true;
//
//            if (power != 0 && Math.abs(encoders - s.getCurrentPosition()) < 100) {
//                if (power == 0.25)
//                    power = -0.25;
//                power = 0.25;
//            }
//            encoders = s.getCurrentPosition();
//
//        }
//        if (waits.get()) {
//            startedThread = false;
//        }

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
