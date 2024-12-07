package org.firstinspires.ftc.teamcode.teleop;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.teleop.enums.Alliance;
import org.firstinspires.ftc.teamcode.teleop.enums.IntakeState;
import org.firstinspires.ftc.teamcode.teleop.enums.MultiplierSelection;
import org.firstinspires.ftc.teamcode.teleop.enums.SwingIntakeState;

// TODO: Test Slide Controls
// TODO: Test Turret Controls
// TODO: Test Linear Actuator Controls
// TODO: Test Intake Controls w/ Sensor
// TODO: Test Alliance Switching
// TODO: Test Reversed Drive Motors (so its actually forward now)
// TODO: Test Swing and Intake Extension/Retraction
// TODO: Test Swing Linear Extension

@TeleOp(name = "Primary TeleOp", group = "TeleOp")
public class PrimaryTeleOp extends OpMode {
    // Robot Hardware
    private HWC robot;

    // Enums
    private Alliance alliance = Alliance.RED;
    private MultiplierSelection selection = MultiplierSelection.TURN_SPEED;
    private IntakeState intakeState = IntakeState.STOPPED;
    private SwingIntakeState swingIntakeState = SwingIntakeState.SWING_RETRACTED;

    // Drive Variables
    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double strafeSpeed = 0.5;

    // Other Variables
    private int slidePositionIndex = 0;
    private int turretPositionIndex = 0;

