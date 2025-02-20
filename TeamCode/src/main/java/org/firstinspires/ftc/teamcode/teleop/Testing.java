package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@TeleOp(name = "Testing", group = "Testing")
public class Testing extends OpMode {
    HWC robot;
    Servo servo;

    double servoPos = 0;

    @Override
    public void init() {
        servo = robot.bucketServo;
    }

    @Override
    public void loop() {
        // Update Gamepads
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        if (robot.currentGamepad1.dpad_up && !robot.previousGamepad1.dpad_up) {
            servoPos += 0.1;
        } else if (robot.currentGamepad1.dpad_down && !robot.previousGamepad1.dpad_down) {
            servoPos -= 0.1;
        }

        servo.setPosition(servoPos);

        telemetry.addData("Servo Position", servoPos);
        telemetry.update();
    }
}
