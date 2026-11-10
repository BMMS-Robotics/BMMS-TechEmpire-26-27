package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static org.firstinspires.ftc.robotcore.external.JavaUtil.createListWith;
import static org.firstinspires.ftc.robotcore.external.JavaUtil.maxOfList;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Robot {

  private final ElapsedTime runtime = new ElapsedTime();
  DcMotor leftFrontMotor;
  DcMotor rightFrontMotor;
  DcMotor leftBackMotor;
  DcMotor rightBackMotor;
  DcMotor intake;
  DcMotor pollenShooterSwag;
  DcMotor nectarShooterSwag;

  LinearOpMode opMode;

  static final double COUNTS_PER_TIRE_REV = 537.7;
  static final double WHEEL_DIAMETER_INCHES = 4.0;
  static final double COUNTS_PER_INCH = (COUNTS_PER_TIRE_REV) / (WHEEL_DIAMETER_INCHES * 3.1415);

  double drive = 0;        // Desired forward power/speed (-1 to +1)
  double strafe = 0;        // Desired strafe power/speed (-1 to +1)
  double turn = 0;
  // Or add a constructor/method to set this

  public Robot(LinearOpMode opMode) {
    this.opMode = opMode;
  }

  public void initializeHardware(HardwareMap hardwareMap) {
    // Initialize the drive system variables.
    leftFrontMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
    rightFrontMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
    leftBackMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
    rightBackMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
    intake = hardwareMap.get(DcMotor.class, "intake");

    // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
    // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
    // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
    leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
    rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
    leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
    rightBackMotor.setDirection(DcMotor.Direction.FORWARD);
  }

  public void setRunUsingEncoder() {
    leftFrontMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    rightFrontMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    leftBackMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    rightBackMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);

    leftFrontMotor.setMode(RunMode.RUN_USING_ENCODER);
    rightFrontMotor.setMode(RunMode.RUN_USING_ENCODER);
    leftBackMotor.setMode(RunMode.RUN_USING_ENCODER);
    rightBackMotor.setMode(RunMode.RUN_USING_ENCODER);
  }

  public void stopMotorEncoder() {
    rightFrontMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    leftFrontMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    rightBackMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    leftBackMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
  }

  public void leftStrafe() {
    rightFrontMotor.setPower(1);
    leftFrontMotor.setPower(-1);
    rightBackMotor.setPower(-1);
    leftBackMotor.setPower(1);
  }

  public void rightStrafe() {
    rightFrontMotor.setPower(-1);
    leftFrontMotor.setPower(1);
    rightBackMotor.setPower(1);
    leftBackMotor.setPower(-1);
  }

  public void stopMotors() {
    rightFrontMotor.setPower(0);
    leftFrontMotor.setPower(0);
    rightBackMotor.setPower(0);
    leftBackMotor.setPower(0);
  }

  /*
   *  Method to perform a relative move, based on encoder counts.
   *  Encoders are not reset as the move is based on the current position.
   *  Move will stop if any of three conditions occur:
   *  1) Move gets to the desired position
   *  2) Move runs out of time
   *  3) Driver stops the OpMode running.
   */
  public void runDriveInstructions(
      double speed,
      double leftInches,
      double rightInches,
      double timeoutS
  ) {
    int newLeftFrontTarget;
    int newRightFrontTarget;
    int newLeftBackTarget;
    int newRightBackTarget;

    // Ensure that the OpMode is still active
    if (opMode.opModeIsActive()) {

      // Determine new target position, and pass to motor controller
      newLeftFrontTarget =
          leftFrontMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
      newRightFrontTarget =
          rightFrontMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
      newLeftBackTarget = leftBackMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
      newRightBackTarget =
          rightBackMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
      leftFrontMotor.setTargetPosition(newLeftFrontTarget);
      rightFrontMotor.setTargetPosition(newRightFrontTarget);
      leftBackMotor.setTargetPosition(newLeftBackTarget);
      rightBackMotor.setTargetPosition(newRightBackTarget);

      // Turn On RUN_TO_POSITION
      leftFrontMotor.setMode(RunMode.RUN_TO_POSITION);
      rightFrontMotor.setMode(RunMode.RUN_TO_POSITION);
      leftBackMotor.setMode(RunMode.RUN_TO_POSITION);
      runtime.reset();
      leftFrontMotor.setPower(abs(speed));
      rightFrontMotor.setPower(abs(speed));
      leftBackMotor.setPower(abs(speed));
      rightBackMotor.setPower(abs(speed));

      // keep looping while we are still active, and there is time left, and both motors are running.
      // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
      // its target position, the motion will stop.  This is "safer" in the event that the robot will
      // always end the motion as soon as possible.
      // However, if you require that BOTH motors have finished their moves before the robot continues
      // onto the next step, use (isBusy() || isBusy()) in the loop test.
      while (opMode.opModeIsActive() &&
              (runtime.seconds() < timeoutS) &&
              (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() && leftBackMotor.isBusy()
                      && rightBackMotor.isBusy())) {
        opMode.sleep(10);
      }

      // Stop all motion;
      leftFrontMotor.setPower(0);
      rightFrontMotor.setPower(0);
      leftBackMotor.setPower(0);
      rightBackMotor.setPower(0);

      // Turn off RUN_TO_POSITION
      leftFrontMotor.setMode(RunMode.RUN_USING_ENCODER);
      rightFrontMotor.setMode(RunMode.RUN_USING_ENCODER);
      leftBackMotor.setMode(RunMode.RUN_USING_ENCODER);
      rightBackMotor.setMode(RunMode.RUN_USING_ENCODER);
      rightBackMotor.setMode(RunMode.RUN_TO_POSITION);

      // reset the timeout time and start motion.

      opMode.sleep(100);   // pause after each move.
    }
  }

  public void moveRobot(float axial, float lateral, double yaw) {
    // Calculate wheel powers.
    double frontLeftPower = axial + lateral + yaw;
    double frontRightPower = axial - lateral - yaw;
    double backLeftPower = axial - lateral + yaw;
    double backRightPower = axial + lateral - yaw;

    // Normalize wheel powers to be less than 1.0
    double max = maxOfList(createListWith(
        abs(frontLeftPower), abs(frontRightPower), abs(backLeftPower), abs(backRightPower
        )));

    if (max > 1.0) {
      frontLeftPower /= max;
      frontRightPower /= max;
      backLeftPower /= max;
      backRightPower /= max;
    }

    double speedScaleFactor = 0.65;
    // Send powers to the wheels.
    leftFrontMotor.setPower(frontLeftPower * speedScaleFactor);
    rightFrontMotor.setPower(frontRightPower * speedScaleFactor);
    leftBackMotor.setPower(backLeftPower * speedScaleFactor);
    rightBackMotor.setPower(backRightPower * speedScaleFactor);
  }

  public void startPollenShooter() {
    pollenShooterSwag.setPower(-0.4);
  }

  public void startNectarShooter() {
    nectarShooterSwag.setPower(0.55);
  }
  public void startShooters() {
    startPollenShooter();
    startNectarShooter();
  }
  public void stopNectarShooter() {
    nectarShooterSwag.setPower(0);
  }
  public void stopPollenShooter() {
    pollenShooterSwag.setPower(0);
  }

  public void stopShooters() {
  stopNectarShooter();
  stopPollenShooter();

  }
  public void startWindowWasher() {
    //bruh
  }
}


