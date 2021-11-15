package org.firstinspires.ftc.teamcode.Hardware.Components;

import org.firstinspires.ftc.teamcode.ThreadedWait;

/** Contains all the combined movement using slides and Bucket Servo
 *
 */
public enum SynchronizedMovement {
    UP, DOWN, STALL, MID, LOW;

    static int stageProgression = 0;
    static SynchronizedMovement s;

    static ThreadedWait waits = new ThreadedWait(1000);

    public static void move (SynchronizedMovement g) {
        s = g;
    }

    public static SynchronizedMovement get() {
        return s;
    }

    public static int getStage() {
        return stageProgression;
    }

    public static void run () {
        if (s.equals( UP )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( 1 );
                    Bucket_Servo.glideToPosition(0.4);
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    if (Slides.getEncoders() >= Slides.getHigh()-10) {
                        Bucket_Servo.glideToPosition( 1 );
                        stageProgression++;
                        break;
                    }
                case 3:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits.start();
                    }
                    break;
            }
        } else if (s.equals( DOWN )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( -1 );
                    Bucket_Servo.glideToPosition( 0.4 );
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()-50) {
                        Bucket_Servo.glideToPosition(0.3);
                    }
                    stageProgression++;
                    break;
                case 2:
                    if (Slides.getPower() == 0) {
                        stageProgression = 0;
                    }
                    break;
            }
        } else if (s.equals( MID )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( 1 );
                    Bucket_Servo.glideToPosition(0.4);
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    if (Slides.getEncoders() >= Slides.getMid()) {
                        Bucket_Servo.glideToPosition( 1 );
                        Slides.setPower( 0 );
                        stageProgression++;
                        break;
                    }
                case 3:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits.start();
                    }
                    break;
            }
        } else if (s.equals( LOW )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( 1 );
                    Bucket_Servo.glideToPosition( 0.4 );
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    if (Slides.getEncoders() >= Slides.getLow()) {
                        Bucket_Servo.glideToPosition( 1 );
                        Slides.setPower( 0 );
                        stageProgression++;
                        break;
                    }
                case 3:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits.start();
                    }
                    break;
            }
        } else if (s.equals( STALL )) {

        }

        if (stageProgression == 0) {
            s = STALL;
        }
    }
}
