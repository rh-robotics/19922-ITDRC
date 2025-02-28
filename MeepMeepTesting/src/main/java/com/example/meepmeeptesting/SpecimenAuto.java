package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class SpecimenAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-14.5, 62, Math.toRadians(90.00)))
                        .lineTo(new Vector2d(0, 35))
                        .waitSeconds(1)

                        .splineTo(new Vector2d(-36, 35), Math.toRadians(270))

                        .splineToConstantHeading(new Vector2d(-36, 20), Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-42, 13), Math.toRadians(270))
                        .lineTo(new Vector2d(-45, 55))

//                        .splineToConstantHeading(new Vector2d(-47, 20), Math.toRadians(270))
//                        .splineToConstantHeading(new Vector2d(-50, 13), Math.toRadians(270))
//                        .splineToConstantHeading(new Vector2d(-55, 50), Math.toRadians(270))
//
//                        .splineToConstantHeading(new Vector2d(-57, 20), Math.toRadians(270))
//                        .splineToConstantHeading(new Vector2d(-60, 13), Math.toRadians(270))
//                        .splineToConstantHeading(new Vector2d(-60, 50), Math.toRadians(270))
//
//                        .lineToConstantHeading(new Vector2d(-45, 62))
//
//                        .lineToConstantHeading(new Vector2d(0, 35))
//
//                        .lineToConstantHeading(new Vector2d(-45, 62))

                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .exportTrajectoryImage("path.png")
                .start();
    }
}