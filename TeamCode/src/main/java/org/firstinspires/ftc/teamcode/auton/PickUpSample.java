package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(name = "Pick Up Sample", group = "Autonomous Methods")
public class PickUpSample extends OpMode {
    HWC robot;

    @Override
    public void init() {
        // Update Telemetry to show that the robot is initializing
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Initialize Robot Hardware
        robot = new HWC(hardwareMap, telemetry);

        // Initialize Controllers
        gamepad1.type = Gamepad.Type.SONY_PS4;
        gamepad2.type = Gamepad.Type.SONY_PS4;
        gamepad1.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);
        gamepad2.setLedColor(((double) 161 / 255), ((double) 129 / 255), ((double) 27 / 255), Gamepad.LED_DURATION_CONTINUOUS);

        // Update Telemetry to show that the robot has been initialized
        telemetry.addData("> Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        robot.hSlideLeftServo.setPosition(1); // TODO: fix this, update on if too
        robot.hSlideRightServo.setPosition(0); // out

        robot.swingServoLeft.setPosition(0.5); // Intake
        robot.swingServoRight.setPosition(0.5);

//        robot.bucketServo.setPosition(0); // transfer // TODO: add this back

        // Precondition: turret is in the transfer position, slides are all the way down
    }

    @Override
    public void loop() {
//        if (robot.hSlideLeftServo.getPosition() == HWC.hSlidePositions[1] && robot.intakeColorSensor.getDistance(DistanceUnit.CM) < 2 &&
//                robot.hSlideRightServo.getPosition() == 0) {
//            robot.swingServoLeft.setPosition(0.15); // transfer
//            robot.swingServoRight.setPosition(0.85);
//
//            robot.hSlideLeftServo.setPosition(0); // TODO: fix this
//            robot.hSlideRightServo.setPosition(1); // in
//
//            try {
//                wait(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else if (robot.hSlideRightServo.getPosition() == 1) {
//            robot.intakeServo1.setPower(-1);
//            robot.intakeServo2.setPower(-1);
//
//            try {
//                wait(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            requestOpModeStop();
//        }
    }
}
