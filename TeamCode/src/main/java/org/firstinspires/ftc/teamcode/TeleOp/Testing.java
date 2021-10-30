package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.HardwareFF;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.FinalConfigV1;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.TeleOpOne;
import org.firstinspires.ftc.teamcode.TeleOp.Configs.Template;

import java.lang.reflect.Array;
import java.util.ArrayList;

@TeleOp(name="Testing", group="UltimateGoal")
public class Testing extends OpMode {

    // declaring variables
    MecanumDriveTrain vroom;

    /* Declare OpMode members. */
    HardwareFF robot = new HardwareFF(); // use the class created to define a RoverRuckus's hardware

    /* Declare Template that will be used */
    Template framework;

    static ArrayList<Class<? extends Template>> listOfTemplates = new ArrayList<>();

    boolean switching = false;
    int indexOfConfig = 0;

    @Override
    public void init() {
        telemetry.addData("What", "Do u want");
        telemetry.update();
        telemetry.addData("Haddi", "Haddi");

        robot.setHardwareMap( hardwareMap );
        Template.setHardwareMap(robot);

        listOfTemplates.add( TeleOpOne.class );
        listOfTemplates.add( FinalConfigV1.class );

        framework = new TeleOpOne();

        framework.init();
        framework.updateTelemetryDM();

        vroom = new MecanumDriveTrain(robot, gamepad1,telemetry);

        telemetry.addData("Haddi", "Haddi");
        telemetry.update();
    }

    @Override
    public void loop() {
        vroom.loop();
        framework.loop();

        if (gamepad2.start) {
            switching = true;
        }
        if (switching) {
            switchConfig();
        }
        else if(Template.canPress()) {
            telemetry.addData( "Caption", framework.getName() );
            telemetry.update();
        }

        if (!switching) {
            framework.loop();
        }


    }

    public void switchConfig() {
        if (Template.canPress() && gamepad2.dpad_down && indexOfConfig!=listOfTemplates.size()-1) indexOfConfig++;
        if (Template.canPress() && gamepad2.dpad_up && indexOfConfig!=0) indexOfConfig--;
        int i = 0;
        for (Class<? extends Template> frameWork: listOfTemplates) {

            telemetry.addData( (i!=indexOfConfig ? " " : ">"), frameWork );
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
        }

        switching = false;
    }


}