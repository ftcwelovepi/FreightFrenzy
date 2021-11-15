package org.firstinspires.ftc.teamcode;

/**
 * Threaded Wait Class
 * Starts a timer that does not interrupt the main thread
 */
public class ThreadedWait extends Thread{

    private double wait;
    private boolean end;

    public ThreadedWait(double millis) {
        wait = millis;
        end = false;
    }

    public boolean get() {
        return end;
    }

    @Override
    public void run() {
        double beginTime = System.currentTimeMillis();
        while (beginTime+wait > System.currentTimeMillis()) {

        }
        end = true;
    }
}
