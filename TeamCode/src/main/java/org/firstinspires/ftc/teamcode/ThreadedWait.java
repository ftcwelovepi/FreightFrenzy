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

    //Returns whether the loop has finished running or not
    //TRUE if finished
    //FALSE if started and is in the loop
    public boolean get() {
        return end;
    }

    //Returns that time that is left, countdown style
    public double time() {
        return beginTime+wait - System.currentTimeMillis();
    }

    //Starts a wait in a separate thread
    @Override
    public void run() {
        end = false;
        beginTime = System.currentTimeMillis();
        while (beginTime+wait > System.currentTimeMillis()) {

        }
        end = true;
    }
}
