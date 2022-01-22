package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.Components.Bucket_Servo;
import org.firstinspires.ftc.teamcode.Hardware.Components.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Components.SynchronizedMovement;
import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDriveTrainUsingCustomBraking;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.ComponentTesting_BS;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.ComponentTesting_SL;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.FinalConfigV1;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.TeleOpOne;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.Template;
import org.firstinspires.ftc.teamcode.ThreadedWait;

import java.util.ArrayList;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;

/**
 * TeleOp Runner, switches through all the configurations in org.firstinspires.ftc.teamcode.TeleOp.Configs
 * Limitations: need to manually add all the classes in
 */
@TeleOp(name="TeleOp Runner", group="FrieghtFrenzy")
public class TeleOpRunner extends OpMode {

    static final double     COUNTS_PER_MOTOR_REV    = /*767.2*/ 383.5 ;
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_CM       = 9.6 ;     // This measurement is more exact than inches
    static final double     COUNTS_PER_CM         = ((COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * Math.PI));
    static final double     HEADING_THRESHOLD       = 1 ;
    static final double     P_TURN_COEFF            = 0.03;
    private DigitalChannel redLED;
    private DigitalChannel greenLED;

    // declaring variables
    MecanumDriveTrainUsingCustomBraking vroom;


    /* Declare OpMode members. */
    HardwareFF robot = new HardwareFF(); // use the class created to define a RoverRuckus's hardware

    /* Declare Template that will be used */
    Template framework;

    //List of templates for cycling when GP2 presses Start
    static ArrayList<Class<? extends Template>> listOfTemplates = new ArrayList<>();

    //Initialize variables for looping, switching stops GP2 from using the buttons
    boolean switching = false;
    int indexOfConfig = 0;

    boolean hasBlock;

    private DistanceSensor sensorRange;

    double startingAngle;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        redLED = hardwareMap.get(DigitalChannel.class, "red");
        greenLED = hardwareMap.get(DigitalChannel.class, "green");

        redLED.setMode(DigitalChannel.Mode.OUTPUT);
        //Sets up the hardwaremap
        robot.setHardwareMap( hardwareMap );

        telemetry.addData("Status", "Setting Up GP2");
        telemetry.update();

        //This is for teh configurations
        //Configs extend the Template class and use a common hardwareMap
        Template.setHardwareMap(robot);

        //Adding all the classes
        listOfTemplates.add( TeleOpOne.class );
        listOfTemplates.add( FinalConfigV1.class );
        listOfTemplates.add( ComponentTesting_BS.class );
        listOfTemplates.add( ComponentTesting_SL.class );

        //First configuration that pops up
        framework = new FinalConfigV1();
        robot.initImu();

        robot.initLED();

        //Initializing the selected Config
        framework.init();
        framework.updateTelemetryDM();

        telemetry.addData("Status", "Setting Up GP 1");
        telemetry.update();

        //initializing GP1 Functions for driving
        vroom = new MecanumDriveTrainUsingCustomBraking(robot, gamepad1,telemetry);

        startingAngle = getAverageGyro();

