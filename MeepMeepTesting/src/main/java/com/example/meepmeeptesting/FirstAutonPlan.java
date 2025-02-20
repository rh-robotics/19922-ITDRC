package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class FirstAutonPlan {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(25, 62.5, Math.toRadians(90)))
                        .lineTo(new Vector2d(0,35))
                        .waitSeconds(2)
                        .splineToLinearHeading(new Pose2d(38, 41, Math.toRadians(0)), Math.toRadians(0))
                        .strafeRight(14)
                        .waitSeconds(2)
                        .strafeLeft(20)
                        .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)), Math.toRadians(90))
                        .waitSeconds(2)
                        .splineToLinearHeading(new Pose2d(48, 27, Math.toRadians(0)), Math.toRadians(0))
                        .waitSeconds(2)
                        .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)), Math.toRadians(0))
                        .waitSeconds(2)
                        .back(10)
                        .splineToLinearHeading(new Pose2d(58, 27, Math.toRadians(0)), Math.toRadians(0))
                        .waitSeconds(2)
                        .back(15)
                        .splineToLinearHeading(new Pose2d(54, 54, Math.toRadians(45)),Math.toRadians(0))
                        .waitSeconds(2)
                        .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}