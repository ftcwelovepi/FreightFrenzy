package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumWheels;
import org.firstinspires.ftc.teamcode.ThreadedWait;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

@Autonomous(name = "Blue Side Auto 2", group = "Freight Frenzy")
public class BlueSideAuto2 extends LinearOpMode{
    static final double     COUNTS_PER_MOTOR_REV    = /*767.2*/ 383.5 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_CM       = 9.6 ;     // This measurement is more exact than inches
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * Math.PI));
    static final double     HEADING_THRESHOLD       = 1 ;
    static final double     P_TURN_COEFF            = 0.03;
    HardwareFF robot;
    ElapsedTime runtime = new ElapsedTime();
    double startingAngle;
    public void initHardware() {
        robot = new HardwareFF();
        robot.setHardwareMap(hardwareMap);
        robot.initComponents();
        robot.initWheels();
        robot.initImu();

        sleep(50);


        telemetry.addData("Ready! ", "Let's go");    //
        telemetry.update();


    }

    public void testDrive(double speed, double distance, DcMotor input) {
        int newTarget;
        MecanumWheels wheels = new MecanumWheels();
        if (opModeIsActive()) {
            wheels.UpdateInput(0, 1, 0);
            newTarget = input.getCurrentPosition() + (int)(distance * COUNTS_PER_CM);

            input.setTargetPosition(newTarget);

            // reset the timeout time and start motion.
            runtime.reset();

            input.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            input.setPower(wheels.getRearRightPower()*speed);
            while (opModeIsActive()  && (input.isBusy())) {

                telemetry.addData("Power",input.getPower());
                telemetry.addData("current position",input.getCurrentPosition());

                telemetry.update();
            }


            input.setPower(0);




            input.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public boolean areMotorsRunning(MecanumWheels wheels){
        for (int i = 0; i < wheels.wheelPowers.length;i++){
            if (wheels.wheelPowers[i]!=0){
                switch (i){
                    case 0:
                        if (!robot.frontLeft.isBusy()) {
                            telemetry.addData("front left motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 1:
                        if (!robot.frontRight.isBusy()) {
                            telemetry.addData("front right power",  wheels.wheelPowers[i]);
                            telemetry.addData("front right motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 2:
                        if (!robot.backLeft.isBusy()){
                            telemetry.addData("back left motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                    case 3:
                        if (!robot.backRight.isBusy()) {
                            telemetry.addData("back right motor", "done");
                            telemetry.update();

                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }


    public void encoderMecanumDrive(double speed, double distance , double timeoutS, double move_x, double move_y) {
        int     newFrontLeftTarget;
        int     newFrontRightTarget;
        int     newBackLeftTarget;
        int     newBackRightTarget;
        int     frontLeftSign;
        int     frontRightSign;
        int     backLeftSign;
        int     backRightSign;
        MecanumWheels wheels = new MecanumWheels();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            wheels.UpdateInput(move_x, move_y, 0);


            frontLeftSign = (int) (Math.abs(wheels.getFrontLeftPower())/wheels.getFrontLeftPower());
            frontRightSign = (int) (Math.abs(wheels.getFrontRightPower())/wheels.getFrontRightPower());
            backLeftSign = (int) (Math.abs(wheels.getRearLeftPower()) /wheels.getRearLeftPower());
            backRightSign = (int) (Math.abs(wheels.getRearRightPower())/wheels.getRearRightPower());



            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = robot.frontLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*frontLeftSign);
            newBackLeftTarget = robot.backLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*backLeftSign);
            newFrontRightTarget = robot.frontRight.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*frontRightSign);
            newBackRightTarget = robot.backRight.getCurrentPosition() + (int)(distance * COUNTS_PER_CM*backRightSign);


            //Set target position
            robot.frontLeft.setTargetPosition(newFrontLeftTarget);
            robot.frontRight.setTargetPosition(newFrontRightTarget);
            robot.backLeft.setTargetPosition(newBackLeftTarget);
            robot.backRight.setTargetPosition(newBackRightTarget);



            // Turn On RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();
            robot.frontLeft.setPower(Math.abs(wheels.getFrontLeftPower()*speed));
            robot.frontRight.setPower(Math.abs(wheels.getFrontRightPower()*speed));
            robot.backRight.setPower(Math.abs(wheels.getRearRightPower()*speed));
            robot.backLeft.setPower(Math.abs(wheels.getRearLeftPower()*speed));



            while (opModeIsActive() && (runtime.seconds() < timeoutS) && areMotorsRunning(wheels)) {

                telemetry.addData("FrontLeftPower",robot.frontLeft.getPower());
                telemetry.addData("FrontRightPower",robot.frontRight.getPower());
                telemetry.addData("BackRightPower",robot.backRight.getPower());
                telemetry.addData("BackLeftPower",robot.backLeft.getPower());
//                telemetry.addData("front left power",  wheels.wheelPowers[0]);
//                telemetry.addData("back left power",  wheels.wheelPowers[2]);
//                telemetry.addData("back right power",  wheels.wheelPowers[3]);
//                telemetry.addData("front right power",  wheels.wheelPowers[1]);
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }



    public void gyroTurn (  double speed, double angle) {
        telemetry.addData("starting angle", getAverageGyro());
        telemetry.update();
        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.addData("current_heading", getAverageGyro());
            telemetry.update();
        }
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        robot.frontLeft.setPower(leftSpeed);
        robot.frontRight.setPower(rightSpeed);
        robot.backLeft.setPower(leftSpeed);
        robot.backRight.setPower(rightSpeed);


        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - getAverageGyro();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getAverageGyro(){
        /*int sum = robot.realgyro.getIntegratedZValue() + robot.realgyro2.getIntegratedZValue();
        return sum/2;*/
        Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, DEGREES);
        double heading = angles.firstAngle;
        return heading;
    }


    @Override
    public void runOpMode() {
        initHardware();
        initializeOpenCV();
        Slides.initialize(robot);
        Bucket_Servo.initialize(robot);

        telemetry.addData( "Autotest", "han" );
        telemetry.update();

        waitForStart();
        sleep(1500);
        telemetry.addData("Position", pipeline.position.toString());
        telemetry.update();
        sleep(500);
        if (pipeline.position== SkystoneDeterminationPipeline.DuckPosition.LEFT){
            SynchronizedMovement.move(SynchronizedMovement.LOW);
            telemetry.addData("Going with BOTTOM", "LEFT");
        }
        else if (pipeline.position== SkystoneDeterminationPipeline.DuckPosition.MIDDLE) {
            SynchronizedMovement.move(SynchronizedMovement.MID);
            telemetry.addData("Going with MID", "MIDDLE");
        }
        else {
            SynchronizedMovement.move(SynchronizedMovement.UP);
            telemetry.addData("Going with TOP", "RIGHT");
        }
        startingAngle = getAverageGyro();
        telemetry.addData("Starting angle", startingAngle);
        telemetry.update();
        if (opModeIsActive()){
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );
            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.BRAKE );

            encoderMecanumDrive(0.4, 20, 3, 1, 0);
            gyroTurn(0.4, startingAngle+60);
            encoderMecanumDrive(0.4,55,3,0,-1);

//            robot.spinner.setPower( 1 );
//            robot.intake.setPower( 0.7 );
//            sleep( 3500 );
//            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.backRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.backLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
//            robot.spinner.setPower( 0 );
//            encoderMecanumDrive(0.4, 10, 3, 0,-1);
//            robot.intake.setPower( 0 );
//
//            gyroTurn(0.7,startingAngle+40);
//            gyroTurn(0.7,90);
//            encoderMecanumDrive(0.4, 70, 3, 0,-1);
//            //extend linear slidehan
//
            while (SynchronizedMovement.get() != SynchronizedMovement.STALL) {

                SynchronizedMovement.run();
                Slides.update();
                Bucket_Servo.update();
                telemetry.addData("Stage", SynchronizedMovement.getStage());
                telemetry.addData("Encoder", Slides.getEncoders());
                telemetry.addData("Power", Slides.getPower());
                telemetry.update();
            }
            Slides.update();
            Bucket_Servo.update();
            encoderMecanumDrive(0.4,10,3,0,1);
            gyroTurn(.4,startingAngle);
            encoderMecanumDrive(.4,70,3,-1,0);
            encoderMecanumDrive(.4,110,3,0,1);
            encoderMecanumDrive(.4,60,3,1,0);
//            gyroTurn(0.4, startingAngle+180);

            //            gyroTurn(0.7, startingAngle);
//            encoderMecanumDrive(.4,135,3,-1,-0.4);
//            gyroTurn(0.7, startingAngle);
//            moveConstGyroandDist( .4, 110, -1, 0, startingAngle );
////            encoderMecanumDrive(.4,170,3,0,-1);
//            encoderMecanumDrive( .4, 50, 3, 1, -0.4 );
//            gyroTurn( 0.7, startingAngle + 180 );

        }
    }

    public void moveConstGyroandDist (double speed, double distance, double move_y, double distanceMM, double heading) {
        int increment = 1;
        double start = distance/7;
        while (opModeIsActive() && increment<=7) {
//            double movex = -(robot.distanceSensor.getDistance( DistanceUnit.MM )-distanceMM)/100;
            double dist = start;
            encoderMecanumDrive( speed, dist, 3, 0, move_y );
            gyroTurn( 0.7, heading );
            increment++;
        }
    }

    SkystoneDeterminationPipeline pipeline;

    public void initializeOpenCV()
    {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        final OpenCvCamera phoneCam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()



        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(160,120, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

//        FtcDashboard.getInstance().startCameraStream(phoneCam, 0);
//
//        while (opModeIsActive())
//        {
//            telemetry.addData("Analysis", pipeline.getAnalysis());
//            telemetry.addData("", pipeline.position);
//            telemetry.update();
//
//            // Don't burn CPU cycles busy-looping in this sample
//            sleep(200);
//        }
    }

    public static class SkystoneDeterminationPipeline extends OpenCvPipeline
    {
        /*
         * An enum to define the skystone position
         */
        public enum DuckPosition
        {
            LEFT,
            MIDDLE,
            RIGHT
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */

        static final int ANCHOR_HEIGHT = 30;
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(0,ANCHOR_HEIGHT);
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(53,ANCHOR_HEIGHT);
        static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(107,ANCHOR_HEIGHT);


        static final int REGION_WIDTH = 52;
        static final int REGION_HEIGHT = 35;




//        final int FOUR_RING_THRESHOLD = 137;
//        final int ONE_RING_THRESHOLD = 130;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region2_pointA = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y);
        Point region2_pointB = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region3_pointA = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x,
                REGION3_TOPLEFT_ANCHOR_POINT.y);
        Point region3_pointB = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat region2_Cb;
        Mat region3_Cb;

        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        int avg1red;
        int avg1green;
        int avg1blue;
        int avg2red;
        int avg2green;
        int avg2blue;
        int avg3red;
        int avg3green;
        int avg3blue;

        int targetred = 255;
        int targetblue = 0;
        int targetgreen = 255;

        int avg1Count = 0;
        int avg2Count = 0;
        int avg3Count = 0;

        Mat dst1 = new Mat();
        Mat dst11 = new Mat();

        Mat dst2 = new Mat();



        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile SkystoneDeterminationPipeline.DuckPosition position = SkystoneDeterminationPipeline.DuckPosition.MIDDLE;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {

            dst2 = input;

//            Imgproc.cvtColor(input, YCrCb, Imgproc.RGB); //ROHIT: check minimum red value
//            Core.extractChannel(YCrCb, dst2, 1);

//           Imgproc.cvtColor(input, dst1, Imgproc.COLOR_RGB2HSV_FULL);

//            Core.inRange(dst1, new Scalar(50, 0.3, 0.3), new Scalar(65, 1, 1), dst2);
//            Core.inRange(dst1, new Scalar(0, 0, 0), new Scalar(360, 0, 0), dst2);
//            Core.inRange(input, new Scalar(0, 0, 0), new Scalar(255, 255, 255), dst2);

//            Core.inRange(dst1, new Scalar(0, 0, 0), new Scalar(10, 10, 10), dst11);
//            Core.bitwise_not(dst11, dst11);
//
//            Core.inRange(dst11, new Scalar(0, 0, 0), new Scalar(360,0,0), dst2);


        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = dst2.submat(new Rect(region1_pointA, region1_pointB));
            region2_Cb = dst2.submat(new Rect(region2_pointA, region2_pointB));
            region3_Cb = dst2.submat(new Rect(region3_pointA, region3_pointB));

        }

        public int getDistance(int x1, int x2, int y1, int y2, int z1, int z2){
            return (int) Math.pow((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2) * 1.0), 0.5);
        }
        public int countPixels(double tolerance, int targetred, int targetgreen, int targetblue, Mat image){
            int counter = 0;
            for (int x = 0; x < image.rows(); x++){ //loop through rows
                for (int y = 0; y < image.cols(); y++){ // loop through columns
                    Scalar pixel = Core.mean(image.row(x).col(y));
                    int red = (int)pixel.val[0];
                    int green = (int)pixel.val[1];
                    int blue = (int)pixel.val[2];

                    if (getDistance(red,targetred,green,targetgreen,blue,targetblue)<= tolerance){
                        counter++;
                    }
                }
            }
            return counter;
        }
        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);
            double tolerance = 200;
            avg1Count = countPixels(tolerance, targetred,targetgreen,targetblue,region1_Cb);

            avg2red = (int) Core.mean(region2_Cb).val[0];
            avg2green = (int) Core.mean(region2_Cb).val[1];
            avg2blue = (int) Core.mean(region2_Cb).val[2];
            avg2Count = countPixels(tolerance, targetred,targetgreen,targetblue,region2_Cb);

            //avg2Distance = getDistance(avg2red, targetred, avg2green, targetgreen, avg2blue, targetblue);


            avg3red = (int) Core.mean(region3_Cb).val[0];
            avg3green = (int) Core.mean(region3_Cb).val[1];
            avg3blue = (int) Core.mean(region3_Cb).val[2];
            avg3Count = countPixels(tolerance, targetred,targetgreen,targetblue,region3_Cb);

            //avg3Distance = getDistance(avg3red, targetred, avg3green, targetgreen, avg3blue, targetblue);





            //avg1 = getDistance(avg1.)

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = SkystoneDeterminationPipeline.DuckPosition.MIDDLE; // Record our analysis
            if(avg1Count > avg2Count && avg1Count > avg3Count){
                position = SkystoneDeterminationPipeline.DuckPosition.LEFT;
            }else if (avg2Count > avg1Count && avg2Count > avg3Count){
                position = SkystoneDeterminationPipeline.DuckPosition.MIDDLE;
            }else{
                position = SkystoneDeterminationPipeline.DuckPosition.RIGHT;
            }

            Imgproc.rectangle(
                    dst2, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill

            Imgproc.rectangle(
                    dst2, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill

            Imgproc.rectangle(
                    dst2, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill


            return dst2;
        }

        public String getAnalysis()
        {
            return avg1Count + " " + avg2Count + " " + avg3Count;
//            return "Left: " + avg1 + " Mid: " + avg2 + " Right: " + avg3;
//           return "Left: " + avg1Distance + " Mid: " + avg2Distance + " Right: " + avg3Distance;

        }
    }
}
