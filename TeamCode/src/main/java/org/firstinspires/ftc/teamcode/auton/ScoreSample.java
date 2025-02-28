package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(name = "Score Sample", group = "Autonomous Methods")
public class ScoreSample extends OpMode {
    HWC robot;
    private enum ScoreSampleState {
        MOVE_TO_HIGH_CHAMBER,
        MOVE_TO_HIGH_BASKET,
        DROP_SPECIMEN,
        RETURN_HOME,
    }

    private ScoreSampleState scoreSampleState = ScoreSampleState.MOVE_TO_HIGH_CHAMBER;

    @Override
    public void init() {
        robot = new HWC(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        // Initial movement to high chamber
        robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[3]);
        robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[3]);
    }

    @Override
    public void loop() {
        // Update all PID movements
        robot.vSlideLeftComponent.moveUsingPID();
        robot.vSlideRightComponent.moveUsingPID();
        robot.turretComponent.moveUsingPID();

        // State machine
        switch (scoreSampleState) {
            case MOVE_TO_HIGH_CHAMBER:
                if (robot.vSlideLeftComponent.closeEnough(400) &&
                        robot.vSlideRightComponent.closeEnough(400)) {
                    robot.turretComponent.setTarget(HWC.turretPositions[2]);
                    robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[5]);
                    robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[5]);
                    scoreSampleState = ScoreSampleState.MOVE_TO_HIGH_BASKET;
                }

                break;

            case MOVE_TO_HIGH_BASKET:
                if (robot.vSlideLeftComponent.closeEnough(50) &&
                        robot.vSlideRightComponent.closeEnough(50)) {
                    robot.bucketServo.setPosition(1);
                    robot.time.reset();
                    scoreSampleState = ScoreSampleState.DROP_SPECIMEN;
                }

                break;

            case DROP_SPECIMEN:
                if (robot.time.milliseconds() > 500) {
                    robot.vSlideLeftComponent.setTarget(HWC.vSlidePositions[0]);
                    robot.vSlideRightComponent.setTarget(HWC.vSlidePositions[0]);
                    scoreSampleState = ScoreSampleState.RETURN_HOME;
                }

                break;

            case RETURN_HOME:
                if (robot.vSlideLeftComponent.closeEnough(50) &&
                        robot.vSlideRightComponent.closeEnough(50)) {
                    requestOpModeStop();
                }

                break;
        }

        telemetry.addData("Current State", scoreSampleState);
        telemetry.update();
    }
}