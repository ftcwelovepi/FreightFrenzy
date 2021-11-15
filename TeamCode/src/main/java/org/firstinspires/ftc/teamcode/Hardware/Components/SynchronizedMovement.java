package org.firstinspires.ftc.teamcode.Hardware.Components;

public enum SynchronizedMovement {
    UP, DOWN, STALL, MID;

    static int stageProgression = 0;
    static SynchronizedMovement s;

    public static void move (SynchronizedMovement g) {
        s = g;
    }

    public static void run () {
        if (s.equals( UP )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( 1 );
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition( 0.7 );
                        stageProgression++;
                    }
                    break;
                case 2:
                    if (Slides.getEncoders() >= Slides.getHigh()) {
                        Bucket_Servo.glideToPosition( 1 );
                        stageProgression = 0;
                    }
                    break;
            }
        } else if (s.equals( DOWN )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( -1 );
                    Bucket_Servo.glideToPosition( 0.3 );
                    stageProgression++;
                    break;
                case 1:
                    if (Slides.getEncoders() >= Slides.getTransferPoint()) {
                        Bucket_Servo.glideToPosition(0);
                    }
                    stageProgression++;
                    break;
                case 2:
                    if (Slides.getPower() == 0) {
                        stageProgression = 0;
                        break;
                    }
            }
        } else if (s.equals( MID )) {
            switch (stageProgression) {
                case 0:
                    Slides.setPower( 1 );
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
                        stageProgression = 0;
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
