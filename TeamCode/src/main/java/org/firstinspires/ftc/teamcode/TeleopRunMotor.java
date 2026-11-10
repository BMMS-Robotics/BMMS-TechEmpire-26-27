/* Copyright (c) 2025 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
/*
 * This OpMode illustrates how to program your robot to drive field relative.  This means
 * that the robot drives the direction you push the joystick regardless of the current orientation
 * of the robot.
 *
 * This OpMode assumes that you have four mecanum wheels each on its own motor named:
 *   front_left_motor, front_right_motor, back_left_motor, back_right_motor
 *
 *   and that the left motors are flipped such that when they turn clockwise the wheel moves backwards
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 */
@TeleOp(name = "Test: Run Motor", group = "Robot")
public class TeleopRunMotor extends OpMode {
    // This declares the four motors needed
    DcMotor test0;

    double power;
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;

    @Override
    public void init() {
        test0 = hardwareMap.get(DcMotor.class, "test0");

        // We set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        test0.setDirection(DcMotor.Direction.REVERSE);

        // This uses RUN_USING_ENCODER to be more accurate.   If you don't have the encoder
        // wires, you should remove these
        test0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        power = 0;
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // If you press the A button, then you reset the Yaw to be zero from the way
        // the robot is currently pointing

        if (gamepad1.a) {
            power = 0.25;
            telemetry.addLine("Button A Pressed");
            telemetry.update();
            test0.setPower(power);
        }

        if (gamepad1.b) {
            power = 0.5;
            telemetry.addLine("Button B Pressed");
            telemetry.update();
            test0.setPower(power);
        }
        if (gamepad1.x) {
            power = 0.75;
            telemetry.addLine("Button X Pressed");
            telemetry.update();
            test0.setPower(power);
        }
        if (gamepad1.y) {
            power = 0.01;
            telemetry.addLine("Button Y Pressed");
            telemetry.update();
            test0.setPower(power);
        }



        /*if (gamepad1.x){
            telemetry.addData("X button Pressed, power before: ", power);
            power = -power;
            telemetry.addData("Power after: ", power);
            telemetry.update();
            test0.setPower(power);
        }
        if (gamepad1.left_trigger_pressed) {
            telemetry.addData("Trigger button pressed, left_trigger: ", gamepad1.left_trigger);
            power = (gamepad1.left_trigger - 0.5)*2;
            telemetry.addData("Power: ", power);
            telemetry.update();
            test0.setPower(power);
        }
        if (gamepad1.y) {
            power = 0;
            test0.setPower(power);
        }*/
    }
}
