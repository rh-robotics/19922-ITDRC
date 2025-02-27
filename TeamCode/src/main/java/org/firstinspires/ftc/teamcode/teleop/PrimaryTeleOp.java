package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.teleop.enums.Alliance;
import org.firstinspires.ftc.teamcode.teleop.enums.AllowedPickupState;
import org.firstinspires.ftc.teamcode.teleop.enums.BucketPosition;
import org.firstinspires.ftc.teamcode.teleop.enums.ClawPosition;
import org.firstinspires.ftc.teamcode.teleop.enums.IntakeState;
import org.firstinspires.ftc.teamcode.teleop.enums.IntakeSwingPosition;
import org.firstinspires.ftc.teamcode.teleop.enums.MultiplierSelection;
import org.firstinspires.ftc.teamcode.teleop.enums.TurretPosition;

@TeleOp
public class PrimaryTeleOp extends OpMode {
    private HWC robot;
    private Alliance alliance = Alliance.RED;
    private MultiplierSelection selection = MultiplierSelection.TURN_SPEED;
    private IntakeState intakeState = IntakeState.STOPPED;
    private AllowedPickupState allowedPickupState = AllowedPickupState.ALL;
    private IntakeSwingPosition intakeSwingPosition = IntakeSwingPosition.TRANSFER;
    private BucketPosition bucketPosition = BucketPosition.TRANSFER;
    private ClawPosition clawPosition = ClawPosition.OPEN;
    private TurretPosition turretPosition = TurretPosition.INIT;

    // Drive Variables
    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double strafeSpeed = 0.5;

    // Position Indexes
    private int hSlideIndex = 0;
    private int vSlideIndex = 0;

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
        updateGamepads();

        // Control Driving
        controlDrive();

        // Control Intake
        controlAllowedPickupState();
        controlIntake();
        controlIntakeSwing();

        // Control Turret
        controlTurret();
        controlVSlides();
//        controlBucket();
        controlClaw();

