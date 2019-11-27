package org.firstinspires.ftc.teamcode.autonomii;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.lib.AutonomousConstants;
import org.firstinspires.ftc.teamcode.robotplus.autonomous.TimeOffsetVoltage;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

@Autonomous(name = "MoveFoundation", group = "Generic")
public class MoveFoundation extends LinearOpMode implements AutonomousConstants {

    private Robot robot;
    private MecanumDrive mecanumDrive;
    private ColorSensor colorSensor;
    private DcMotor arm;
    private DcMotor elevator;
    private Servo grabber;
    private Servo assist;
    private Servo clamp;
    private TouchSensor touchSensorLeft;
    private TouchSensor touchSensorRight;
    private DistanceSensor frontDistance;


    private float hsvValues[] = {0F, 0F, 0F};
    private final double SCALE_FACTOR = 355;
    private int step = 0;

    public void runOpMode() {
        // init
        this.robot = new Robot(hardwareMap);
        this.mecanumDrive = (MecanumDrive) this.robot.getDrivetrain();
        this.colorSensor = hardwareMap.get(ColorSensor.class, "c1");
        this.arm = hardwareMap.get(DcMotor.class, "arm");
        this.elevator = hardwareMap.get(DcMotor.class, "elevator");
        this.grabber = hardwareMap.get(Servo.class, "grabber");
        this.assist = hardwareMap.get(Servo.class, "assist");
        this.clamp = hardwareMap.get(Servo.class, "clamp");
        this.touchSensorLeft = hardwareMap.get(TouchSensor.class, "left_touch");
        this.touchSensorRight = hardwareMap.get(TouchSensor.class, "right_touch");
        this.frontDistance = hardwareMap.get(DistanceSensor.class, "front_distance");

        waitForStart();


        while (opModeIsActive()) {
            // update colors
            Color.RGBToHSV((int) (colorSensor.red() * this.SCALE_FACTOR),
                    (int) (colorSensor.green() * this.SCALE_FACTOR),
                    (int) (colorSensor.blue() * this.SCALE_FACTOR),
                    this.hsvValues
            );


            telemetry.addData("Step", this.step);
            telemetry.addData("Red", this.hsvValues[0]);
            telemetry.addData("Green", this.hsvValues[1]);
            telemetry.addData("Blue", this.hsvValues[2]);
            telemetry.addData("Alpha", this.colorSensor.alpha());
            telemetry.update();

            switch (step) {
                case 0:
                    // move backwards
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(), 1, 0);
                    sleep(500);
                    this.mecanumDrive.stopMoving();
                    this.step++;
                    break;
                case 1:
                    if (!this.touchSensorRight.isPressed()) {
                        // keep moving
                        this.mecanumDrive.complexDrive(MecanumDrive.Direction.LEFT.angle(), 1, 0);
                    }
                    else {
                        this.mecanumDrive.stopMoving();
                        step++;
                    }
                    break;
                case 2:
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(), 1, 0);
                    sleep(TimeOffsetVoltage.calculateDistance(13, 27));
                    this.mecanumDrive.stopMoving();
                    step++;
                    break;
                case 3:
                    // clamp the foundation
                    this.clamp.setPosition(AutonomousConstants.CLAMP_DOWN);
                    sleep(500);
                    step++;
                    break;
                case 4:
                    // move the foundation until the distance to wall is met
                    if (this.frontDistance.getDistance(DistanceUnit.CM) > 7) {
                        this.mecanumDrive.complexDrive(MecanumDrive.Direction.DOWN.angle(), 1, 0);
                    }
                    else {
                        this.mecanumDrive.stopMoving();
                        step++;
                    }
                    break;
                case 5:
                    // move to the blue line
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.RIGHT.angle(), 1, 0);
                    sleep(TimeOffsetVoltage.calculateDistance(13, 40));
                    this.mecanumDrive.stopMoving();
                    step++;
                    break;
            }
        }
    }
}