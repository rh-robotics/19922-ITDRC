# 19922-ITDRC

Repository containing code for FTC Team 19922 Iron Lions' INTO THE DEEP Season!

## Table of Contents

- [Port Map](#port-map)
  - [Control Hub](#control-hub)
- [Controller Map](#controller-map)

## Port Map

### Control Hub

| **_Port_**      | **_Configuration Name_**   | **_Type_**                   |
| --------------- |----------------------------| ---------------------------- |
| **Motor 0**     | `rightFront`               | 435 RPM GoBilda YellowJacket |
| **Encoder 0**   | `odoLeft`                  | Swingram Odometry Pod        |
| **Motor 1**     | `rightRear`                | 435 RPM Gobilda YellowJacket |
| **Encoder 2**   | `odoRear`                  | Swingram Odometry Pod        |
| **Motor 2**     | `leftFront`                | 435 GoBilda YellowJacket     |
| **Encoder 2**   | `odoRight`                 | Swingram Odometry Pod        |
| **Motor 3**     | `leftRear`                 | 435 GoBilda YellowJacket     |
| **USB 2.0**     |                            |                              |
| **USB 3.0**     |                            |                              |
| **USB C**       |                            |                              |
| **USB Mini**    |                            |                              |
| **I2C 0**       | `imu`                      | REV Robotics Internal IMU    |
| **I2C 1**       | `intakeColorSensor`        |                              |
| **I2C 2**       | `specimenColorSensor`      |                              |
| **I2C 3**       |                            |                              |
| **Analog 0:1**  |                            |                              |
| **Analog 2:3**  |                            |                              |
| **Digital 0:1** |                            |                              |
| **Digital 2:3** |                            |                              |
| **Digital 4:5** |                            |                              |
| **Digital 6:7** |                            |                              |
| **Servo 0**     | `intakeBeltServo`          |                              |
| **Servo 1**     | `intakeWheelServo`         |                              |
| **Servo 2**     | `specimenClawServo`        |                              |
| **Servo 3**     | `swingServoLeft`           |                              |
| **Servo 4**     | `swingServoRight`          |                              |
| **Servo 5**     | `intakeVerticalJointServo` |                              |
| **UART 1**      | `swingExtensionLeft`       |                              |
| **UART 2**      | `swingExtensionRight`      |                              |
| **UART 3**      |                            |                              |
| **UART 4**      |                            |                              |

### Expansion Hub

| **_Port_**      | **_Configuration Name_** | **_Type_** |
| --------------- | ------------------------ | ---------- |
| **Motor 0**     | `turretMotor`            |            |
| **Encoder 0**   | ^^                       |            |
| **Motor 1**     | `linearActuatorMotor`    |            |
| **Encoder 2**   | ^^                       |            |
| **Motor 2**     | `slideLeftMotor`         |            |
| **Encoder 2**   | ^^                       |            |
| **Motor 3**     | `slideRightMotor`        |            |
| **Encoder 3**   | ^^                       |            |
| **USB 2.0**     |                          |            |
| **USB 3.0**     |                          |            |
| **USB C**       |                          |            |
| **USB Mini**    |                          |            |
| **I2C 0**       |                          |            |
| **I2C 1**       |                          |            |
| **I2C 3**       |                          |            |
| **Analog 0:1**  |                          |            |
| **Analog 2:3**  |                          |            |
| **Digital 0:1** |                          |            |
| **Digital 2:3** |                          |            |
| **Digital 4:5** |                          |            |
| **Digital 6:7** |                          |            |
| **Servo 0**     |                          |            |
| **Servo 1**     |                          |            |
| **Servo 2**     |                          |            |
| **Servo 3**     |                          |            |
| **Servo 4**     |                          |            |
| **Servo 5**     |                          |            |
| **UART 1**      |                          |            |
| **UART 2**      |                          |            |
| **UART 3**      |                          |            |
| **UART 4**      |                          |            |

## Controller Map

| **_Control_**          | **_Action_**                                       |
| ---------------------- |----------------------------------------------------|
| **Left JoyStick**      | _Driving_ - Forward/Backward + Strafe              |
| **Right JoyStick**     | _Swing Extension_ - Extend/Retract Swing Extension |
| **Dpad Up**            | _Slide_ - Increment Position                       |
| **Dpad Down**          | _Slide_ - Decrement Position                       |
| **Dpad Left**          | _Turret_ - Increment Position                      |
| **Dpad Right**         | _Turret_ - Decrement Position                      |
| **Left Trigger**       | _Driving_ - Turn Left                              |
| **Right Trigger**      | _Driving_ - Turn Right                             |
| **Left Bumper**        | _Intake_ - Outtake                                 |
| **Right Bumper**       | _Intake_ - Intake                                  |
| **Cross Button**       | _Swing_ - Increment Position                       |
| **Circle Button**      | _Swing_ - Decrement Position                       |
| **Square Button**      | _Speed Multiplier_ - Strafe Speed                  |
| **Triangle Button**    |                                                    |
| **Touchpad**           | _Alliance_ - Switch                                |
| **Left Stick Button**  | _Turret_ - Reset Encoder                           |
| **Right Stick Button** | _Slide_ - Reset Encoders                           |
| **Options Button**     | _Linear Actuator_ - Toggle Between Powers          |
| **Share Button**       |                                                    |
