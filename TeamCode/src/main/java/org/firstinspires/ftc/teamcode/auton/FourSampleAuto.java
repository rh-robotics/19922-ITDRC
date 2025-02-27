package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;
import org.firstinspires.ftc.teamcode.subsystems.roadrunner.drive.SampleMecanumDrive;

@Autonomous(name = "Four Sample Auton")
public class FourSampleAuto extends OpMode {

    private enum State {
        DRIVING_TO_DEPOSIT_PRELOAD_SAMPLE, DEPOSITING_PRELOAD_SAMPLE, DRIVING_TO_SAMPLE, PICKING_UP_SAMPLE, DRIVING_TO_BASKET, DEPOSITING_SAMPLE, DRIVE_TO_PARKING
    }
    HWC robot;

    State state = State.DRIVING_TO_DEPOSIT_PRELOAD_SAMPLE;

    Trajectory driveToDepositPreloadSample;
    private Trajectory driveToSample1;
    private Trajectory driveToBasket1;
    private Trajectory driveToSample2;
    private Trajectory driveToBasket2;
    private Trajectory driveToSample3;
    private Trajectory driveToBasket3;
    private Trajectory driveToPark;
    private SampleMecanumDrive drive;
    private final Pose2d START_POSE_BLUE = new Pose2d(23.6, 62.5, Math.toRadians(90));
    private final Pose2d START_POSE_RED = new Pose2d(-23.6, -60.5, Math.toRadians(-90));
    private int sampleCount = 1;

