package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(name = "Pick Up Specimen", group = "Autonomous Methods")
public class PickUpSpecimen extends OpMode {
    private HWC robot;
    private double clawStatusChanged = -1000; // in *seconds*

    @Override
    public void init() {
        // Update Telemetry to show that the robot is initializing
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Initialize Robot Hardware
        robot = new HWC(hardwareMap, telemetry);

        // Initialize Controllers
        gamepad1.type = Gamepad.Type.SONY_PS4;
        gamepad2.type = Gamepad.Type.SONY_PS4;
        gamepad1.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);
        gamepad2.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);

        // Update Telemetry to show that the robot has been initialized
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        robot.time.reset();

        robot.specimenClawServo.setPosition(0.4); // Closed Position
        clawStatusChanged = robot.time.time();
    }

    @Override
    public void loop() {
        telemetry.addLine("time: " + robot.time.time());
        telemetry.addLine("clawStatusChanged: " + clawStatusChanged);
        telemetry.addLine("difference: " + (robot.time.time() - clawStatusChanged));

        if (robot.time.time() - clawStatusChanged < 0.5) { // only run when it's been over half a second since the last time it was changed
            return;
        }

        robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[1]);
        robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[1]);
        robot.vSlideLeftComponent.moveUsingPID();
        robot.vSlideRightComponent.moveUsingPID();

        if (robot.vSlideLeftComponent.closeEnough(50)) {
            requestOpModeStop();
        }
    }

}