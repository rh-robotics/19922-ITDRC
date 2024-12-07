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
import org.firstinspires.ftc.teamcode.subsystems.pid.PIDComponent;

public class HWC {
    // Declare empty variables for robot hardware
    public DcMotorEx leftFront, rightFront, leftRear, rightRear, turretMotor, linearActuatorMotor, slideLeftMotor, slideRightMotor;
    public Servo specimenClawServo, swingServoLeft, swingServoRight, intakeVerticalJointServo;
    public CRServo intakeBeltServo, intakeWheelServo, swingExtensionLeft, swingExtensionRight;
    public ColorRangeSensor intakeColorSensor, specimenColorSensor;

    // Declare PID Components
    public PIDComponent slideLeftComponent, slideRightComponent, turretComponent;

    // Declare Gamepads
    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();

    // Time Variables
    public ElapsedTime time = new ElapsedTime();

    // Position Variables
    public static int[] slidePositions = {0, -1, -2}; // TODO: Update Values With Actual Slide Positions
    public static int[] turretPositions = {0, -1}; // TODO: Update Values With Actual Turret Positions

    // Other Variables
    Telemetry telemetry;

    // Constructor
    public HWC(@NonNull HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        // Drivetrain Motors (435 RPM GoBilda REX Shaft Motors)
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

        // Other Motors
        turretMotor = hardwareMap.get(DcMotorEx.class, "turretMotor"); //
        linearActuatorMotor = hardwareMap.get(DcMotorEx.class, "linearActuatorMotor");
        slideLeftMotor = hardwareMap.get(DcMotorEx.class, "slideLeftMotor"); // 223 RPM - 751.8 PPR
        slideRightMotor = hardwareMap.get(DcMotorEx.class, "slideRightMotor"); // 223 RPM - 751.8 PPR

        // Set the direction of motors
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);

        // Run ALL motors without encoders
        leftFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        linearActuatorMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slideLeftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slideRightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // PID Components
        slideLeftComponent = new PIDComponent(slideLeftMotor, 751.8, 0.1, 0, 0, 0); // TODO: Tune Values
        slideRightComponent = new PIDComponent(slideRightMotor, 751.8, 0.1, 0, 0, 0); // TODO: Tune Values
        turretComponent = new PIDComponent(turretMotor, 0, 0.1, 0, 0, 0); // TODO: Tune Values & Update Ticks Per Rotation

        // Declare Servos
        specimenClawServo = hardwareMap.get(Servo.class, "specimenClawServo");
        swingServoLeft = hardwareMap.get(Servo.class, "swingServoLeft");
        swingServoRight = hardwareMap.get(Servo.class, "swingServoRight");
        intakeBeltServo = hardwareMap.get(CRServo.class, "intakeBeltServo");
        intakeWheelServo = hardwareMap.get(CRServo.class, "intakeWheelServo");
        swingExtensionLeft = hardwareMap.get(CRServo.class, "swingExtensionLeft");
        swingExtensionRight = hardwareMap.get(CRServo.class, "swingExtensionRight");

        // Declare Sensors
        intakeColorSensor = hardwareMap.get(ColorRangeSensor.class, "intakeColorSensor");
        specimenColorSensor = hardwareMap.get(ColorRangeSensor.class, "specimenColorSensor");
    }
}