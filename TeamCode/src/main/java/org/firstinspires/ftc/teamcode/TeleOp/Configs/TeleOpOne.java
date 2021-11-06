package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

public class TeleOpOne extends Template {

    public double bucketTimeIncrement = 25;
    public double pauseTime = System.currentTimeMillis();
    public boolean bucketBack = true;

    public void init() {
        robot.initWheels();
        robot.initComponents();
    }

    public void loop() {
        setBucket();
    }

    private void setBucket () {
        if (pauseTime+bucketTimeIncrement > System.currentTimeMillis()) {
            return;
        }
        if (bucketBack && robot.bucket.getPosition()<1) {
            robot.bucket.setPosition( robot.bucket.getPosition()+0.05 );
        } else if (!bucketBack && robot.bucket.getPosition()>0) {
            robot.bucket.setPosition( robot.bucket.getPosition()-0.05 );
        }
        pauseTime = System.currentTimeMillis();
    }

    @Override
    public void a(boolean pressed) {
        if (pressed) {
            bucketBack = false;
        }
    }

    @Override
    public void b(boolean pressed) {
        if (pressed) {
            bucketBack = true;
        }
    }

    public void updateTelemetryDM() {
        telemetryDM.put( "Testing", "Testing 2" );
    }

    public String getName() {
        return "First Class";
    }

    @NonNull
    @Override
    public String toString() {
        return "First Class";
    }
}
