package org.firstinspires.ftc.teamcode;

public class WaitNT {
    double timeMS;
    double targetMillisecondTime;
    /* Method that starts a timer that is not in while, returns true or false*/
    public void waitAsync(double timeMS){
        this.timeMS = timeMS;
        targetMillisecondTime = timeMS + System.currentTimeMillis();
    }
    /** precondition: You must have called the waitSync method before this!**/
    public boolean syncDone(){
        return System.currentTimeMillis() > targetMillisecondTime; // parentheses are good practice here in order to emphasize that we do something with the returned boolean as opposed to an arbitrary return statement
    }
    /* Method that starts a timer that is in a while, pauses all operations, (time out is 3 sec)  */
    public void waitSync(double timeMS){
        targetMillisecondTime = timeMS + System.currentTimeMillis();
        this.timeMS = timeMS;
        while (targetMillisecondTime > System.currentTimeMillis()) {

        }
    }
    /* Method that restarts the set timer (returns true false like first) */
    public void resetTimer(){
        targetMillisecondTime = timeMS + System.currentTimeMillis();
    }
    /* Method that runs the while(synchronous) after starting the timer as async*/
    public void asyncToSync(){
        while(targetMillisecondTime > System.currentTimeMillis()){
        }
    }
}
