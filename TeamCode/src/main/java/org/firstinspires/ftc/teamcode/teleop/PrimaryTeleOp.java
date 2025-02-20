package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.teleop.enums.MultiplierSelection;

@TeleOp(name = "Primary Teleop", group = "Teleop")
public class PrimaryTeleOp extends OpMode {
    private HWC robot;
    private MultiplierSelection selection = MultiplierSelection.TURN_SPEED;
    private boolean testingMode = false;

    // ------ Drive Variables ------ //
    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double strafeSpeed = 0.5;

    @Override
    public void init() {
        // ------ Telemetry ------ //
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ------ Initialize Robot Hardware ------ //
        robot = new HWC(hardwareMap, telemetry);

        // ------ Telemetry ------ //
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // ------ Gamepad Updates ------ //
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        // ------ Speed Multiplier Selection ------ //
        if (robot.currentGamepad1.a && !robot.previousGamepad1.a) {
            selection = MultiplierSelection.TURN_SPEED;
        } else if (robot.currentGamepad1.b && !robot.previousGamepad1.b) {
            selection = MultiplierSelection.DRIVE_SPEED;
        } else if (robot.currentGamepad1.x && !robot.previousGamepad1.x) {
            selection = MultiplierSelection.STRAFE_SPEED;
        }

        // ------ Speed Multiplier Changes ------ //
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

        // ------ Enabling Testing Mode ------ //
        if ((robot.currentGamepad1.left_bumper && !robot.previousGamepad1.left_bumper) && (robot.currentGamepad1.right_bumper && !robot.previousGamepad1.right_bumper)) {
            testingMode = !testingMode;
        }

        // ------ Telemetry ------ //
        if (testingMode) {
            telemetry.addData("> TESTING MODE IS ENABLED", "Extra Telemetry is Displayed");
            telemetry.addLine();
        }

        telemetry.addData("Press A to start changing turn speed", "");
        telemetry.addData("Press B to start changing drive speed", "");
        telemetry.addData("Press X to start changing strafe speed", "");
        telemetry.addLine();
        telemetry.addData("Modifying", selection);
        telemetry.addLine();
        telemetry.addData("Turn Speed", turnSpeed);
        telemetry.addData("Drive Speed", driveSpeed);
        telemetry.addData("Strafe Speed", strafeSpeed);
        telemetry.update();
    }

    @Override
    public void start() {
        robot.time.reset();
    }

    @Override
    public void loop() {
        // ------ GamePad Updates ------ //
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        // ------ Power & Controller Values ------ //
        double drive = -robot.currentGamepad1.left_stick_y;
        double strafe = robot.currentGamepad1.left_stick_x;
        double turn = (robot.currentGamepad1.left_trigger - robot.currentGamepad1.right_trigger) * turnSpeed;

        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);
        double frontLeftPower = (turn - strafe - drive) / denominator;
        double backLeftPower = (turn + strafe - drive) / denominator;
        double frontRightPower = (turn - strafe + drive) / denominator;
        double backRightPower = (turn + strafe + drive) / denominator;

        // ------ Run Motors ------ //
        robot.leftFront.setPower(frontLeftPower);
        robot.leftRear.setPower(backLeftPower);
        robot.rightFront.setPower(frontRightPower);
        robot.rightRear.setPower(backRightPower);

        // ------ Telemetry Updates ------ //
        telemetry.addData("> Status", "Running");
        telemetry.update();
    }
}
