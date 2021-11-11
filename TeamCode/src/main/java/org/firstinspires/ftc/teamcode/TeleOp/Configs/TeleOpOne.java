package org.firstinspires.ftc.teamcode.TeleOp.Configs;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class TeleOpOne extends Template {

    public double bucketTimeIncrement = 25;
    public double pauseTime = System.currentTimeMillis();
    public boolean bucketBack = true;
    public DcMotor.Direction direction = DcMotor.Direction.FORWARD;
    public double power;

    public boolean lb = false, rb = false;

    public void init() {
        robot.initWheels();
        robot.initComponents();
        robot.initSlides();
    }

    public void loop() {
        setBucket();
        if (rb || lb) {
            robot.slides.setPower( power );
        } else {
            robot.slides.setPower( 0 );
        }
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
    public void lb(boolean pressed) {
        if (pressed) {
            power = 0.2;
            lb = true;
        }
        lb = false;
    }

    @Override
    public void rb(boolean pressed) {
        if (pressed) {
            power = -0.2;
            rb = true;
        }
        rb = false;
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

    @Override
    public void x (boolean pressed) {
        if (pressed) {
            if (direction.equals(DcMotor.Direction.FORWARD)) {
                direction = DcMotor.Direction.REVERSE;
            } else {
                direction = DcMotor.Direction.FORWARD;
            }
            robot.spinner.setDirection(direction);
        }
    }

    @Override
    public void y (boolean pressed) {

    }

    @Override
    public void lt (float pressure) {
        robot.spinner.setPower(pressure);
    }

    public void updateTelemetryDM() {
        telemetryDM.put( "Testing", "Testing 2" );
        telemetryDM.put( "Bucket Position", String.valueOf( robot.bucket.getPosition() ) );
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
