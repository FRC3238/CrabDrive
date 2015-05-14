package org.usfirst.frc.team3238.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Interprets Visual Vectors from TurtleGUIdance
 * 
 * @author Nick Sorensen
 */

public class Autonomous
{

	final double ANGLEENCODERCONSTANT = 3000;
	final double DISTANCEENCODERCONSTANT = 12.5667;

	private ArrayList<String> gCode;

	private int tempLeft, tempRight;
	private int min = 0, max, current; // range of gCode needed

	private double angle, distance; // angle is radians, distance is inches
	private double currentAngle, currentDistance;
	private double anglePConstant = 2, angleIConstant = .0005;
	private double distancePConstant = 4, distanceIConstant = .001;
	private double syncPConstant = 1, syncIConstant = .0001;
	private double distanceThrottle = 0.65, angleThrottle = 0.50;

	private boolean angled = false; // if the angle is correct
	private boolean goneDaDistance = false;

	PIController anglePIController, distancePIController, syncPIController;

	public Autonomous(Chassis chassis)
	{

	}

	public void init()
	{
		gCode = FileReader.readFile("drive.txt");

		for (int i = 0; i < gCode.size(); i++)
		{
			if (gCode.get(i).substring(0, 1).equals("V") && min == 0) // only
																		// sets
																		// min
																		// once.
			{
				min = i;
			}

			if (gCode.get(i).substring(0, 3).equals("END"))
			{
				max = i - 1;
				break;
			}
		}
		current = min;

		anglePIController = new PIController(anglePConstant, angleIConstant);
		distancePIController = new PIController(distancePConstant, distanceIConstant);
		syncPIController = new PIController(syncPConstant, syncIConstant);

		anglePIController.setThrottle(angleThrottle);
		distancePIController.setThrottle(distanceThrottle);
		syncPIController.setThrottle(0.25);
	}

	public void idle(Chassis chassis, DoubleSolenoid frontSolenoid, DoubleSolenoid backSolenoid,
			Encoder left, Encoder right)
	{
		frontSolenoid.set(DoubleSolenoid.Value.kReverse);
		backSolenoid.set(DoubleSolenoid.Value.kForward);

		if (current <= max)
		{
			String vector = gCode.get(current).substring(2);

			angle = Double.parseDouble(vector.substring(0, vector.indexOf(" ")));
			distance = Double.parseDouble(vector.substring(vector.indexOf(" ") + 1));

			if (angled != true)
			{
				currentAngle = ((right.get() - left.get()) / ANGLEENCODERCONSTANT) * 2.0 * Math.PI;
				chassis.setJoystickData(0, 0, anglePIController.getMotorValue(angle, currentAngle));

				if (Math.abs(angle - currentAngle) < .02)
				{
					angled = true;
				}
			}
			else
			{
				if (goneDaDistance != true)
				{
					tempRight = right.get();
					goneDaDistance = true;
				}

				currentDistance = ((right.get() - tempRight) / 360.0) * DISTANCEENCODERCONSTANT;

				chassis.setJoystickData(0,
						distancePIController.getMotorValue(distance, currentDistance),
						syncPIController.getMotorValue(right.get(), left.get()));
				// adjust the twist to drive stright

				if (Math.abs(distance - currentDistance) < 1)
				{
					angled = false;
					goneDaDistance = false;
					current++;

					chassis.setJoystickData(0, 0, 0);

					anglePIController.reinit();
					distancePIController.reinit();
				}
			}
		}
		else
		{
			System.out.println("Done With GCODE!");
			chassis.setJoystickData(0, 0, 0);
		}

	}
}
