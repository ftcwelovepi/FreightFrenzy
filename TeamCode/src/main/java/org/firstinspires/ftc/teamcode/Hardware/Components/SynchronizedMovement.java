package org.firstinspires.ftc.teamcode.Hardware.Components;

import org.firstinspires.ftc.teamcode.ThreadedWait;

/** Contains all the combined movement using slides and Bucket Servo
 *
 */
public enum SynchronizedMovement {
    UP, DOWN, STALL, MID, LOW;

    static int stageProgression = 0;
    static SynchronizedMovement s = SynchronizedMovement.STALL;

    static double bucketPos, slidesPoint;

    static boolean startedThread = false;

    static ThreadedWait waits = new ThreadedWait(1000);

    public static boolean turn = true;
    public static boolean down = true;

    public static void move (SynchronizedMovement g) {
        s = g;
    }

    public static SynchronizedMovement get() {
        return s;
    }

    public static int getStage() {
        return stageProgression;
    }

    //Sets the position till which the Slides move
    public static void run () {
        if (s.equals( UP )) {
            sequence(Slides.getHigh());
        } else if (s.equals( MID )) {
            sequence(Slides.getMid());
        } else if (s.equals( LOW )) {
            sequence(Slides.getLow());
        }
        if (stageProgression==0) {
            s = STALL;
        }
    }

    //The uniform sequence taken by the bucket and slides
    public static void sequence (double encoders) {
        switch (stageProgression) {
            case 0:
                stageProgression++;
                break;
            case 1:
                Slides.setPower( 1 );
                if (turn)
                    Intake.setPower( -0.3 );
                if (Slides.getEncoders() >= 20) {
                    stageProgression++;
                }
                break;
            case 2:
                Bucket_Servo.glideToPosition( 0.7 );
                stageProgression++;
                break;
            case 3:
                if (Slides.getEncoders() >= Slides.getTransferPoint()) {
//                    Bucket_Servo.glideToPosition( 0.8 );
                    if (turn)
                        Intake.setPower( 0 );
                    stageProgression++;
                }
                break;
            case 4:
                if (Slides.getEncoders() >= encoders) {
                    Bucket_Servo.glideToPosition( 1 );
                    Slides.setPower(0);
                    stageProgression++;
                }
                break;
            case 5:
                if (!startedThread) {
                    waits = new ThreadedWait( 500 );
                    waits.start();
                    startedThread = true;
                }
                if (waits.get()) {
                    stageProgression++;
                    startedThread = false;
                }
                break;
            case 6:
                Slides.setPower( -1 );
                Bucket_Servo.glideToPosition( 0.5 );
                stageProgression++;
                break;
            case 7:
                if (Slides.getEncoders() <= Slides.getTransferPoint()) {
                    Intake.setPower( -0.2 );
                    Bucket_Servo.glideToPosition( 0.3 );
                    stageProgression++;
                }
                break;
            case 8:
                if (Slides.getPower() == 0) {
                    Intake.setPower( 0 );
                    stageProgression = 0;
                }
                break;
        }
    }

    public static void run2 () {
        if (s.equals( UP )) {
            switch (stageProgression) {
                case 0:
                    stageProgression++;
                    break;
                case 1:
                    Slides.setPower( 1 );
                    if (Slides.getEncoders() >= 10) {
                        Intake.setPower( -0.2 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    Bucket_Servo.glideToPosition(0.5);
                    stageProgression++;
                    break;
                case 3:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        Intake.setPower( 0 );
                        stageProgression++;
                    }
                    break;
                case 4:
                    if (Slides.getEncoders() >= Slides.getHigh()-10) {
                        Bucket_Servo.glideToPosition( 1 );
                        stageProgression++;
                    }
                    break;
                case 5:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits = new ThreadedWait(1000);
                        waits.start();
                    }
                    break;
            }
        } else if (s.equals( DOWN )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( -1 );
                    Bucket_Servo.glideToPosition( 0.5 );
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() <= Slides.getTransferPoint()-50) {
                        Intake.setPower( -0.2 );
                        Bucket_Servo.glideToPosition( 0.3 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    if (Slides.getPower() == 0) {
                        Intake.setPower( 0 );
                        stageProgression = 0;
                    }
                    break;
            }
        } else if (s.equals( MID )) {
            switch (stageProgression) {
                case 0:
                    stageProgression++;
                    break;
                case 1:
                    Slides.setPower( 1 );
                    if (Slides.getEncoders() >= 10) {
                        Intake.setPower( -0.2 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    Bucket_Servo.glideToPosition(0.5);
                    stageProgression++;
                    break;
                case 3:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        Intake.setPower( 0 );
                        stageProgression++;
                    }
                    break;
                case 4:
                    if (Slides.getEncoders() >= Slides.getMid()) {
                        Bucket_Servo.glideToPosition( 1 );
                        Slides.setPower( 0 );
                        stageProgression++;
                    }
                    break;
                case 5:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits = new ThreadedWait(1000);
                        waits.start();
                    }
                    break;
            }
        } else if (s.equals( LOW )) {
            switch (stageProgression) {
                case 0:
                    stageProgression++;
                    break;
                case 1:
                    Slides.setPower( 1 );
                    if (Slides.getEncoders() >= 10) {
                        Intake.setPower( -0.2 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    Bucket_Servo.glideToPosition( 0.5 );
                    stageProgression++;
                    break;
                case 3:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        Intake.setPower( 0 );
                        stageProgression++;
                    }
                    break;
                case 4:
                    if (Slides.getEncoders() >= Slides.getLow()) {
                        Bucket_Servo.glideToPosition( 1 );
                        Slides.setPower( 0 );
                        stageProgression++;
                    }
                    break;
                case 5:
                    if (waits.get()) {
                        stageProgression = 0;
                    } else if (!waits.isAlive() && !waits.get()) {
                        waits = new ThreadedWait(1000);
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
