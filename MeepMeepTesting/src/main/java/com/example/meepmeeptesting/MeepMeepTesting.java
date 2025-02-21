package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-14.5, 62, Math.toRadians(90)))
                        // drop off first
                        .lineTo(new Vector2d(0, 35))
                        .waitSeconds(0.5)

                        // collect third
                        .splineTo(new Vector2d(-36, 35), Math.toRadians(270))

                        .splineToConstantHeading(new Vector2d(-36, 20), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-42, 13), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-45, 50), Math.toRadians(270))

                        // collect fourth
                        .splineToConstantHeading(new Vector2d(-47, 20), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-50, 13), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-55, 50), Math.toRadians(270))

                        // collect fifth
                        .splineToConstantHeading(new Vector2d(-57, 20), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-60, 13), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-60, 50), Math.toRadians(270))

                        // pick up second
                        .lineToConstantHeading(new Vector2d(-45, 62))
                        .waitSeconds(0.5)

                        // score second
                        .lineToConstantHeading(new Vector2d(0, 35))
                        .waitSeconds(0.5)

                        // pick up third
                        .lineToConstantHeading(new Vector2d(-45, 62))
                        .waitSeconds(0.5)

                        // score third
                        .lineToConstantHeading(new Vector2d(0, 35))
                        .waitSeconds(0.5)

                        // pick up fourth
                        .lineToConstantHeading(new Vector2d(-45, 62))
                        .waitSeconds(0.5)

                        // score fourth
                        .lineToConstantHeading(new Vector2d(0, 35))
                        .waitSeconds(0.5)

                        // pick up fifth
                        .lineToConstantHeading(new Vector2d(-45, 62))
                        .waitSeconds(0.5)

                        // score fifth
                        .lineToConstantHeading(new Vector2d(0, 35))
                        .waitSeconds(0.5)

                        // park
                        .lineToConstantHeading(new Vector2d(-45, 62))

                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .exportTrajectoryImage("path.png")
                .start();
    }
}