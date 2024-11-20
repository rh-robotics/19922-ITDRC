# 19922-ITDRC

Repository containing code for FTC Team 19922 Iron Lions' INTO THE DEEP Season!

## Table of Contents

- [Port Map](#port-map)
  - [Control Hub](#control-hub)
- [Controller Map](#controller-map)

## Port Map

### Control Hub

| **_Port_**      | **_Configuration Name_** | **_Type_**                   |
| --------------- | ------------------------ | ---------------------------- |
| **Motor 0**     | `rightFront`             | 435 RPM GoBilda YellowJacket |
| **Encoder 0**   | `odoLeft`                | Swingram Odometry Pod        |
| **Motor 1**     | `rightRear`              | 435 RPM Gobilda YellowJacket |
| **Encoder 2**   | `odoRear`                | Swingram Odometry Pod        |
| **Motor 2**     | `leftFront`              | 435 GoBilda YellowJacket     |
| **Encoder 2**   | `odoRight`               | Swingram Odometry Pod        |
| **Motor 3**     | `leftRear`               | 435 GoBilda YellowJacket     |
| **USB 2.0**     |                          |                              |
| **USB 3.0**     |                          |                              |
| **USB C**       |                          |                              |
| **USB Mini**    |                          |                              |
| **I2C 0**       | `imu`                    | REV Robotics Internal IMU    |
| **I2C 1**       |                          |                              |
| **I2C 3**       |                          |                              |
| **Analog 0:1**  |                          |                              |
| **Analog 2:3**  |                          |                              |
| **Digital 0:1** |                          |                              |
| **Digital 2:3** |                          |                              |
| **Digital 4:5** |                          |                              |
| **Digital 6:7** |                          |                              |
| **Servo 0**     | `intakeBeltServo`        |                              |
| **Servo 1**     | `intakeWheelServo`       |                              |
| **Servo 2**     | `specimenServo`          |                              |
| **Servo 3**     |                          |                              |
| **Servo 4**     |                          |                              |
| **Servo 5**     |                          |                              |
| **UART 1**      |                          |                              |
| **UART 2**      |                          |                              |
| **UART 3**      |                          |                              |
| **UART 4**      |                          |                              |

## Controller Map

| **_Control_**      | **_Action_**                          |
| ------------------ | ------------------------------------- |
| **Left JoyStick**  | _Driving_ - Forward/Backward + Strafe |
| **Right JoyStick** |                                       |
| **Dpad Up**        |                                       |
| **Dpad Down**      |                                       |
| **Dpad Left**      |                                       |
| **Dpad Right**     |                                       |
| **Left Trigger**   | _Driving_ - Turn Left                 |
| **Left Bumper**    |                                       |
| **Right Trigger**  | _Driving_ - Turn Right                |
| **Right Bumper**   |                                       |
| **Y Button**       |                                       |
| **A Button**       |                                       |
| **X Button**       |                                       |
| **B Button**       |                                       |
| **Back Button**    |                                       |
| **Start Button**   |                                       |
