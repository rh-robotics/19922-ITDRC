package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.pid.components.MotorPIDComponent;
import org.firstinspires.ftc.teamcode.subsystems.pid.components.ServoPIDComponent;

public class HWC {
    // Motors
    public DcMotorEx leftFront, rightFront, leftRear, rightRear;
    public DcMotorEx turretMotor, linearActuatorMotor, vSlideLeftMotor, vSlideRightMotor;

    // Motor PID Components
    public MotorPIDComponent vSlideLeftComponent, vSlideRightComponent, turretComponent;

    // Servos
    public Servo specimenClawServo, bucketServo, swingServoLeft, swingServoRight;

    // CR Servos
    public CRServo intakeServo1, intakeServo2, hSlideLeftServo, hSlideRightServo;

    // Axon AnalogInput Encoders
    public AnalogInput hSlideLeftEncoder, hSlideRightEncoder;

    // Servo PID Components
    public ServoPIDComponent hSlideLeftComponent, hSlideRightComponent;

    // Sensors
    public ColorRangeSensor intakeColorSensor;

    // Gamepads
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();

    // Time
    public ElapsedTime time = new ElapsedTime();

    // Position Variables
    public static int[] vSlidePositions = {0, -1500, -5000, -8000, -12000}; // (In order): Zero, Low Chamber, Side Panel Clearance, High Chamber, Low Basket, High Basket
    public static int[] hSlidePositions = {0, 0}; // TODO: Update with actual values
    public static int[] turretPositions = {350, 0, -350};

    // Other Variables
    Telemetry telemetry;

    // Constructor
    public HWC(@NonNull HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        // Motors
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor"); // 117 RPM
        linearActuatorMotor = hardwareMap.get(DcMotorEx.class, "linearActuatorMotor");
        vSlideLeftMotor = hardwareMap.get(DcMotorEx.class, "vSlideLeftMotor"); // 223 RPM - 751.8 PPR
        vSlideRightMotor = hardwareMap.get(DcMotorEx.class, "vSlideRightMotor"); // 223 RPM - 751.8 PPR

        // Motor PID Components
        vSlideLeftComponent = new MotorPIDComponent(vSlideLeftMotor, 751.8, 0.003, 0, 0, 0.1);
        vSlideRightComponent = new MotorPIDComponent(vSlideRightMotor, 751.8, 0.003, 0, 0, 0.1);
        turretComponent = new MotorPIDComponent(turretMotor, .4,1425.1, 0.003, 0, 0.0002, 0.0001);

        // Analog Inputs
        hSlideLeftEncoder = hardwareMap.get(AnalogInput.class, "hSlideLeftEncoder");
        hSlideRightEncoder = hardwareMap.get(AnalogInput.class, "hSlideRightEncoder");

        // CRServos
        intakeServo1 = hardwareMap.get(CRServo.class, "intakeServo1");
        intakeServo2 = hardwareMap.get(CRServo.class, "intakeServo2");
        hSlideLeftServo = hardwareMap.get(CRServo.class, "hSlideLeftServo");
        hSlideRightServo = hardwareMap.get(CRServo.class, "hSlideRightServo");

        // Servo PID Components
//        hSlideLeftComponent = new ServoPIDComponent(hSlideLeftServo, hSlideLeftEncoder, 0.1, 0, 0, 0, 0); // TODO: Tune Values & Update Ticks Per Rotation
//        hSlideRightComponent = new ServoPIDComponent(hSlideRightServo, hSlideRightEncoder, 0.1, 0, 0, 0, 0); // TODO: Tune Values & Update Ticks Per Rotation

        // Servos
        specimenClawServo = hardwareMap.get(Servo.class, "specimenClawServo");
        bucketServo = hardwareMap.get(Servo.class, "bucketServo");
        swingServoLeft = hardwareMap.get(Servo.class, "swingServoLeft");
        swingServoRight = hardwareMap.get(Servo.class, "swingServoRight");

        // Sensors
        intakeColorSensor = hardwareMap.get(ColorRangeSensor.class, "intakeColorSensor");

        // Set the direction of motors
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);
        vSlideRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset motor encoders
        vSlideRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        vSlideLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        linearActuatorMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // Run ALL motors without encoders
        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        linearActuatorMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        vSlideLeftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        vSlideRightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
}