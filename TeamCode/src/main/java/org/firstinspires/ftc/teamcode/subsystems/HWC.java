package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HWC {
    // Declare empty variables for robot hardware
    public DcMotorEx leftFront, rightFront, leftRear, rightRear;
    public Servo specimenServo;
    public CRServo intakeBeltServo, intakeWheelServo;
    public ColorRangeSensor intakeColorSensor;

    // Declare Gamepads
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();

    // Time Variables
    public ElapsedTime time = new ElapsedTime();

    // Other Variables
    Telemetry telemetry;

    // Constructor
    public HWC(@NonNull HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        // Declare motors
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

        // Set the direction of motors
        // TODO: UPDATE VALUES WITH NEW BOT
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);

        // Declare Servos
        specimenServo = hardwareMap.get(Servo.class, "specimenServo");
        intakeBeltServo = hardwareMap.get(CRServo.class, "intakeBeltServo");
        intakeWheelServo = hardwareMap.get(CRServo.class, "intakeWheelServo");

        // Declare Sensors
        intakeColorSensor = hardwareMap.get(ColorRangeSensor.class, "intakeColorSensor");
    }
}