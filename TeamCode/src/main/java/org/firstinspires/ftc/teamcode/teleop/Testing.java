package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Config
@TeleOp(name = "Testing", group = "Testing")
public class Testing extends OpMode {
    HWC robot;
    public static double servoPos;

    @Override
    public void init() {
        robot = new HWC(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        // Update Gamepads
        robot.previousGamepad1.copy(robot.currentGamepad1);
        robot.currentGamepad1.copy(gamepad1);

        if (robot.currentGamepad1.cross && !robot.previousGamepad1.cross) {
            robot.specimenClawServo.setPosition(servoPos);
        }

        // Telemetry
        telemetry.addData("Specimen Claw Servo Position", robot.specimenClawServo.getPosition());

    }
}
