package org.firstinspires.ftc.teamcode.teleop;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.teleop.enums.Alliance;
import org.firstinspires.ftc.teamcode.teleop.enums.IntakeState;
import org.firstinspires.ftc.teamcode.teleop.enums.MultiplierSelection;

@TeleOp(name = "Primary TeleOp", group = "TeleOp")
public class PrimaryTeleOp extends OpMode {
    // Robot Hardware & Gamepad Controller
    private HWC robot;

    // Enums
    private Alliance alliance = Alliance.RED;
    private MultiplierSelection selection = MultiplierSelection.TURN_SPEED;
    private IntakeState intakeState = IntakeState.STOPPED;

    // Drive Variables
    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double strafeSpeed = 0.5;

    // Testing Mode Boolean (for extra telemetry information)
    private boolean testingMode = false;

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

        // Speed Multiplier Selection
        if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
            selection = MultiplierSelection.TURN_SPEED;
        } else if (robot.currentGamepad1.circle && !robot.previousGamepad1.circle) {
            selection = MultiplierSelection.DRIVE_SPEED;
        } else if (robot.currentGamepad1.square && !robot.previousGamepad1.square) {
            selection = MultiplierSelection.STRAFE_SPEED;
        }

        // Speed Multiplier Changes
        switch (selection) {
            case TURN_SPEED:
                if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
                    turnSpeed += 0.1;
                } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
                    turnSpeed -= 0.1;
                }
                break;
            case DRIVE_SPEED:
                if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
                    driveSpeed += 0.1;
                } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
                    driveSpeed -= 0.1;
                }
                break;
            case STRAFE_SPEED:
                if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
                    strafeSpeed += 0.1;
                } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
                    strafeSpeed -= 0.1;
                }
                break;
        }

        // Alliance Switching
        if (robot.currentGamepad1.touchpad && !robot.previousGamepad1.touchpad) {
            alliance = (alliance == Alliance.RED) ? Alliance.BLUE : Alliance.RED;
        }

        // Enabling Testing Mode
        if ((robot.currentGamepad1.left_bumper && !robot.previousGamepad1.left_bumper) && (robot.currentGamepad1.right_bumper && !robot.previousGamepad1.right_bumper)) {
            testingMode = !testingMode;
        }

        // Telemetry
        if (testingMode) {
            telemetry.addData("> TESTING MODE IS ENABLED", "Extra Telemetry is Displayed");
            telemetry.addLine();
        }

        telemetry.addData("> STATUS", "Initialization Loop");
        telemetry.addData("> ALLIANCE", alliance);
        telemetry.addLine();
        telemetry.addData("> CONTROLS", "");
        telemetry.addData("    - Press \"TOUCHPAD\" to switch alliance", "");
        telemetry.addData("    - Press \"CROSS\" to start changing turn speed", "");
        telemetry.addData("    - Press \"CIRCLE\" to start changing drive speed", "");
        telemetry.addData("    - Press \"SQUARE\" to start changing strafe speed", "");
        telemetry.addLine();
        telemetry.addData("> SPEED MULTIPLIERS - Currently Modifying", selection);
        telemetry.addData("    - Turn Speed", turnSpeed);
        telemetry.addData("    - Drive Speed", driveSpeed);
        telemetry.addData("    - Strafe Speed", strafeSpeed);
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

        // Drive control variables
        double drive = -robot.currentGamepad1.left_stick_y;
        double strafe = robot.currentGamepad1.left_stick_x;
        double turn = (robot.currentGamepad1.left_trigger - robot.currentGamepad1.right_trigger) * turnSpeed;
        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);
        double frontLeftPower = (turn - strafe - drive) / denominator;
        double backLeftPower = (turn + strafe - drive) / denominator;
        double frontRightPower = (turn - strafe + drive) / denominator;
        double backRightPower = (turn + strafe + drive) / denominator;

        // Intake controls
        if (robot.currentGamepad1.right_bumper && !robot.previousGamepad1.right_bumper) {
            intakeState = (intakeState == IntakeState.STOPPED) ? IntakeState.INTAKE : IntakeState.STOPPED;
        }
        if (robot.currentGamepad1.left_bumper && !robot.previousGamepad1.left_bumper) {
            intakeState = (intakeState == IntakeState.STOPPED) ? IntakeState.OUTTAKE : IntakeState.STOPPED;
        }

        // Run intake (based on intakeState)
        switch (intakeState) {
            case INTAKE:
                robot.intakeBeltServo.setPower(-1); // Set Belt Power
                robot.intakeWheelServo.setPower(-1); // Set Gecko Wheel Power
                detectIntake(); // Detect Intake
                break;
            case OUTTAKE:
                robot.intakeBeltServo.setPower(1); // Set Belt Power
                robot.intakeWheelServo.setPower(1); // Set Gecko Wheel Power
                break;
            case STOPPED:
                robot.intakeBeltServo.setPower(0); // Set Belt Power
                robot.intakeWheelServo.setPower(0); // Set Gecko Wheel Power
                break;
        }

        // Run motors
        robot.leftFront.setPower(frontLeftPower);
        robot.leftRear.setPower(backLeftPower);
        robot.rightFront.setPower(frontRightPower);
        robot.rightRear.setPower(backRightPower);

        // Telemetry updates
        telemetry.addData("> STATUS", "Running");
        telemetry.addData("> ALLIANCE", alliance);
        if (testingMode) {
            telemetry.addLine();
            telemetry.addLine("------------------------");
            telemetry.addData("MOTOR POWERS", "");
            telemetry.addData("    - Front Left", frontLeftPower);
            telemetry.addData("    - Back Left", backLeftPower);
            telemetry.addData("    - Front Right", frontRightPower);
            telemetry.addData("    - Back Right", backRightPower);
            telemetry.addData("SERVO POWER/POSITIONS", "");
            telemetry.addData("    - Intake Belt Servo Power", robot.intakeBeltServo.getPower());
            telemetry.addData("    - Intake Wheel Servo Power", robot.intakeWheelServo.getPower());
            telemetry.addData("    - Specimen Servo Position", robot.specimenServo.getPosition());
            telemetry.addData("ENUMS", "");
            telemetry.addData("    - Intake State", intakeState);
            telemetry.addData("    - Multiplier Selection", selection);
            telemetry.addData("    - Alliance", alliance);
        }

        telemetry.update();
    }

    // Helper function to detect if the intake is full
    private void detectIntake() {
        int color = robot.intakeColorSensor.argb();
        boolean isYellow = (color == Color.YELLOW);
        boolean isAllianceColor = (alliance == Alliance.RED && color == Color.RED) || (alliance == Alliance.BLUE && color == Color.BLUE);
        boolean isOppositeAllianceColor = (alliance == Alliance.RED && color == Color.BLUE) || (alliance == Alliance.BLUE && color == Color.RED);

        if (isYellow || isAllianceColor) {
            intakeState = IntakeState.STOPPED;
        } else if (isOppositeAllianceColor) {
            intakeState = IntakeState.OUTTAKE;
        }
    }
}
