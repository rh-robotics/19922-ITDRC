package org.firstinspires.ftc.teamcode.testFiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CRServoTest extends OpMode {
    CRServo servo;

    @Override
    public void init() {
        servo = hardwareMap.get(CRServo.class, "testServo");

        servo.setDirection(CRServo.Direction.FORWARD);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            servo.setPower(0.2);
        } else if (gamepad1.b) {
            servo.setPower(-0.2);
        } else {
            servo.setPower(0);
        }

        telemetry.addLine("Current CR Servo Power: " + servo.getPower());
    }
}