        telemetry.addData( "Slides Initialized Position", Slides.getEncoders() );
        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
    }

    @Override
    public void start() {
        SynchronizedMovement.reset();
    }

    @Override
    public void loop() {
        vroom.loop(); //GP 1

        if (sensorRange.getDistance(DistanceUnit.MM) < 20) {
            if (!hasBlock) {
                hasBlock = true;
                greenLED.setState(true);
                redLED.setState(false);
                telemetry.addData("block", "not has block");
            }
        }else{
            if (hasBlock) {
                hasBlock = false;
                redLED.setState(true);
                greenLED.setState(false);
                telemetry.addData("block", "has block");
            }
        }
        telemetry.update();
        if (gamepad1.a) {
            gyroTurn( 0.7, startingAngle );
        }
        if (gamepad1.b) {
            startingAngle = getAverageGyro();
        }

        //Starts the switching configs
        if (gamepad2.start) {
            switching = true;
        }
        if (switching) {
            switchConfig();
        }
        /* Can Press Method is button Timeout
        * When button is pressed input is given as pulses */
        else if(Template.canPress()) {
            framework.a( gamepad2.a );
            framework.b( gamepad2.b );
            framework.x( gamepad2.x );
            framework.y( gamepad2.y );

            framework.dd( gamepad2.dpad_down );
            framework.du( gamepad2.dpad_up );
            framework.dl( gamepad2.dpad_left );
            framework.dr( gamepad2.dpad_right );

            framework.lt( gamepad2.left_trigger );
            framework.rt( gamepad2.right_trigger );

            framework.rb( gamepad2.right_bumper );
            framework.lb( gamepad2.left_bumper );

            framework.ljoyb( gamepad2.left_stick_button );
            framework.rjoyb( gamepad2.right_stick_button );

            framework.updateTelemetryDM();

            for (String key: framework.telemetryDM.keySet()) {
                telemetry.addData( key, framework.telemetryDM.get( key ) );
            }
            telemetry.addData( "Slides", String.valueOf( robot.slides.getPower() ) );
            telemetry.update();
            Template.resetButton();
        }

        if (!switching) {
            framework.loop();
            framework.rjoy( gamepad2.right_stick_x, gamepad2.right_stick_y );
            framework.ljoy( gamepad2.left_stick_x, gamepad2.left_stick_y );
        }


    }

    //Clear List once Opmode is stopped
    @Override
    public void stop() {
        listOfTemplates = new ArrayList<>();
        SynchronizedMovement.move(SynchronizedMovement.STALL);
    }

    //Method for Switching Configurations
    public void switchConfig() {
        //Move Up and Down (index) on Telemetry using D-UP and D-DOWN
        if (Template.canPress() && gamepad2.dpad_down && indexOfConfig!=listOfTemplates.size()-1) {
            indexOfConfig++;
            Template.resetButton();
        }
        if (Template.canPress() && gamepad2.dpad_up && indexOfConfig!=0) {
            indexOfConfig--;
            Template.resetButton();
        }
        int i = 0;
        //Display all Configs with selected one
        for (Class<? extends Template> frameWork: listOfTemplates) {

            telemetry.addData( (i!=indexOfConfig ? "  " : ">"), frameWork.toString().split( "\\." )[frameWork.toString().split( "\\." ).length - 1] );
            i++;
        }
        if (Template.canPress() && gamepad2.x) setConfig(indexOfConfig);

        telemetry.update();
    }

    //Selects the Configuration
    //Pre condition: index must be within the size of listoftemplates
    public void setConfig(int index) {
        switch (index) {
            case 0:
                framework = new TeleOpOne();
                break;
            case 1:
                framework = new FinalConfigV1();
                break;
            case 2:
                framework = new ComponentTesting_BS();
                break;
            case 3:
                framework = new ComponentTesting_SL();
                break;
        }

        //Initialize the framework
        framework.init();

        switching = false;
    }

    public double getAverageGyro(){
        /*int sum = robot.realgyro.getIntegratedZValue() + robot.realgyro2.getIntegratedZValue();
        return sum/2;*/
        Orientation angles = robot.imu.getAngularOrientation( AxesReference.INTRINSIC, AxesOrder.ZYX, DEGREES);
        double heading = angles.firstAngle;
        return heading;
    }

    public void gyroTurn (  double speed, double angle) {
        telemetry.addData("starting angle", getAverageGyro());
        telemetry.update();
        ThreadedWait wait = new ThreadedWait( 500 );
        wait.start();
        // keep looping while we are still active, and not on heading.
        while (!wait.get() || !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.addData("current_heading", getAverageGyro());
            telemetry.addData( "Timer", wait.time() );
            telemetry.update();
        }
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

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - getAverageGyro();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }
}