package org.firstinspires.ftc.teamcode.testFiles;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.roadrunner.util.Encoder;

@TeleOp(name = "Encoder Test", group = "Test OpModes")
public class EncoderTest extends OpMode {
    Encoder leftEncoder, frontEncoder, rightEncoder;

    @Override
    public void init() {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftRear"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightRear"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftFront"));

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        telemetry.addLine("Left Encoder: " + leftEncoder.getCurrentPosition());
        telemetry.addLine("Front Encoder: " + frontEncoder.getCurrentPosition());
        telemetry.addLine("Right Encoder: " + rightEncoder.getCurrentPosition());

    }
}
