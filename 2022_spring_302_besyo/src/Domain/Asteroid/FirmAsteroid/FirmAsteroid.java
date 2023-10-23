package Domain.Asteroid.FirmAsteroid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import Domain.Asteroid.Asteroid;
import Domain.Asteroid.SimpleMove;
import Domain.Asteroid.GiftAsteroid.GiftAsteroid;

public class FirmAsteroid  extends Asteroid{
	
	private int radius;
	protected BufferedImage firmAsteroidImage;
	private SimpleMove simpleMove;

	
	public FirmAsteroid(int xLoc,int yLoc, boolean freezeStatus, int radius, SimpleMove simpleMove) {
		super.xLoc = xLoc;
		super.yLoc = yLoc;
		super.freezeStatus = freezeStatus;
		this.radius = radius;
		this.simpleMove = simpleMove;
		try {
			firmAsteroidImage = ImageIO.read(new FileImageInputStream(new File("Assets/Firm_Asteroid.png")));
		}
		catch(IOException ex) {
			Logger.getLogger(FirmAsteroid.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	
	public void move() {}
	
	public boolean hit() {
		if (this.radius > 30) {
			this.radius -= 1;
			return false;
		}
		else {
			return true;
		}
	}
	
	/*
	 * Getter And Setter
	 */
	

	public BufferedImage getImage() {
		return this.firmAsteroidImage;
	}
	public int getRadius() {
		return this.radius;
	}
	public void setSimpleMove(SimpleMove sm) {
		this.simpleMove = sm;
	}
}
