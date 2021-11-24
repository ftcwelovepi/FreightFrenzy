package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;


@TeleOp(name="OpenCVDetection", group="UltimateGoal")
public abstract class OpenCVDetection extends LinearOpMode {

    FreightFrenzyDeterminationPipeline pipeline;


    public void initOpenCV()
    {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        final OpenCvCamera phoneCam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new FreightFrenzyDeterminationPipeline();
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

        FtcDashboard.getInstance().startCameraStream(phoneCam, 0);


        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("", pipeline.position);
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(200);
        }
    }

    public static class FreightFrenzyDeterminationPipeline extends OpenCvPipeline
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
        public volatile DuckPosition position = DuckPosition.MIDDLE;

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

            position = DuckPosition.MIDDLE; // Record our analysis
            if(avg1Count > avg2Count && avg1Count > avg3Count){
                position = DuckPosition.LEFT;
            }else if (avg2Count > avg1Count && avg2Count > avg3Count){
                position = DuckPosition.MIDDLE;
            }else{
                position = DuckPosition.RIGHT;
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
    }
}