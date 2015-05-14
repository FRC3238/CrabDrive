package org.usfirst.frc.team3238.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * Crab drive uses 2 sets of Wheels(Mecanum Drive, High Traction) Pnuematics can
 * cycle between the two different drives
 * 
 * @author Nick Sorensen
 */

public class Robot extends IterativeRobot
{

	Autonomous autonomous;
	Chassis chassis;
	Talon leftFrontTalon, rightFrontTalon, leftRearTalon, rightRearTalon;
	Joystick joystickZero;
	Encoder leftFront, rightFront;
	DoubleSolenoid frontSolenoid, backSolenoid;
	ArrayList<String> fileContents;

	public void robotInit()
	{
		final int CHASSISLEFTFRONTTALONID = 3;
		final int CHASSISLEFTREARTALONID = 2;
		final int CHASSISRIGHTFRONTTALONID = 1;
		final int CHASSISRIGHTREARTALONID = 0;
		final int JOYSTICKPORT = 0;
		final int LEFTFRONTENCODERA = 3;
		final int LEFTFRONTENCODERB = 2;
		final int RIGHTFRONTENCODERA = 0;
		final int RIGHTFRONTENCODERB = 1;
		final int FRONTUPSOLENOID = 2;
		final int FRONTDOWNSOLENOID = 0;
		final int BACKUPSOLENOID = 3;
		final int BACKDOWNSOLENOID = 1;

		joystickZero = new Joystick(JOYSTICKPORT);

		leftFrontTalon = new Talon(CHASSISLEFTFRONTTALONID);
		rightFrontTalon = new Talon(CHASSISRIGHTFRONTTALONID);
		leftRearTalon = new Talon(CHASSISLEFTREARTALONID);
		rightRearTalon = new Talon(CHASSISRIGHTREARTALONID);

		frontSolenoid = new DoubleSolenoid(FRONTUPSOLENOID, FRONTDOWNSOLENOID);
		backSolenoid = new DoubleSolenoid(BACKUPSOLENOID, BACKDOWNSOLENOID);

		leftFront = new Encoder(LEFTFRONTENCODERA, LEFTFRONTENCODERB);
		rightFront = new Encoder(RIGHTFRONTENCODERA, RIGHTFRONTENCODERB);

		chassis = new Chassis(leftFrontTalon, leftRearTalon, rightFrontTalon, rightRearTalon);

		autonomous = new Autonomous(chassis);
	}

	public void autonomousInit()
	{

		autonomous.init();
		leftFront.reset();
		rightFront.reset();

	}

	public void autonomousPeriodic()
	{
		autonomous.idle(chassis, frontSolenoid, backSolenoid, leftFront, rightFront);
		chassis.idle();
	}

	public void teleopPeriodic()
	{
		System.out.println("Right " + rightFront.get() + "  Left: " + leftFront.get());
		if (joystickZero.getRawButton(1))
		{
			frontSolenoid.set(DoubleSolenoid.Value.kForward);
			backSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		else if (joystickZero.getRawButton(2))
		{
			frontSolenoid.set(DoubleSolenoid.Value.kReverse);
			backSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
		else if (joystickZero.getRawButton(3))
		{
			frontSolenoid.set(DoubleSolenoid.Value.kForward);
			backSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
		else if (joystickZero.getRawButton(4))
		{
			frontSolenoid.set(DoubleSolenoid.Value.kReverse);
			backSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		else
		{
			frontSolenoid.set(DoubleSolenoid.Value.kOff);
			backSolenoid.set(DoubleSolenoid.Value.kOff);
		}

		double x = joystickZero.getX();
		double y = joystickZero.getY();
		double twist = joystickZero.getTwist();
		double deadzone = 0.15;

		if (twist < 0)
		{
			if (twist > -deadzone)
			{
				twist = 0;
			}
			else
			{
				twist += deadzone;
				twist = twist * (1 / (1 - deadzone));
			}
		}
		else
		{
			if (twist < deadzone)
			{
				twist = 0;
			}
			else
			{
				twist -= deadzone;
				twist = twist * (1 / (1 - deadzone));
			}
		}

		/*
		 * Maps the value of the joystick using the current position of the
		 * throttle slider for safety and ease of driver control.
		 */
		double throttleMapping = Math.abs((joystickZero.getThrottle() - 1));
		chassis.setJoystickData(x * throttleMapping * -1, y * throttleMapping * -1, twist * -1);
		chassis.idle();

	}

	public void testPeriodic()
	{

	}

}
