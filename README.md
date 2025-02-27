# 19922-ITDRC

Repository containing code for FTC Team 19922 Iron Lions' INTO THE DEEP Season!

## Table of Contents

- [Port Map](#port-map)
  - [Control Hub](#control-hub)
- [Controller Map](#controller-map)

## Port Map

### Control Hub

| **_Port_**      | **_Configuration Name_**                   | **_Type_**                                 |
|-----------------|--------------------------------------------|--------------------------------------------|
| **Motor 0**     | `rightFront`                               | GoBilda 5203 Series - 435 RPM YellowJacket |
| **Encoder 0**   |                                            |                                            |
| **Motor 1**     | `rightRear`                                | GoBilda 5203 Series - 435 RPM YellowJacket |
| **Encoder 1**   | `odoRight`                                 |                                            |
| **Motor 2**     | `leftFront`                                | GoBilda 5203 Series - 435 RPM YellowJacket |
| **Encoder 2**   | `odoFront`                                 |                                            |
| **Motor 3**     | `leftRear`                                 | GoBilda 5203 Series - 435 RPM YellowJacket |
| **Encoder 3**   | `odoLeft`                                  | GoBilda 4-Bar Odometery Pod                |                                    
| **USB 2.0**     |                                            |                                            |
| **USB 3.0**     |                                            |                                            |
| **USB C**       |                                            |                                            |
| **USB Mini**    |                                            |                                            |
| **I2C 0**       | `imu`                                      | REV Robotics Internal IMU                  |
| **I2C 1**       | `intakeColorSensor`                        | REV Color Sensor V3 (ColorRange Sensor)    |
| **I2C 2**       |                                            |                                            |
| **I2C 3**       |                                            |                                            |
| **Analog 0:1**  | `hSlideLeftEncoder` : `hSlideRightEncoder` |                                            |
| **Analog 2:3**  |                                            |                                            |
| **Digital 0:1** |                                            |                                            |
| **Digital 2:3** |                                            |                                            |
| **Digital 4:5** |                                            |                                            |
| **Digital 6:7** |                                            |                                            |
| **Servo 0**     | `intakeServo1`                             | GoBilda Dual Mode Speed Servo (CR)         |
| **Servo 1**     | `intakeServo2`                             | GoBilda Dual Mode Speed Servo (CR)         |
| **Servo 2**     | `hSlideLeftServo`                          | GoBilda 5-Turn Dual Mode Speed Servo (CR)  |
| **Servo 3**     | `hSlideRightServo`                         | GoBilda 5-Turn Dual Mode Speed Servo (CR)  |
| **Servo 4**     | `swingServoRight`                          | GoBilda Dual Mode Torque Servo (Position)  |
| **Servo 5**     | `swingServoLeft`                           | GoBilda Dual Mode Torque Servo (Position)  |
| **UART 1**      |                                            |                                            |
| **UART 2**      |                                            |                                            |
| **UART 3**      |                                            |                                            |
| **UART 4**      |                                            |                                            |

### Expansion Hub

| **_Port_**      | **_Configuration Name_** | **_Type_** |
|-----------------|--------------------------|------------|
| **Motor 0**     | `turretMotor`            |            |
| **Encoder 0**   | ^^                       |            |
| **Motor 1**     | `linearActuatorMotor`    |            |
| **Encoder 2**   | ^^                       |            |
| **Motor 2**     | `vSlideRightMotor`       |            |
| **Encoder 2**   | ^^                       |            |
| **Motor 3**     | `vSlideLeftMotor`        |            |
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

| **_Control_**          | **_Action_**                                                               |
|------------------------|----------------------------------------------------------------------------|
| **Left JoyStick**      | _Driving_ - Forward/Backward + Strafe                                      |
| **Right JoyStick**     |                                                                            |
| **Dpad Up**            | _Turret_ - Increment Vertical Slide Position                               |
| **Dpad Down**          | _Turret_ - Decrement Vertical Slide Position                               |
| **Dpad Left**          | _Intake_ - Decrement Horizontal Slide Position                             |
| **Dpad Right**         | _Intake_ - Increment Horizontal Slide Position                             |
| **Left Trigger**       | _Driving_ - Turn Left                                                      |
| **Right Trigger**      | _Driving_ - Turn Right                                                     |
| **Left Bumper**        | _Intake_ - Outtake / Stop                                                  |
| **Right Bumper**       | _Intake_ - Intake / Stop                                                   |
| **Cross Button**       | _Turret_ - Specimen Claw                                                   |
| **Circle Button**      | _Turret_ - Bucket                                                          |
| **Square Button**      | _Intake_ - Toggle Intake Swing (Intake/Transfer)                           |
| **Triangle Button**    | _Turret_ - Rotate Turret 180 (Toggle)                                      |
| **Touchpad**           | _Intake_ - Set Control Mode (Left/Middle/Right - Yellow Only/All/Alliance) |
| **Left Stick Button**  |                                                                            |
| **Right Stick Button** |                                                                            |
| **Options Button**     |                                                                            |
| **Share Button**       |                                                                            |
