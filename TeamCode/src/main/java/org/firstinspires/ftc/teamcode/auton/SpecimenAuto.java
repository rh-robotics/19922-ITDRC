package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

// lol I wish
@Autonomous(group = "Final Autons", name = "5 Specimen")
public class SpecimenAuto extends OpMode {
    private enum State {
        START, SCORING, MOVING_PRESETS, PICK_UP_SPECIMEN, PARK, STOP;
    }

    private State state = State.START;
    private int specimensScored = 0;
    private boolean delivered = false;

    private Trajectory scoreSpecimenFromPickUp, scoreSpecimenFromStart;
    private Trajectory firstPreset1, firstPreset2, firstPreset3, secondPreset1, secondPreset2, secondPreset3, thirdPreset1, thirdPreset2, thirdPreset3, goToPresets;
    private Trajectory pickUpFromScore, pickUpFromMove, moveForwardSpecimen;

    private String activeTrajectory = "";

    private HWC robot;

    private boolean specimenInPossession = true;
    private boolean robotInPosition = false;

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
    }

    @Override
    public void loop() {
        telemetry.addData("Current State: ", state);
        telemetry.addData("Robot In Position: ", robotInPosition);
        telemetry.addData("Specimen In Possession: ", specimenInPossession);

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
        if (!robotInPosition) {
            // specimensScored should be 1 if coming from moving presets and larger than that otherwise.
            if (specimensScored == 1) {
                robot.drive.followTrajectory(pickUpFromMove);
                robotInPosition = true;
            } else {
                robot.drive.followTrajectory(pickUpFromScore);
                robotInPosition = true;
            }
        }

        pickUpSpecimen(); // this will loop

        if (specimenInPossession) {
            state = State.SCORING;
            robotInPosition = false;
        }
    }

    private void moveToScoreSpecimen() {
        if (!robotInPosition) {
            // Triggered by both the START state and the SCORING state.
            if (state == State.START) {
                robot.drive.followTrajectory(scoreSpecimenFromStart);
                robot.drive.followTrajectory(moveForwardSpecimen);
                robotInPosition = true;
            } else { // state = State.SCORING
                robot.drive.followTrajectory(scoreSpecimenFromPickUp);
                robot.drive.followTrajectory(moveForwardSpecimen);
                robotInPosition = true;
            }
        }

        if (specimenInPossession) {
            scoreSpecimen(); // this will loop
        } else {
            if (state == State.START) {
                state = State.MOVING_PRESETS;
            } else if (state == State.SCORING) {
                if (specimensScored < 3) {
                    state = State.PICK_UP_SPECIMEN;
                    robotInPosition = false;
                } else {
                    // Park
                    robot.drive.followTrajectory(pickUpFromScore);
                    state = State.PARK;
                    robotInPosition = false;
                }
            }
        }
    }

    private void movePreSets() {
        robot.drive.followTrajectory(firstPreset1);
        robot.drive.followTrajectory(firstPreset2);

        state = State.PICK_UP_SPECIMEN;
        robotInPosition = false;
    }

    private void scoreSpecimen() {
        specimensScored ++;
        specimenInPossession = false;
    }

    private void pickUpSpecimen() {
        specimenInPossession = true;
    }
}
