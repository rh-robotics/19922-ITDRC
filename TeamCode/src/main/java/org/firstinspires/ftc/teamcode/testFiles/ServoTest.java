package org.firstinspires.ftc.teamcode.testFiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group = "Test OpModes")
public class ServoTest extends OpMode {
    Gamepad previousGamepad1 = new Gamepad();
    Servo servo;

    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "testServo");

        servo.setDirection(Servo.Direction.FORWARD);

        gamepad1.copy(previousGamepad1);
    }

    @Override
    public void loop() {
        if (gamepad1.circle && previousGamepad1.circle != gamepad1.circle) {
            servo.setPosition(servo.getPosition() + 0.001);
        } else if (gamepad1.square && previousGamepad1.square != gamepad1.square) {
            servo.setPosition(servo.getPosition() - 0.001);
        }

//        0.4, 0.12


        gamepad1.copy(previousGamepad1);

        telemetry.addLine("Current Servo Position: " + servo.getPosition());
    }
}
