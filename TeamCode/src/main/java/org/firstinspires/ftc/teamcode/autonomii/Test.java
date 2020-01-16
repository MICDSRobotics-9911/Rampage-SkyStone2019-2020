package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

@Autonomous(name = "Test")
public class Test extends LinearOpMode {
    private Servo clampLeft;
    private DigitalChannel frontSwitch;
    private double step = 0.1;

    public void runOpMode() {
        this.clampLeft = hardwareMap.get(Servo.class, "clamp_left");
        this.frontSwitch = hardwareMap.get(DigitalChannel.class, "front_switch");

        this.clampLeft.setPosition(0.1); // down
        sleep(5000);
        this.clampLeft.setPosition(0.5); // up

        waitForStart();
        //this.clampLeft.setPosition(1);

        this.sleep(2000);

        while (opModeIsActive()) {
            telemetry.addData("switch", this.frontSwitch.getState());
            this.clampLeft.setPosition(this.step);
            sleep(3000);
            this.step += 0.1;
            telemetry.addData("Position", (this.step));

            telemetry.update();
        }

        /*this.clamp.setPosition(0.5); // up position
        sleep(5000);
        this.clamp.setPosition(0); // down position
        sleep(5000);*/
    }
}