    @Override
    public void init() {
        // ------ Telemetry ------ //
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ------ Initialize Robot Hardware ------ //
        robot = new HWC(hardwareMap, telemetry);

        //switch (allianceColor) {

        //case BLUE:

//        // Drive to deposit preloaded sample
//        driveToDepositPreloadSample = drive.trajectoryBuilder(START_POSE_BLUE)
//                .back(7)
//                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)), Math.toRadians(90))
//                .build();
//
//        // Drive to pick up the first yellow sample
//        driveToSample1 = drive.trajectoryBuilder(driveToDepositPreloadSample.end())
//                .splineToLinearHeading(new Pose2d(38, 34, Math.toRadians(0)), Math.toRadians(0))
//                .strafeRight(7)
//                .build();
//
//        // Drive to the basket to deposit the first sample
//        driveToBasket1 = drive.trajectoryBuilder(driveToSample1.end())
//                .lineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)))
//                .build();
//
//        // Drive to pick up the second yellow sample
//        driveToSample2 = drive.trajectoryBuilder(driveToBasket1.end())
//                .splineToLinearHeading(new Pose2d(45, 27, Math.toRadians(0)), Math.toRadians(0))
//                .forward(3)
//                .build();
//
//        // Drive to the basket to deposit the second sample
//        driveToBasket2 = drive.trajectoryBuilder(driveToSample2.end())
//                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)), Math.toRadians(0))
//                .build();
//
//        // Drive to pick up the third sample
//        driveToSample3 = drive.trajectoryBuilder(driveToBasket2.end())
//                .splineToLinearHeading(new Pose2d(58, 27, Math.toRadians(0)), Math.toRadians(0))
//                .build();
//
//        // Drive to the basket to deposit the third sample
//        driveToBasket3 = drive.trajectoryBuilder(driveToSample3.end())
//                .back(5)
//                .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)),Math.toRadians(0))
//                .build();
//
//        // Drive to the parking area
//        driveToPark = drive.trajectoryBuilder(driveToBasket3.end())
//                .lineToLinearHeading(new Pose2d(25, 0, Math.toRadians(0)))
//                .build();

        //case RED:

        // Drive to deposit preloaded sample
        driveToDepositPreloadSample = drive.trajectoryBuilder(START_POSE_RED)
                .back(10)
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(225)), Math.toRadians(270))
                .build();

        // Drive to pick up the first yellow sample
        driveToSample1 = drive.trajectoryBuilder(driveToDepositPreloadSample.end())
                .splineToLinearHeading(new Pose2d(-38, -37 , Math.toRadians(180)), Math.toRadians(0))
                .strafeRight(12)
                .build();

        // Drive to the basket to deposit the first sample
        driveToBasket1 = drive.trajectoryBuilder(driveToSample1.end())
                .strafeLeft(5)
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(225)), Math.toRadians(90))
                .build();

        // Drive to pick up the second yellow sample
        driveToSample2 = drive.trajectoryBuilder(driveToBasket1.end())
                .splineToLinearHeading(new Pose2d(-48, -25, Math.toRadians(180)), Math.toRadians(180))
                .build();

        // Drive to the basket to deposit the second sample
        driveToBasket2 = drive.trajectoryBuilder(driveToSample2.end())
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(225)), Math.toRadians(180))
                .build();

        // Drive to pick up the third sample
        driveToSample3 = drive.trajectoryBuilder(driveToBasket2.end())
                .back(3)
                .lineToLinearHeading(new Pose2d(-58, -25, Math.toRadians(180)))
                .build();

        // Drive to the basket to deposit the third sample
        driveToBasket3 = drive.trajectoryBuilder(driveToSample3.end())
                .back(3)
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(225)),Math.toRadians(180))
                .build();

        // Drive to the parking area
        driveToPark = drive.trajectoryBuilder(driveToBasket3.end())
                .lineToLinearHeading(new Pose2d(-25, 0, Math.toRadians(180)))
                .build();

        //}

        // ------ Telemetry ------ //
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        switch (state) {
            case DRIVING_TO_DEPOSIT_PRELOAD_SAMPLE:
                driveToDepositPreloadSample();
                break;

            case DEPOSITING_PRELOAD_SAMPLE:
                depositPreloadSample();
                break;

            case DRIVING_TO_SAMPLE:
                driveToSample();
                break;

            case PICKING_UP_SAMPLE:
                pickUpSample();
                break;

            case DRIVING_TO_BASKET:
                driveToBasket();
                break;

            case DEPOSITING_SAMPLE:
                depositSample();
                break;

            case DRIVE_TO_PARKING:
                driveToParking();
                break;
        }
    }

    private void driveToDepositPreloadSample() {
        drive.followTrajectory(driveToDepositPreloadSample);
        if (!drive.isBusy()) {
            state = State.DEPOSITING_PRELOAD_SAMPLE;
        }
    }

    private void depositPreloadSample() {
        depositSample();
        state = State.DRIVING_TO_SAMPLE;
    }

    private void driveToSample() {
        if (sampleCount == 1) {
            drive.followTrajectory(driveToSample1);
            if (!drive.isBusy()) {
                state = State.PICKING_UP_SAMPLE;
            }
        } else if (sampleCount == 2) {
            drive.followTrajectory(driveToSample2);
            if (!drive.isBusy()) {
                state = State.PICKING_UP_SAMPLE;
            }
        } else {
            drive.followTrajectory(driveToSample3);
            if (!drive.isBusy()) {
                state = State.PICKING_UP_SAMPLE;
            }
        }
    }

    private void pickUpSample() {
        // code to pick up sample here :)
        state = State.DRIVING_TO_BASKET;
    }

    private void driveToBasket() {
        if (sampleCount == 1) {
            drive.followTrajectory(driveToBasket1);
            if (!drive.isBusy()) {
                state = State.DEPOSITING_SAMPLE;
            }
        } else if (sampleCount == 2) {
            drive.followTrajectory(driveToBasket2);
            if (!drive.isBusy()) {
                state = State.DEPOSITING_SAMPLE;
            }
        } else {
            drive.followTrajectory(driveToBasket3);
            if (!drive.isBusy()) {
                state = State.DEPOSITING_SAMPLE;
            }
        }
    }

    private void depositSample() {
        // code to deposit sample here :)
        if (sampleCount == 1) {
            sampleCount = 2;
            state = State.DRIVING_TO_SAMPLE;
        } else if (sampleCount == 2) {
            sampleCount = 3;
            state = State.DRIVING_TO_SAMPLE;
        } else {
            state = State.DRIVE_TO_PARKING;
        }
    }

    private void driveToParking() {
        drive.followTrajectory(driveToPark);
    }

}
