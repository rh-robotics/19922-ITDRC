package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.teleop.enums.MultiplierSelection;

public class TestingOp extends OpMode {
    private HWC robot;
    private double specimenServoPos = 0;
    private double intakeServoPower = 0;

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
    public void start() {
        robot.time.reset();
    }

    @Override
    public void loop() {
        // ------ GamePad Updates ------ //
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        // ------ Specimen Servo Control ------ //
        if (robot.currentGamepad1.left_bumper && !robot.previousGamepad1.left_bumper) {
            specimenServoPos += 0.1;
        } else if (robot.currentGamepad1.right_bumper && !robot.previousGamepad1.right_bumper) {
            specimenServoPos -= 0.1;
        }

        // ------ Intake Servo Control ------ //
        if (robot.currentGamepad1.a && !robot.previousGamepad1.a) {
            intakeServoPower = 1;
        } else if (robot.currentGamepad1.b && !robot.previousGamepad1.b) {
            intakeServoPower = -1;
        } else if (robot.currentGamepad1.x && !robot.previousGamepad1.x) {
            intakeServoPower = 0;
        }

        // ------ Run Motors & Servos ------ //
        robot.specimenServo.setPosition(specimenServoPos);
        robot.intakeServo.setPower(intakeServoPower);

        // ------ Telemetry Updates ------ //
        telemetry.addData("> Status", "Running");
        telemetry.addLine();
        telemetry.addData("> ", "Left/Right Bumper to Increment/Decent Specimen Servo Position");
        telemetry.addData("> ", "A/B/X to Run/Reverse/Stop Intake Servo");
        telemetry.addData("Specimen Servo Position", specimenServoPos);
        telemetry.addData("Intake Servo Power", intakeServoPower);
        telemetry.update();
    }
}
