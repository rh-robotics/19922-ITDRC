package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(group = "Final Autons", name = "1 Specimen")
public class OneSpecimenAuto extends OpMode {
    private enum State {
        START, SCORING, MOVING_PRESETS, PICK_UP_SPECIMEN, PARK, STOP;
    }

    private State state = State.START;
    private int specimensScored = 0;
    private boolean delivered = false;

    private Trajectory scoreSpecimenFromPickUp, scoreSpecimenFromStart;
    private Trajectory firstPreset1, firstPreset2, firstPreset3, secondPreset1, secondPreset2, secondPreset3, thirdPreset1, thirdPreset2, thirdPreset3, goToPresets;
    private Trajectory pickUpFromScore, pickUpFromMove, moveForwardSpecimen;
    private Trajectory park;

    private String activeTrajectory = "";

    private HWC robot;

    private final Pose2d START_POSE_BLUE = new Pose2d(-14.5, 62, Math.toRadians(90.00));

    @Override
    public void init() {
        robot = new HWC(hardwareMap, telemetry);

        robot.drive.setPoseEstimate(START_POSE_BLUE);

        scoreSpecimenFromStart = robot.drive.trajectoryBuilder(START_POSE_BLUE)
                .lineTo(new Vector2d(0, 35))
                .build();

        moveForwardSpecimen = robot.drive.trajectoryBuilder(scoreSpecimenFromStart.end())
                .lineTo(new Vector2d(0, 31))
                .build();

        goToPresets = robot.drive.trajectoryBuilder(moveForwardSpecimen.end())
                .splineTo(new Vector2d(-36, 35), Math.toRadians(270))
                .build();

        firstPreset1 = robot.drive.trajectoryBuilder(goToPresets.end())
                .splineToConstantHeading(new Vector2d(-36, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-42, 13), Math.toRadians(270))
                .build();

        firstPreset2 = robot.drive.trajectoryBuilder(firstPreset1.end())
                .lineTo(new Vector2d(-45, 60))
                .build();

        secondPreset1 = robot.drive.trajectoryBuilder(firstPreset1.end())
                .splineToConstantHeading(new Vector2d(-47, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-50, 13), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-55, 50), Math.toRadians(270))
                .build();

        thirdPreset1 = robot.drive.trajectoryBuilder(secondPreset1.end())
                .splineToConstantHeading(new Vector2d(-57, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-60, 13), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-60, 50), Math.toRadians(270))
                .build();

        pickUpFromMove = robot.drive.trajectoryBuilder(thirdPreset1.end())
                .lineToConstantHeading(new Vector2d(-45, 60))
                .build();

        scoreSpecimenFromPickUp = robot.drive.trajectoryBuilder(pickUpFromMove.end())
                .lineToLinearHeading(new Pose2d(0, 35, Math.toRadians(90)))
                .build();

        pickUpFromScore = robot.drive.trajectoryBuilder(moveForwardSpecimen.end())
                .lineToLinearHeading(new Pose2d(-45, 60, Math.toRadians(270)))
                .build();

        park = robot.drive.trajectoryBuilder(moveForwardSpecimen.end())
                .lineToConstantHeading(new Vector2d(-45, 60))
                .build();
    }

    @Override
    public void start() {
        robot.drive.followTrajectory(scoreSpecimenFromStart);
        robot.drive.followTrajectory(moveForwardSpecimen);
        robot.drive.followTrajectory(goToPresets);
        robot.drive.followTrajectory(firstPreset1);
        robot.drive.followTrajectory(firstPreset2); // forward
        robot.drive.followTrajectory(scoreSpecimenFromPickUp);
        robot.drive.followTrajectory(moveForwardSpecimen);
        robot.drive.followTrajectory(pickUpFromScore);
        robot.drive.followTrajectory(scoreSpecimenFromPickUp);
        robot.drive.followTrajectory(moveForwardSpecimen);
        robot.drive.followTrajectory(pickUpFromScore);
    }

    @Override
    public void loop() {
        requestOpModeStop();
    }

    private void scoreSpecimen() {
        // TODO: implement this :)
    }

    private void pickUpSpecimen() {
        // TODO: implement this :)
    }
}
