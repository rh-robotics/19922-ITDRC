package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Position Test")
public class ServoTest extends OpMode {
    Servo testServo;
    double position1 = 0.5;
    double position2 = 0;

    @Override
    public void init() {
        testServo = hardwareMap.get(Servo.class, "testServo");
    }

    @Override
    public void loop() {
        if (gamepad1.square) {
            testServo.setPosition(position1);
        } else if (gamepad1.circle) {
            testServo.setPosition(position2);
        }
    }
}
