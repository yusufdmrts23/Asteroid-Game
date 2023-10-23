package Domain.Paddle;

import java.awt.*;

public class Paddle {
	private int angle = 0;
	private int angularVelocity = 5;
	private final int HEIGHT = 20;
	private final int WIDTH = 100;
	private final int VELOCITY = WIDTH / 2;
	private int location = (1820 - WIDTH) / 2;

	public void draw(Graphics g) {
		((Graphics2D) g).rotate(Math.toRadians(angle),location+(WIDTH/2),820);
		g.setColor(Color.yellow);
		g.fillRect(location, 810, WIDTH, HEIGHT);
	}

	public void moveLeft() {
		if (location - VELOCITY < 10) {
			location = 10;
		} else {
			location -= VELOCITY;
		}
	}

	public void moveRight() {
		if (location + WIDTH + VELOCITY > 1810) {
			location = 1810 - WIDTH;
		} else {
			location += VELOCITY;
		}
	}

	public void rotateRight() {
		if(angle <= 40) {
			angle+=angularVelocity;
		}
	}

	public void rotateLeft() {
		if(angle >= -40) {
			angle-=angularVelocity;
		}
	}

	public void reset() {
		location = (1810 - WIDTH) / 2;
		angle = 0;
	}

	public int getLocation() {
		return location;
	}

	public int getAngle() {
		return angle;
	}
	
	public void setLocation(int location) {
		this.location = location;
	}
}