package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(name = "Score Specimen", group = "Autonomous Methods")
public class ScoreSpecimen extends OpMode {
    HWC robot;
    Trajectory moveForwardSpecimen;

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

        moveForwardSpecimen = robot.drive.trajectoryBuilder(new Pose2d())
            .forward(5)
            .build();

        // Update Telemetry to show that the robot has been initialized
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        // (In order): Zero, Low Chamber, Side Panel Clearance, High Chamber, Low Basket, High Basket
        robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[3]);
        robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[3]);

        // Precondition: turret is in the right position

        // closed
        robot.specimenClawServo.setPosition(0.4);

        telemetry.addLine("start");
    }

    @Override
    public void loop() {
        robot.vSlideLeftComponent.moveUsingPID();
        robot.vSlideRightComponent.moveUsingPID();

        telemetry.addLine("start");
        telemetry.addLine("Pos: " + robot.vSlideRightComponent.getPosition());
        telemetry.addLine("Tar: " + robot.vSlideRightComponent.getTarget());

        // Precondition: turret is in the right position
        if (robot.vSlideRightComponent.closeEnough(50) && robot.vSlideRightComponent.getTarget() == HWC.vSlidePositions[3] &&
                robot.turretComponent.closeEnough(50)) {
            telemetry.addLine("1");
            robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[2]);
            robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[2]);
        } else if (robot.vSlideLeftComponent.getTarget() == HWC.vSlidePositions[2] &&
                robot.vSlideLeftComponent.closeEnough(450)) {
            telemetry.addLine("2");
            robot.drive.followTrajectory(moveForwardSpecimen);
            robot.specimenClawServo.setPosition(0.12); // open

            robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[0]);
            robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[0]);
        } else if (robot.vSlideLeftComponent.getTarget() == HWC.vSlidePositions[0] &&
                robot.vSlideLeftComponent.closeEnough(50)) {
            telemetry.addLine("3");
            requestOpModeStop();
            telemetry.addData("OpMode", "All done!");
        }
    }
}
