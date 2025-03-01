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
import org.firstinspires.ftc.teamcode.subsystems.roadrunner.drive.SampleMecanumDrive;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.pid.components.MotorPIDComponent;
import org.firstinspires.ftc.teamcode.subsystems.pid.components.ServoPIDComponent;

public class HWC {
    // Motors
    public DcMotorEx leftFront, rightFront, leftRear, rightRear;
    public DcMotorEx turretMotor, vSlideLeftMotor, vSlideRightMotor;

    // Motor PID Components
    public MotorPIDComponent vSlideLeftComponent, vSlideRightComponent, turretComponent;

    // Servos
    public Servo specimenClawServo, bucketServo, swingServoLeft, swingServoRight, hSlideLeftServo, hSlideRightServo;;

    // CR Servos
    public CRServo intakeServo1, intakeServo2;

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
    public static int[] vSlidePositions = {0, -600, -1300, -1850, -2800, -3900}; // (In order): Zero, Low Chamber, Side Panel Clearance, High Chamber, Low Basket, High Basket
    public static int[] turretPositions = {-350, 0}; // (In order): Bucket Inwards, Initialize, Bucket Outwards
    public static double[] hSlideLeftPositions = {0.9, 0.75, 0.2, 0}; // (In order): Retracted, Transfer, Halfway, Extension
    public static double[] hSlideRightPositions = {0.15, 0.35, 0.8, 1}; // (In order): Retracted, Transfer, Halfway, Extension

    // Other Variables
    Telemetry telemetry;
    public SampleMecanumDrive drive;

    // Constructor
    public HWC(@NonNull HardwareMap hardwareMap, Telemetry telemetry) {
        drive = new SampleMecanumDrive(hardwareMap);

        this.telemetry = telemetry;

        // Motors
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor"); // 117 RPM
        vSlideLeftMotor = hardwareMap.get(DcMotorEx.class, "vSlideLeftMotor"); // 223 RPM - 751.8 PPR
        vSlideRightMotor = hardwareMap.get(DcMotorEx.class, "vSlideRightMotor"); // 223 RPM - 751.8 PPR

        // Motor PID Components
        vSlideLeftComponent = new MotorPIDComponent(vSlideLeftMotor, 0.7,751.8, 0.004, 0, 0, 0);
        vSlideRightComponent = new MotorPIDComponent(vSlideRightMotor, 0.7, 751.8, 0.004, 0, 0, 0);
        turretComponent = new MotorPIDComponent(turretMotor, .4,1425.1, 0.003, 0, 0.0002, 0.0001);

        // CRServos
        intakeServo1 = hardwareMap.get(CRServo.class, "intakeServo1");
        intakeServo2 = hardwareMap.get(CRServo.class, "intakeServo2");

        // Servos
        specimenClawServo = hardwareMap.get(Servo.class, "specimenClawServo");
        bucketServo = hardwareMap.get(Servo.class, "bucketServo");
        swingServoLeft = hardwareMap.get(Servo.class, "swingServoLeft");
        swingServoRight = hardwareMap.get(Servo.class, "swingServoRight");
        hSlideLeftServo = hardwareMap.get(Servo.class, "hSlideLeftServo");
        hSlideRightServo = hardwareMap.get(Servo.class, "hSlideRightServo");

        // Sensors
        intakeColorSensor = hardwareMap.get(ColorRangeSensor.class, "intakeColorSensor");

        // Set the direction of motors
//        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
//        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
//        leftRear.setDirection(DcMotorEx.Direction.FORWARD);
//        rightRear.setDirection(DcMotorEx.Direction.REVERSE);
        vSlideRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset motor encoders
        vSlideRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        vSlideLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // Run ALL motors without encoders
        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        vSlideLeftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        vSlideRightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
}