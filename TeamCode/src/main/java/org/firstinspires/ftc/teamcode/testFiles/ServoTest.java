package org.firstinspires.ftc.teamcode.testFiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group = "Test OpModes")
public class ServoTest extends OpMode {
    Gamepad previousGamepad1;
    Servo servo;

    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "testServo");

        servo.setDirection(Servo.Direction.FORWARD);

        gamepad1.copy(previousGamepad1);
    }

    @Override
    public void loop() {
        if (gamepad1.a && previousGamepad1.a != gamepad1.a) {
            servo.setPosition(servo.getPosition() + 0.05);
        } else if (gamepad1.b && previousGamepad1.b != gamepad1.b) {
            servo.setPosition(servo.getPosition() - 0.05);
        }

        gamepad1.copy(previousGamepad1);

        telemetry.addLine("Current Servo Position: " + servo.getPosition());
    }
}
