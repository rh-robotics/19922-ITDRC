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

    private Trajectory scoreSpecimen2FromPickUp, scoreSpecimen3FromPickUp, scoreSpecimenFromStart, firstPreset1, firstPreset2, goToPresets, pickUpFromScore2, moveForwardSpecimen1, moveForwardSpecimen2, moveForwardSpecimen3, goToPark;

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

        moveForwardSpecimen1 = robot.drive.trajectoryBuilder(scoreSpecimenFromStart.end())
                .lineTo(new Vector2d(0, 31))
                .build();

        moveForwardSpecimen2 = robot.drive.trajectoryBuilder(scoreSpecimen2FromPickUp.end())
                .lineTo(new Vector2d(-5, 31))
                .build();

        moveForwardSpecimen3 = robot.drive.trajectoryBuilder(scoreSpecimen3FromPickUp.end())
                .lineTo(new Vector2d(5, 31))
                .build();

        goToPresets = robot.drive.trajectoryBuilder(moveForwardSpecimen1.end())
                .splineTo(new Vector2d(-36, 35), Math.toRadians(270))
                .build();

        firstPreset1 = robot.drive.trajectoryBuilder(goToPresets.end())
                .splineToConstantHeading(new Vector2d(-36, 20), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-42, 13), Math.toRadians(270))
                .build();

        firstPreset2 = robot.drive.trajectoryBuilder(firstPreset1.end())
                .lineTo(new Vector2d(-45, 60))
                .build();

        scoreSpecimen2FromPickUp = robot.drive.trajectoryBuilder(firstPreset2.end())
                .lineToLinearHeading(new Pose2d(-5, 35, Math.toRadians(90)))
                .build();

        scoreSpecimen3FromPickUp = robot.drive.trajectoryBuilder(firstPreset2.end())
                .lineToLinearHeading(new Pose2d(5, 35, Math.toRadians(90)))
                .build();

        pickUpFromScore2 = robot.drive.trajectoryBuilder(moveForwardSpecimen2.end())
                .lineToLinearHeading(new Pose2d(-45, 60, Math.toRadians(270)))
                .build();

        goToPark = robot.drive.trajectoryBuilder(moveForwardSpecimen3.end())
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
                robotInPosition = true;
            } else if (specimensScored == 2) {
                robot.drive.followTrajectory(pickUpFromScore2);
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
                robot.drive.followTrajectory(moveForwardSpecimen1);
                robotInPosition = true;
            } else if (specimensScored < 2) { // state = State.SCORING
                robot.drive.followTrajectory(scoreSpecimen2FromPickUp);
                robot.drive.followTrajectory(moveForwardSpecimen2);
                robotInPosition = true;
            } else if (specimensScored < 3) {
                robot.drive.followTrajectory(scoreSpecimen3FromPickUp);
                robot.drive.followTrajectory(moveForwardSpecimen3);
                robotInPosition = true;
            }
        }

        if (specimenInPossession) {
            scoreSpecimen(); // this will loop
        } else {
            if (state == State.START) {
                state = State.MOVING_PRESETS;
            } else if (state == State.SCORING) {
                if (specimensScored < 2) {
                    state = State.PICK_UP_SPECIMEN;
                    robotInPosition = false;
                } else {
                    // Park
                    robot.drive.followTrajectory(goToPark);
                    requestOpModeStop();
                }
            }
        }
    }

    private void movePreSets() {
        robot.drive.followTrajectory(goToPresets);
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
