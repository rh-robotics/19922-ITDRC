package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.HWC;

@Autonomous(name = "Pick Up Sample", group = "Autonomous Methods")
public class PickUpSample extends OpMode {
    private HWC robot;
    private double waitMarker = -1000;

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
        robot.time.reset();

        robot.hSlideLeftServo.setPosition(HWC.hSlideLeftPositions[3]);
        robot.hSlideRightServo.setPosition(HWC.hSlideRightPositions[3]); // out

        robot.swingServoLeft.setPosition(0.5); // Intake
        robot.swingServoRight.setPosition(0.5);

        robot.intakeServo1.setPower(-1);
        robot.intakeServo2.setPower(1);

//        robot.bucketServo.setPosition(0); // transfer // TODO: add this back

        // Precondition: turret is in the transfer position, slides are all the way down
    }

    @Override
    public void loop() {
        if (robot.time.time() - waitMarker < 1) {
            return;
        }

        if (robot.hSlideLeftServo.getPosition() == HWC.hSlideLeftPositions[2] && robot.intakeColorSensor.getDistance(DistanceUnit.CM) < 2 &&
                robot.hSlideRightServo.getPosition() == HWC.hSlideRightPositions[2]) {
            robot.swingServoLeft.setPosition(0.15); // transfer
            robot.swingServoRight.setPosition(0.85);

            robot.hSlideLeftServo.setPosition(HWC.hSlideLeftPositions[1]);
            robot.hSlideRightServo.setPosition(HWC.hSlideRightPositions[1]); // in

            waitMarker = robot.time.time(); // 1s wait

        } else if (robot.hSlideRightServo.getPosition() == 1) {
            robot.intakeServo1.setPower(-1);
            robot.intakeServo2.setPower(-1);

            waitMarker = robot.time.time(); // 1s wait

            requestOpModeStop();
        }
    }
}
