package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "IntakeTest")
public class IntakeTest extends OpMode {
    CRServo intakeServo1;
    CRServo intakeServo2;

    @Override
    public void init() {
        intakeServo1 = hardwareMap.get(CRServo.class, "intakeServo1");
        intakeServo2 = hardwareMap.get(CRServo.class, "intakeServo2");

        intakeServo1.setDirection(CRServo.Direction.REVERSE);
        intakeServo2.setDirection(CRServo.Direction.FORWARD);
    }

    @Override
    public void loop() {
        if (gamepad1.circle) {
            intakeServo1.setPower(1);
            intakeServo2.setPower(1);
        } else if (gamepad1.square) {
            intakeServo1.setPower(-1);
            intakeServo2.setPower(-1);
        } else {
            intakeServo1.setPower(0);
            intakeServo2.setPower(0);
        }
    }
}