    // Servo Positions
    // TODO: Update Servo Positions
    private static final double SWING_EXTENDED_LEFT_POSITION = 1.0;
    private static final double SWING_EXTENDED_RIGHT_POSITION = 0.0;
    private static final double SWING_RETRACTED_LEFT_POSITION = 0.0;
    private static final double SWING_RETRACTED_RIGHT_POSITION = 1.0;
    private static final double INTAKE_VERTICAL_JOINT_EXTENDED_POSITION = 1.0;
    private static final double INTAKE_VERTICAL_JOINT_RETRACTED_POSITION = 0.0;

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
        gamepad1.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);
        gamepad2.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);

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

        // Slide Position Controls
        if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
            // Increment Value
            slidePositionIndex++;

            // If value is above the max, don't increase
            if (slidePositionIndex > HWC.slidePositions.length - 1) {
                slidePositionIndex = HWC.slidePositions.length - 1;
            }
        } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
            // Decrement Value
            slidePositionIndex--;

            // If value is below the min, don't decrease
            if (slidePositionIndex < 0) {
                slidePositionIndex = 0;
            }
        }

        // Backup Controls to Reset Slide Encoders
        if ((robot.currentGamepad1.right_stick_button && !robot.previousGamepad1.right_stick_button) || (robot.currentGamepad2.right_stick_button && !robot.previousGamepad1.right_stick_button)) {
            resetSlideEncoders();
        }

        // Turret Position Controls
        if (robot.currentGamepad1.dpad_left && !robot.previousGamepad1.dpad_left) {
            // Increment Value
            turretPositionIndex++;

            // If value is above the max, don't increase
            if (turretPositionIndex > HWC.turretPositions.length - 1) {
                turretPositionIndex = HWC.turretPositions.length - 1;
            }
        } else if (robot.currentGamepad1.dpad_right && !robot.previousGamepad1.dpad_right) {
            // Decrement Value
            turretPositionIndex--;

            // If value is below the min, don't decrease
            if (turretPositionIndex < 0) {
                turretPositionIndex = 0;
            }
        }

        // Backup Controls to Reset Turret Encoder
        if ((robot.currentGamepad1.left_stick_button && !robot.previousGamepad1.left_stick_button) || (robot.currentGamepad2.left_stick_button && !robot.previousGamepad1.left_stick_button)) {
            resetTurretEncoder();
        }

        // Swing and Intake State Machine
        switch (swingIntakeState) {
            case SWING_RETRACTED:
                if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
                    swingIntakeState = SwingIntakeState.SWING_EXTENDING;
                }
                break;
            case SWING_EXTENDING:
                robot.swingServoLeft.setPosition(SWING_EXTENDED_LEFT_POSITION);
                robot.swingServoRight.setPosition(SWING_EXTENDED_RIGHT_POSITION);
                if (robot.swingServoLeft.getPosition() == SWING_EXTENDED_LEFT_POSITION && robot.swingServoRight.getPosition() == SWING_EXTENDED_RIGHT_POSITION) {
                    swingIntakeState = SwingIntakeState.INTAKE_EXTENDING;
                }
                break;
            case INTAKE_EXTENDING:
                robot.intakeVerticalJointServo.setPosition(INTAKE_VERTICAL_JOINT_EXTENDED_POSITION);
                if (robot.intakeVerticalJointServo.getPosition() == INTAKE_VERTICAL_JOINT_EXTENDED_POSITION) {
                    swingIntakeState = SwingIntakeState.SWING_EXTENDED;
                }
                break;
            case SWING_EXTENDED:
                if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
                    swingIntakeState = SwingIntakeState.INTAKE_RETRACTING;
                }
                break;
            case INTAKE_RETRACTING:
                robot.intakeVerticalJointServo.setPosition(INTAKE_VERTICAL_JOINT_RETRACTED_POSITION);
                if (robot.intakeVerticalJointServo.getPosition() == INTAKE_VERTICAL_JOINT_RETRACTED_POSITION) {
                    swingIntakeState = SwingIntakeState.SWING_RETRACTING;
                }
                break;
            case SWING_RETRACTING:
                robot.swingServoLeft.setPosition(SWING_RETRACTED_LEFT_POSITION);
                robot.swingServoRight.setPosition(SWING_RETRACTED_RIGHT_POSITION);
                if (robot.swingServoLeft.getPosition() == SWING_RETRACTED_LEFT_POSITION && robot.swingServoRight.getPosition() == SWING_RETRACTED_RIGHT_POSITION) {
                    swingIntakeState = SwingIntakeState.SWING_RETRACTED;
                }
                break;
        }

        // Linear Actuator Controls
        if (robot.currentGamepad1.options && !robot.previousGamepad1.options) {
            // Switch between a power of 1, 0, and -1
            robot.linearActuatorMotor.setPower((robot.linearActuatorMotor.getPower() == 1) ? 0 : (robot.linearActuatorMotor.getPower() == 0) ? -1 : 1);
        }

        // Specimen Claw Controls
        if (robot.currentGamepad1.share && !robot.previousGamepad1.share) {
            // Switch between a position of 1 and 0
            robot.specimenClawServo.setPosition((robot.specimenClawServo.getPosition() == 1) ? 0 : 1);
        }

        // Swing Linear Extension Controls
        // TODO: Update Servo Power Signs
        robot.swingExtensionLeft.setPower(robot.currentGamepad1.left_stick_y);
        robot.swingExtensionRight.setPower(-robot.currentGamepad1.right_stick_y);

        // Set Slide Target Positions
        robot.slideLeftComponent.setTarget(HWC.slidePositions[slidePositionIndex]);
        robot.slideRightComponent.setTarget(HWC.slidePositions[slidePositionIndex]);

        // Run Slides Using PID
        robot.slideLeftComponent.moveUsingPID();
        robot.slideRightComponent.moveUsingPID();

        // Set Turret Target Positions
        robot.turretComponent.setTarget(HWC.turretPositions[turretPositionIndex]);

        // Run Turret Using PID
        robot.turretComponent.moveUsingPID();

        // Run Drive Motors
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
            telemetry.addData("    - Linear Actuator Motor", robot.linearActuatorMotor.getPower());
            telemetry.addData("    - Slide Left Motor", robot.slideLeftMotor.getPower());
            telemetry.addData("    - Slide Right Motor", robot.slideRightMotor.getPower());
            telemetry.addData("    - Turret Motor", robot.turretMotor.getPower());
            telemetry.addLine();
            telemetry.addData("MOTOR POSITIONS", "");
            telemetry.addData("    - Slide Left Motor", robot.slideLeftMotor.getCurrentPosition());
            telemetry.addData("    - Slide Right Motor", robot.slideRightMotor.getCurrentPosition());
            telemetry.addData("    - Turret Motor", robot.turretMotor.getCurrentPosition());
            telemetry.addLine();
            telemetry.addData("SERVO POWER/POSITIONS", "");
            telemetry.addData("    - Intake Belt Servo Power", robot.intakeBeltServo.getPower());
            telemetry.addData("    - Intake Wheel Servo Power", robot.intakeWheelServo.getPower());
            telemetry.addData("    - Specimen Servo Position", robot.specimenClawServo.getPosition());
            telemetry.addData("    - Swing Servo Left Position", robot.swingServoLeft.getPosition());
            telemetry.addData("    - Swing Servo Right Position", robot.swingServoRight.getPosition());
            telemetry.addData("    - Intake Vertical Joint Servo Position", robot.intakeVerticalJointServo.getPosition());
            telemetry.addData("    - Swing Extension Left Power", robot.swingExtensionLeft.getPower());
            telemetry.addData("    - Swing Extension Right Power", robot.swingExtensionRight.getPower());
            telemetry.addLine();
            telemetry.addData("POSITION INDEXES", "");
            telemetry.addData("    - Slide Position Index", slidePositionIndex);
            telemetry.addData("    - Turret Position Index", turretPositionIndex);
            telemetry.addLine();
            telemetry.addData("SENSOR VALUES", "");
            telemetry.addData("    - Intake Color Sensor Color/Distance", robot.intakeColorSensor.argb() + " / " + robot.intakeColorSensor.getDistance(DistanceUnit.MM));
            telemetry.addData("    - Specimen Color Sensor Color/Distance", robot.specimenColorSensor.argb() + " / " + robot.specimenColorSensor.getDistance(DistanceUnit.MM));
            telemetry.addLine();
            telemetry.addData("ENUMS", "");
            telemetry.addData("    - Intake State", intakeState);
            telemetry.addData("    - Multiplier Selection", selection);
            telemetry.addData("    - Alliance", alliance);
            telemetry.addData("    - Swing Intake State", swingIntakeState);
            telemetry.addLine();
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
            gamepad1.rumble(100);
        } else if (isOppositeAllianceColor) {
            intakeState = IntakeState.OUTTAKE;
        }
    }

    // Helper function to reset slide encoders
    private void resetSlideEncoders() {
        // Reset Encoders
        robot.slideLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.slideRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Run Without Encoders
        robot.slideLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.slideRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Helper function to reset slide encoders
    private void resetTurretEncoder() {
        // Reset Encoders
        robot.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Run Without Encoders
        robot.turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
