package Domain.Ball;

import java.awt.*;
import java.util.Observable;
import java.util.Vector;

public class Ball extends Observable {
	private int xLocation = 100;
	private int yLocation = 100;
	private int xVelocity = 4;
	private int yVelocity = 4;
	private final double RADIUS = 8.5;

	public void move(int paddleLocation, int paddleAngle) {
		xLocation += xVelocity;
		yLocation += yVelocity;

		if (xLocation <= 10) {
			reflectHorizontal();
		} else if (xLocation >= 1820 - RADIUS * 2) {
			reflectHorizontal();
		}

		if (yLocation <= 10) {
			reflectVertical();
		}

		if (intersectionWithPaddle(paddleLocation,paddleAngle)) {

			double incomingAngleOverall;
			if(xVelocity == 0) {
				incomingAngleOverall = Math.toRadians(90);
			} else {
				incomingAngleOverall = Math.atan(yVelocity/xVelocity);
			}

			double incomingAngleWPaddle;

			if(xVelocity >= 0) {
				incomingAngleWPaddle = incomingAngleOverall + Math.toRadians(paddleAngle);
			} else {
				incomingAngleWPaddle = incomingAngleOverall - Math.toRadians(paddleAngle);
			}

			double outgoingAngleOverall = incomingAngleWPaddle - Math.toRadians(paddleAngle);

			double xDivYVeloc = Math.tan(outgoingAngleOverall);
			
			double xCoeff = 1/xDivYVeloc;

			double ySquared = 32*xCoeff/(Math.abs(xCoeff)+1);
			yVelocity = -(int)Math.sqrt(Math.abs(ySquared));

			double xSquared = (32)/(Math.abs(xCoeff)+1);
			int xSign = xVelocity/Math.abs(xVelocity);

			xVelocity = (int)(xSign*Math.sqrt(Math.abs(xSquared)));



			

		}

		if (yLocation >= 860) {
			setChanged();
			notifyObservers();
		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(xLocation, yLocation, (int) RADIUS * 2, (int) RADIUS * 2);
	}

	private boolean intersectionWithPaddle(int paddleLocation, int paddleAngle) {
		int[] xCoors = new int[4];
		int[] yCoors = new int[4];

		double paddleInnerAngle = Math.atan(0.20);
		double lengthToCornerFromCenter = Math.sqrt((10*10) + (50*50));

		int centerXCoord = paddleLocation + 50;
		int centerYCoord = 820;

		double firstCornerYLength = Math.sin(Math.toRadians(paddleAngle) + paddleInnerAngle)*lengthToCornerFromCenter;
		double firstCornerXLength = Math.cos(Math.toRadians(paddleAngle) + paddleInnerAngle)*lengthToCornerFromCenter;

		double ndCornerXDistanceFromFirst = 20*Math.sin(Math.toRadians(paddleAngle));
		double ndCorderYDistanceFromFirst = 20*Math.cos(Math.toRadians(paddleAngle));

		xCoors[0] = (int)(centerXCoord - firstCornerXLength);
		xCoors[1] = (int)(xCoors[0] - ndCornerXDistanceFromFirst);
		xCoors[2] = (int)(centerXCoord + firstCornerXLength);
		xCoors[3] = (int)(xCoors[2] + ndCornerXDistanceFromFirst);

		yCoors[0] = (int)(centerYCoord - firstCornerYLength);
		yCoors[1] = (int)(yCoors[0] + ndCorderYDistanceFromFirst);
		yCoors[2] = (int)(centerYCoord + firstCornerYLength);
		yCoors[3] = (int)(yCoors[2] - ndCorderYDistanceFromFirst);

		Polygon interPoly = new Polygon(xCoors, yCoors, 4);

		return interPoly.intersects(xLocation, yLocation, 17, 17);

	}

	public void reflectVertical() {
		yVelocity = -yVelocity;
	}

	public void reflectHorizontal() {
		xVelocity = - xVelocity;
	}

	public void reset() {
		xLocation = 100;
		yLocation = 100;
		xVelocity = 4;
		yVelocity = 4;
	}
	
	/*
	 * Getter And Setter
	 */
	
	public int getXLocation() {
		return xLocation;
	}
	public int getYLocation() {
		return yLocation;
	}
	public double getRadius() {
		return RADIUS;
	}
	public void setXLocation(int xLocation) {
		this.xLocation = xLocation;
	}
	public void setYLocation(int yLocation) {
		this.yLocation = yLocation;
	}
	
}
