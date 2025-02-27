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
    private Trajectory moveAllPreSets;
    private Trajectory pickUpFromScore, pickUpFromMove;
    private Trajectory park;

    private String activeTrajectory = "";

    private HWC robot;

    private final Pose2d START_POSE_BLUE = new Pose2d(-14.5, 62, Math.toRadians(90.00));

    @Override
    public void init() {
        robot = new HWC(hardwareMap, telemetry);

        scoreSpecimenFromStart = robot.drive.trajectoryBuilder(START_POSE_BLUE)
                .splineTo(new Vector2d(-36, 35), Math.toRadians(270))
                .build();

        moveAllPreSets = robot.drive.trajectoryBuilder(scoreSpecimenFromStart.end())
                .splineToConstantHeading(new Vector2d(-36, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-42, 13), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-45, 50), Math.toRadians(270))

                .splineToConstantHeading(new Vector2d(-47, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-50, 13), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-55, 50), Math.toRadians(270))

                .splineToConstantHeading(new Vector2d(-57, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-60, 13), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-60, 50), Math.toRadians(270))
                .build();

        pickUpFromMove = robot.drive.trajectoryBuilder(moveAllPreSets.end())
                .lineToConstantHeading(new Vector2d(-45, 62))
                .build();

        scoreSpecimenFromPickUp = robot.drive.trajectoryBuilder(pickUpFromMove.end())
                .lineToConstantHeading(new Vector2d(0, 35))
                .build();

        pickUpFromScore = robot.drive.trajectoryBuilder(scoreSpecimenFromPickUp.end())
                .lineToConstantHeading(new Vector2d(-45, 62))
                .build();

        park = robot.drive.trajectoryBuilder(scoreSpecimenFromPickUp.end())
                .lineToConstantHeading(new Vector2d(-45, 62))
                .build();
    }

    @Override
    public void loop() {
        robot.drive.followTrajectory(scoreSpecimenFromStart);
    }

    private void scoreSpecimen() {
        // TODO: implement this :)
    }

    private void pickUpSpecimen() {
        // TODO: implement this :)
    }
}
