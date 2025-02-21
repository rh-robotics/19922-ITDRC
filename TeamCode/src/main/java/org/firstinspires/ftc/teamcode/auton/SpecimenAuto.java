package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(group = "Final Autons", name = "5 Specimen")
public class SpecimenAuto extends OpMode {
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
        switch (state) {
            case START:
                moveToScoreSpecimen();
                break;
            case MOVING_PRESETS:
                movePreSets();
                break;
            case SCORING:
                moveToScoreSpecimen();
                break;
            case PICK_UP_SPECIMEN:
                moveToPickUpSpecimen();
                break;
        }
    }

    private void moveToPickUpSpecimen() {
        // specimensScored should be 1 if coming from moving presets and larger than that otherwise.
        if (specimensScored == 1) {
            robot.drive.followTrajectory(pickUpFromMove);
        } else {
            robot.drive.followTrajectory(pickUpFromScore);
        }

        pickUpSpecimen();
        state = State.SCORING;
    }

    private void moveToScoreSpecimen() {
        // Triggered by both the START state and the SCORING state.
        if (state == State.START) {
            robot.drive.followTrajectoryAsync(scoreSpecimenFromStart);
            activeTrajectory = "scoreSpecimenFromStart";
        } else { // state = State.SCORING
            robot.drive.followTrajectoryAsync(scoreSpecimenFromPickUp);
            activeTrajectory = "scoreSpecimenFromStart";
        }

        // Switch to next state once done
        if (!robot.drive.isBusy()) {
            if (!delivered) {
                scoreSpecimen();
                specimensScored ++;
                delivered = true; // switch back to false when we switch into to the SCORING state
            }

            if (state == State.START) {
                state = State.MOVING_PRESETS;
            } else if (state == State.SCORING) {
                if (specimensScored < 5) {
                    state = State.PICK_UP_SPECIMEN;
                } else {
                    state = State.PARK;
                }
            }
        }
    }

    private void movePreSets() {
        robot.drive.followTrajectory(moveAllPreSets);
        state = State.PICK_UP_SPECIMEN;
    }

    private void scoreSpecimen() {
        // TODO: implement this :)
    }

    private void pickUpSpecimen() {
        // TODO: implement this :)
    }
}
