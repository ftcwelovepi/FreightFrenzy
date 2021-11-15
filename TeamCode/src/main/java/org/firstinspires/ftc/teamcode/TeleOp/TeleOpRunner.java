package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.ComponentTesting_BS;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.ComponentTesting_SL;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.FinalConfigV1;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.TeleOpOne;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.Template;

import java.util.ArrayList;

/**
 * TeleOp Runner, switches through all the configurations in org.firstinspires.ftc.teamcode.TeleOp.Configs
 * Limitations: need to manually add all the classes in
 */
@TeleOp(name="TeleOp Runner", group="FrieghtFrenzy")
public class TeleOpRunner extends OpMode {

    // declaring variables
    MecanumDriveTrain vroom;

    /* Declare OpMode members. */
    HardwareFF robot = new HardwareFF(); // use the class created to define a RoverRuckus's hardware

    /* Declare Template that will be used */
    Template framework;

    //List of templates for cycling when GP2 presses Start
    static ArrayList<Class<? extends Template>> listOfTemplates = new ArrayList<>();

    //Initialize variables for looping, switching stops GP2 from using the buttons
    boolean switching = false;
    int indexOfConfig = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

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

        //Initializing the selected Config
        framework.init();
        framework.updateTelemetryDM();

        telemetry.addData("Status", "Setting Up GP 1");
        telemetry.update();

        //initializing GP1 Functions for driving
        vroom = new MecanumDriveTrain(robot, gamepad1,telemetry);

        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        vroom.loop(); //GP 1

        //Starts teh switching configs
        if (gamepad2.start) {
            switching = true;
        }
        if (switching) {
            switchConfig();
        }
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

    @Override
    public void stop() {
        listOfTemplates = new ArrayList<>();
    }

    public void switchConfig() {
        if (Template.canPress() && gamepad2.dpad_down && indexOfConfig!=listOfTemplates.size()-1) {
            indexOfConfig++;
            Template.resetButton();
        }
        if (Template.canPress() && gamepad2.dpad_up && indexOfConfig!=0) {
            indexOfConfig--;
            Template.resetButton();
        }
        int i = 0;
        for (Class<? extends Template> frameWork: listOfTemplates) {

            telemetry.addData( (i!=indexOfConfig ? "  " : ">"), frameWork.toString().split( "\\." )[frameWork.toString().split( "\\." ).length - 1] );
            i++;
        }
        if (Template.canPress() && gamepad2.x) setConfig(indexOfConfig);

        telemetry.update();
    }

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

        framework.init();

        switching = false;
    }


}