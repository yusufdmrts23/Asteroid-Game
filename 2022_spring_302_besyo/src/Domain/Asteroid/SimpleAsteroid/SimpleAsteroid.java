package Domain.Asteroid.SimpleAsteroid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import Domain.Asteroid.Asteroid;
import Domain.Asteroid.SimpleMove;
import Domain.Asteroid.GiftAsteroid.GiftAsteroid;

public class SimpleAsteroid extends Asteroid{
	
	protected BufferedImage simpleAsteroidImage;
	private int length = 30;
	private int width = 30;
	private SimpleMove simpleMove;
	
	public SimpleAsteroid (int xLoc, int yLoc, boolean freezeStatus, SimpleMove simpleMove) {
		super.xLoc = xLoc;
		super.yLoc = yLoc;
		super.freezeStatus = freezeStatus;
		this.simpleMove = simpleMove;
		try {
			simpleAsteroidImage = ImageIO.read(new FileImageInputStream(new File("Assets/Simple_Asteroid.png")));
		}
		catch(IOException ex) {
			Logger.getLogger(SimpleAsteroid.class.getName()).log(Level.SEVERE,null,ex);
		}
	}	
	
	public void move() {}
	
	
	/*
	 * Getter And Setter
	 */
	
	public BufferedImage getImage() {
		return this.simpleAsteroidImage;
	}
	public int getLength() {
		return length;
	}
	public int getWidth() {
		return width;
	}
	public void setSimpleMove(SimpleMove sm) {
		this.simpleMove = sm;
	}
	
	
	
}








