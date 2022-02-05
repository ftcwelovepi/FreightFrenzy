package org.firstinspires.ftc.teamcode.Hardware;

import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.Spinner;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;

public class ThreadedComponents extends Thread{
    public static boolean run;

    @Override
    public void run() {
        while (run) {
            Intake.update();
            Slides.update();
            Bucket_Servo.update();
            Spinner.update();
            SynchronizedMovement.run();
        }
    }
}