        // Update Telemetry
        updateTelemetry();
    }

    private void updateGamepads() {
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);
    }

    private void controlDrive() {
        double drive = robot.currentGamepad1.left_stick_y;
        double strafe = -robot.currentGamepad1.left_stick_x;
        double turn = (robot.currentGamepad1.right_trigger - robot.currentGamepad1.left_trigger) * turnSpeed;
        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);

        double frontLeftPower = (turn - strafe - drive) / denominator;
        double backLeftPower = (turn + strafe - drive) / denominator;
        double frontRightPower = (turn - strafe + drive) / denominator;
        double backRightPower = (turn + strafe + drive) / denominator;

        robot.leftFront.setPower(frontLeftPower);
        robot.rightFront.setPower(frontRightPower);
        robot.leftRear.setPower(backLeftPower);
        robot.rightRear.setPower(backRightPower);
    }

    private void controlAllowedPickupState() {
        if (robot.currentGamepad1.touchpad_finger_1 && !robot.previousGamepad1.touchpad_finger_1) {
            if (robot.currentGamepad1.touchpad_finger_1_x < 0.33) {
                allowedPickupState = AllowedPickupState.YELLOW_ONLY;
            } else if (robot.currentGamepad1.touchpad_finger_1_x > 0.33 && robot.currentGamepad1.touchpad_finger_1_x < 0.66) {
                allowedPickupState = AllowedPickupState.ALL;
            } else if (robot.currentGamepad1.touchpad_finger_1_x > 0.66) {
                allowedPickupState = AllowedPickupState.ALLIANCE_ONLY;
            }
        }
    }

    private void controlIntakeSwing() {
        if (robot.currentGamepad1.square && !robot.previousGamepad1.square) {
            intakeSwingPosition = (intakeSwingPosition == IntakeSwingPosition.INTAKE) ? IntakeSwingPosition.TRANSFER : IntakeSwingPosition.INTAKE;
        }

        switch (intakeSwingPosition) {
            case INTAKE:
                robot.swingServoLeft.setPosition(0.5); // TODO: Test Value
                robot.swingServoRight.setPosition(0.5);

                break;
            case TRANSFER:
                robot.swingServoLeft.setPosition(0.15);
                robot.swingServoRight.setPosition(0.85);

                break;
        }
    }

    private void controlIntake() {
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
                robot.intakeServo1.setPower(-1);
                robot.intakeServo2.setPower(1);

                if (intakeSwingPosition == IntakeSwingPosition.INTAKE) {
                    detectIntake();
                }

                break;
            case OUTTAKE:
                robot.intakeServo1.setPower(1);
                robot.intakeServo2.setPower(-1);

                break;
            case STOPPED:
                robot.intakeServo1.setPower(0);
                robot.intakeServo2.setPower(0);

                break;
        }
    }

    private void detectIntake() {
        if (robot.intakeColorSensor.getDistance(DistanceUnit.CM) < 2) {
            int red = robot.intakeColorSensor.red();
            int green = robot.intakeColorSensor.green();
            int blue = robot.intakeColorSensor.blue();

            switch (alliance) {
                case RED:
                    switch (allowedPickupState) {
                        case YELLOW_ONLY:
                            if (red > green && red > blue) {
                                intakeState = IntakeState.OUTTAKE;
                            } else if (blue > green && blue > red) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                        case ALL:
                            if (blue > red && blue > green) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                        case ALLIANCE_ONLY:
                            if (green > red && green > blue) {
                                intakeState = IntakeState.OUTTAKE;
                            } else if (blue > red && blue > green) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                    }
                    break;
                case BLUE:
                    switch (allowedPickupState) {
                        case YELLOW_ONLY:
                            if (red > green && red > blue) {
                                intakeState = IntakeState.OUTTAKE;
                            } else if (blue > green && blue > red) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                        case ALL:
                            if (red > blue && red > green) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                        case ALLIANCE_ONLY:
                            if (green > red && green > blue) {
                                intakeState = IntakeState.OUTTAKE;
                            } else if (red > blue && red > green) {
                                intakeState = IntakeState.OUTTAKE;
                            } else {
                                intakeState = IntakeState.STOPPED;
                            }
                            break;
                    }
                    break;
            }
        }
    }

    private void controlTurret() {
        if (robot.currentGamepad1.triangle && !robot.previousGamepad1.triangle) {
            turretPosition = (turretPosition == TurretPosition.TRANSFER) ? TurretPosition.DELIVERY : TurretPosition.TRANSFER;
        }

        switch (turretPosition) {
            case TRANSFER:
                robot.turretComponent.setTarget(HWC.turretPositions[0]);

                break;
            case DELIVERY:
                robot.turretComponent.setTarget(HWC.turretPositions[2]);

                break;
            case INIT:
                robot.turretComponent.setTarget(HWC.turretPositions[1]);

                break;
        }

        robot.turretComponent.moveUsingPID();
    }

    private void controlVSlides() {
        if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
            // Increment Value
            vSlideIndex++;

            // If value is above the max, don't increase
            if (vSlideIndex > HWC.vSlidePositions.length - 1) {
                vSlideIndex = HWC.vSlidePositions.length - 1;
            }
        } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
            // Decrement Value
            vSlideIndex--;

            // If value is below the min, don't decrease
            if (vSlideIndex < 0) {
                vSlideIndex = 0;
            }
        }

        robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[vSlideIndex]);
        robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[vSlideIndex]);
        robot.vSlideLeftComponent.moveUsingPID();
        robot.vSlideRightComponent.moveUsingPID();
    }

    private void controlBucket() {
        if (robot.currentGamepad1.circle && !robot.previousGamepad1.circle) {
            bucketPosition = (bucketPosition == BucketPosition.TRANSFER) ? BucketPosition.DELIVER : BucketPosition.TRANSFER;
        }

        // TODO: Change Positions
        switch (bucketPosition) {
            case TRANSFER:
                robot.bucketServo.setPosition(0);

                break;
            case DELIVER:
                robot.bucketServo.setPosition(1);

                break;
        }
    }

    private void controlClaw() {
        if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
            clawPosition = (clawPosition == ClawPosition.OPEN) ? ClawPosition.CLOSED : ClawPosition.OPEN;
        }

        switch (clawPosition) {
            case OPEN:
                robot.specimenClawServo.setPosition(0.8);

                break;
            case CLOSED:
                robot.specimenClawServo.setPosition(0.5);

                break;
        }
    }


    private void updateTelemetry() {
        telemetry.addData("Status", "Running");
        telemetry.addData("Alliance", alliance);

        if (testingMode) {
            telemetry.addLine();
            telemetry.addLine();
            telemetry.addLine("| ---- EXTENSION ---- |");
            telemetry.addLine("> Extension Left Servo");
            telemetry.addData("    - Position", robot.hSlideLeftServo.getPosition());
            telemetry.addLine("> Extension Right Servo");
            telemetry.addData("    - Position", robot.hSlideRightServo.getPosition());
            telemetry.addLine();
            telemetry.addLine("| ---- INTAKE ---- |");
            telemetry.addData("> Intake State: ", intakeState);
            telemetry.addLine("> Color Sensor");
            telemetry.addData("    - Allowed Pickup State: ", allowedPickupState);
            telemetry.addData("    - Distance (CM): ", robot.intakeColorSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("    - Red: ", robot.intakeColorSensor.red());
            telemetry.addData("    - Green: ", robot.intakeColorSensor.green());
            telemetry.addData("    - Blue: ", robot.intakeColorSensor.blue());
            telemetry.addLine("> Intake Servos");
            telemetry.addData("    - Intake Servo 1 (Power): ", robot.intakeServo1.getPower());
            telemetry.addData("    - Intake Servo 2 (Power): ", robot.intakeServo2.getPower());
            telemetry.addLine("> Intake Swing Servos");
            telemetry.addData("    - Intake Swing State: ", intakeSwingPosition);
            telemetry.addData("    - Swing Servo Left (Position): ", robot.swingServoLeft.getPosition());
            telemetry.addData("    - Swing Servo Right (Position): ", robot.swingServoRight.getPosition());
            telemetry.addLine();
            telemetry.addLine("| ---- TURRET ---- |");
            telemetry.addLine("> Turret");
            telemetry.addData("    - Turret Position: ", turretPosition);
            telemetry.addData("    - Turret Motor (Power): ", robot.turretMotor.getPower());
            telemetry.addData("    - Turret Motor (Position): ", robot.turretMotor.getCurrentPosition());
            telemetry.addData("    - Turret Motor (Target): ", robot.turretComponent.getTarget());
            telemetry.addLine("> Vertical Slides");
            telemetry.addData("    - Vertical Slide Index: ", vSlideIndex);
            telemetry.addLine("    ---");
            telemetry.addData("    - Vertical Slide Left (Power): ", robot.vSlideLeftMotor.getPower());
            telemetry.addData("    - Vertical Slide Left (Position): ", robot.vSlideLeftMotor.getCurrentPosition());
            telemetry.addData("    - Vertical Slide Left (Target): ", robot.vSlideLeftComponent.getTarget());
            telemetry.addLine("    ---");
            telemetry.addData("    - Vertical Slide Right (Power): ", robot.vSlideRightMotor.getPower());
            telemetry.addData("    - Vertical Slide Right (Position): ", robot.vSlideRightMotor.getCurrentPosition());
            telemetry.addData("    - Vertical Slide Right (Target): ", robot.vSlideRightComponent.getTarget());
            telemetry.addLine("> Specimen Claw");
            telemetry.addData("    - Specimen Claw Servo (Position): ", robot.specimenClawServo.getPosition());
            telemetry.addData("    - Claw Position (Enum): ", clawPosition);
            telemetry.addLine("> Bucket");
            telemetry.addData("    - Bucket Servo (Position): ", robot.bucketServo.getPosition());
            telemetry.addData("    - Bucket Position (Enum): ", bucketPosition);
            telemetry.addLine();
            telemetry.addLine("| ---- DRIVE ---- |");
            telemetry.addLine("> Speed Multipliers");
            telemetry.addData("    - Turn Speed: ", turnSpeed);
            telemetry.addData("    - Drive Speed: ", driveSpeed);
            telemetry.addData("    - Strafe Speed: ", strafeSpeed);
            telemetry.addLine("> Drive Motors");
            telemetry.addData("    - Left Front (Power): ", robot.leftFront.getPower());
            telemetry.addData("    - Right Front (Power): ", robot.rightFront.getPower());
            telemetry.addData("    - Left Rear (Power): ", robot.leftRear.getPower());
            telemetry.addData("    - Right Rear (Power): ", robot.rightRear.getPower());
            telemetry.addLine();
            telemetry.addLine();
            telemetry.addLine("| -------- GAMEPAD 1 -------- |");
            telemetry.addLine("> Buttons");
            telemetry.addData("    - Cross:", robot.currentGamepad1.cross);
            telemetry.addData("    - Square:", robot.currentGamepad1.square);
            telemetry.addData("    - Triangle:", robot.currentGamepad1.triangle);
            telemetry.addData("    - Circle:", robot.currentGamepad1.circle);
            telemetry.addLine("> D-Pad");
            telemetry.addData("    - Up:", robot.currentGamepad1.dpad_up);
            telemetry.addData("    - Down:", robot.currentGamepad1.dpad_up);
            telemetry.addData("    - Left:", robot.currentGamepad1.dpad_up);
            telemetry.addData("    - Right:", robot.currentGamepad1.dpad_up);
            telemetry.addLine("> Bumpers");
            telemetry.addData("    - Left:", robot.currentGamepad1.left_bumper);
            telemetry.addData("    - Right:", robot.currentGamepad1.right_bumper);
            telemetry.addLine("> Triggers");
            telemetry.addData("    - Left:", robot.currentGamepad1.left_trigger);
            telemetry.addData("    - Right:", robot.currentGamepad1.right_trigger);
            telemetry.addLine("> Joysticks");
            telemetry.addData("    - Left Joystick (x, y, click)", "(" + robot.currentGamepad1.left_stick_x + ", " + robot.currentGamepad1.left_stick_y + ", " + robot.currentGamepad1.left_stick_button + ")");
            telemetry.addData("    - Right Joystick (x, y, click)", "(" + robot.currentGamepad1.right_stick_x + ", " + robot.currentGamepad1.right_stick_y + ", " + robot.currentGamepad1.right_stick_button + ")");

        }
    }
}
