package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@TeleOp(name = "Zero Servo", group = "Testing")
public class ZeroServo extends OpMode {
    // Robot Hardware & Gamepad Controller
    private HWC robot;

    enum ServoSelector {
        SWING_SERVO_LEFT,
        SWING_SERVO_RIGHT
    }

    ServoSelector servoSelector = ServoSelector.SWING_SERVO_LEFT;

    @Override
    public void init() {
        // Update Telemetry to show that the robot is initializing
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Initialize Robot Hardware
        robot = new HWC(hardwareMap, telemetry);

        // Initialize Controllers
        gamepad1.type = Gamepad.Type.SONY_PS4;
        gamepad2.type = Gamepad.Type.SONY_PS4;
        gamepad1.setLedColor(186, 142, 34, Gamepad.LED_DURATION_CONTINUOUS);
        gamepad2.setLedColor(186, 142, 34, Gamepad.LED_DURATION_CONTINUOUS);

        // Update Telemetry to show that the robot has been initialized
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // Update Gamepads
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        telemetry.addData("> STATUS", "Initialization Loop");
        telemetry.update();
    }

    @Override
    public void start() {
        robot.time.reset();
    }

    @Override
    public void loop() {
        // Update gamepads
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        // Switch between all the servos in ServoSelector enum with button 'cross'
        if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
            switch (servoSelector) {
                case SWING_SERVO_LEFT:
                    servoSelector = ServoSelector.SWING_SERVO_RIGHT;
                    break;
                case SWING_SERVO_RIGHT:
                    servoSelector = ServoSelector.SWING_SERVO_LEFT;
                    break;
            }
        }

        // Set the servo to 0 with button 'triangle'
        if (robot.currentGamepad1.triangle && !robot.previousGamepad1.triangle) {
            switch (servoSelector) {
                case SWING_SERVO_LEFT:
                    robot.swingServoLeft.setPosition(0);
                    break;
                case SWING_SERVO_RIGHT:
                    robot.swingServoRight.setPosition(0);
                    break;
            }
        }

        // Set the servo to 1 with button 'circle'
        if (robot.currentGamepad1.circle && !robot.previousGamepad1.circle) {
            switch (servoSelector) {
                case SWING_SERVO_LEFT:
                    robot.swingServoLeft.setPosition(1);
                    break;
                case SWING_SERVO_RIGHT:
                    robot.swingServoRight.setPosition(1);
                    break;
            }
        }
        



        // Telemetry updates
        telemetry.addData("> STATUS", "Running");
        telemetry.addData("> SERVO SELECTOR", servoSelector);
        telemetry.addData("> CONTROL SCHEME", "Cross: Switch Servo, Triangle: Set Servo to 0, Circle: Set Servo to 1");
        telemetry.addLine();
        telemetry.addLine("------------------------");
        telemetry.addData("SERVO POWER/POSITIONS", "");
        telemetry.addData("    - Intake Belt Servo Power", robot.intakeBeltServo.getPower());
        telemetry.addData("    - Intake Wheel Servo Power", robot.intakeWheelServo.getPower());
        telemetry.addData("    - Specimen Servo Position", robot.specimenClawServo.getPosition());
        telemetry.addData("    - Swing Servo Left Position", robot.swingServoLeft.getPosition());
        telemetry.addData("    - Swing Servo Right Position", robot.swingServoRight.getPosition());
        telemetry.update();
    }
}
