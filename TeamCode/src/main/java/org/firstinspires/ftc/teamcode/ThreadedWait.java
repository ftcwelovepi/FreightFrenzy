package org.firstinspires.ftc.teamcode;

/**
 * Threaded Wait Class
 * Starts a timer that does not interrupt the main thread
 */
public class ThreadedWait extends Thread{

    private double wait;
    private boolean end;
    private double beginTime;

    public ThreadedWait(double millis) {
        wait = millis;
        end = false;
    }

    public boolean get() {
        return end;
    }

    public double time() {
        return beginTime+wait - System.currentTimeMillis();
    }

    @Override
    public void run() {
        end = false;
        beginTime = System.currentTimeMillis();
        while (beginTime+wait > System.currentTimeMillis()) {

        }
        end = true;
    }
}
