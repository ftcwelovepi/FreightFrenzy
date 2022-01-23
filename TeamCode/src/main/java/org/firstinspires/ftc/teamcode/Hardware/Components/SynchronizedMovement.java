package org.firstinspires.ftc.teamcode.Hardware.Components;

import org.firstinspires.ftc.teamcode.ThreadedWait;

/** Contains all the combined movement using slides and Bucket Servo
 *
 */
public enum SynchronizedMovement {
    UP, STALL, MID, LOW;

    static int stageProgression = 0;
    static SynchronizedMovement s = SynchronizedMovement.STALL;

    static boolean startedThread = false;
    static ThreadedWait waits = new ThreadedWait( 1000 );

    public static void move(SynchronizedMovement g) {
        s = g;
    }

    public static SynchronizedMovement get() {
        return s;
    }

    public static void down() {
        stageProgression = 4;
    }

    public static int getStage() {
        return stageProgression;
    }

    //Sets the position till which the Slides move
    public static void run() {
        if (s.equals( UP )) {
            sequence( Slides.getHigh() );
        } else if (s.equals( MID )) {
            sequence( Slides.getMid() );
        } else if (s.equals( LOW )) {
            sequence( Slides.getLow() );
        }
    }

    public static void reset() {
        s = STALL;
        stageProgression = 0;
        Bucket_Servo.glideToPosition(0);
    }

    //The uniform sequence taken by the bucket and slides
    public static void sequence(double encoders) {
        switch (stageProgression) {
            case 0:
                stageProgression++;
                break;
            case 1:
                Intake.setPower( -0.5 );
                Slides.setPower( 1 );
                stageProgression++;
                break;
            case 2:
                if (Slides.getEncoders() >= encoders) {
                    Bucket_Servo.glideToPosition( 1 );
                    Slides.setPower( 0 );
                    stageProgression++;
                }
                break;
            case 3:
                if (!startedThread) {
                    waits = new ThreadedWait( 750 );
                    waits.start();
                    startedThread = true;
                }
                if (waits.get()) {
                    stageProgression++;
                    startedThread = false;
                }
                break;
            case 4:
                if (Slides.getEncoders() < 250) {
                    stageProgression = 3;
                    Slides.setPower( 1 );
                    break;
                }
                Intake.setPower( 0.2 );
                Bucket_Servo.glideToPosition( 0 );
                Slides.setPower( -1 );
                stageProgression++;
                break;
            case 5:
                if (Slides.getPower() == 0) {
                    stageProgression++;
                }
                break;
            case 6:
                if (!startedThread) {
                    waits = new ThreadedWait( 500 );
                    waits.start();
                    startedThread = true;
                }
                if (waits.get()) {
                    Intake.setPower( 0 );
                    stageProgression = 0;
                    startedThread = false;
                    s = STALL;
                }
                break;
        }
    }
}