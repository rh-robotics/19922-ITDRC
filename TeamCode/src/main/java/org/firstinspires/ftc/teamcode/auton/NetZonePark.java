package org.firstinspires.ftc.teamcode.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(group = "Final Autons", name = "Net Zone Park")
public class NetZonePark extends OpMode {
    private Trajectory scoreNet, park, moveForward;
    private String activeTrajectory = "";

    private HWC robot;

    private final Pose2d START_POSE = new Pose2d(32.2, 62.5, Math.toRadians(90));

    @Override
    public void init() {
        robot = new HWC(hardwareMap, telemetry);

        robot.drive.setPoseEstimate(START_POSE);

        scoreNet = robot.drive.trajectoryBuilder(START_POSE)
                .back(7)
                .lineToLinearHeading(new Pose2d(37, 29, Math.toRadians(90)))
                .lineToLinearHeading(new Pose2d(37, 10, Math.toRadians(90-27)))
                .forward(47)
                .build();

        park = robot.drive.trajectoryBuilder(scoreNet.end())
                .splineToLinearHeading(new Pose2d(34, 10, Math.toRadians(180)), Math.toRadians(45))
                .forward(10)
                .build();

        moveForward = robot.drive.trajectoryBuilder(park.end())
                .forward(4)
                .build();

        robot.time.reset();
    }

    @Override
    public void start() {
        robot.drive.followTrajectory(scoreNet);
        robot.drive.followTrajectory(park);
    }

    @Override
    public void loop() {
        robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[3]);
        robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[3]);

        robot.vSlideLeftComponent.moveUsingPID();
        robot.vSlideRightComponent.moveUsingPID();
        robot.turretComponent.moveUsingPID();

        if (robot.time.time() > 25) {
            robot.turretComponent.setTarget(HWC.turretPositions[0]);
        }

        if (robot.time.time() > 28) {
            robot.drive.followTrajectory(moveForward);
            requestOpModeStop();
        }
    }
}
