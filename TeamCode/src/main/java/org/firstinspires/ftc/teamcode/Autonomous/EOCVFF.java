package org.firstinspires.ftc.teamcode.Autonomous;

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


@TeleOp(name="EOCVFF", group="UltimateGoal")
public class EOCVFF extends LinearOpMode {

    SkystoneDeterminationPipeline pipeline;

    @Override
    public void runOpMode()
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
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("", pipeline.position);
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(200);
        }
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

        static final int ANCHOR_HEIGHT = 60;
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(0,ANCHOR_HEIGHT);
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(107,ANCHOR_HEIGHT);
        static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(214,ANCHOR_HEIGHT);


        static final int REGION_WIDTH = 104;
        static final int REGION_HEIGHT = 70;




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

        int avg1;
        int avg2;
        int avg3;

        Mat dst1 = new Mat();
        Mat dst11 = new Mat();

        Mat dst2 = new Mat();



        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile DuckPosition position = DuckPosition.MIDDLE;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {
//            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
//            Core.extractChannel(YCrCb, Cb, 1);

           Imgproc.cvtColor(input, dst1, Imgproc.COLOR_RGB2HSV_FULL);

//            Core.inRange(dst1, new Scalar(50, 0.3, 0.3), new Scalar(65, 1, 1), dst2);
//            Core.inRange(dst1, new Scalar(0, 0, 0), new Scalar(360, 0, 0), dst2);
//            Core.inRange(input, new Scalar(0, 0, 0), new Scalar(255, 255, 255), dst2);

            Core.inRange(dst1, new Scalar(0, 0, 0), new Scalar(10, 10, 10), dst11);
            Core.bitwise_not(dst11, dst11);

            Core.inRange(dst11, new Scalar(0, 0, 0), new Scalar(360,0,0), dst2);


        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = dst2.submat(new Rect(region1_pointA, region1_pointB));
            region2_Cb = dst2.submat(new Rect(region2_pointA, region2_pointB));
            region3_Cb = dst2.submat(new Rect(region3_pointA, region3_pointB));


        }

        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];
            avg2 = (int) Core.mean(region2_Cb).val[0];
            avg3 = (int) Core.mean(region3_Cb).val[0];


            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = DuckPosition.MIDDLE; // Record our analysis
            if(avg1 > avg2 && avg1 > avg3){
                position = DuckPosition.LEFT;
            }else if (avg2 > avg1 && avg2 > avg3){
                position = DuckPosition.MIDDLE;
            }else{
                position = DuckPosition.RIGHT;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    1); // Negative thickness means solid fill

            return dst2;
        }

        public String getAnalysis()
        {
            return "Left: " + avg1 + " Mid: " + avg2 + " Right: " + avg3;
        }
    }
}