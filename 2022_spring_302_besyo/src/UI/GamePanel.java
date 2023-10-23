package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import Domain.Alien.ProtectingAlien;
import Domain.Asteroid.CircularMove;
import Domain.Asteroid.SimpleMove;
import Domain.Asteroid.Explosion.Explosion;
import Domain.Asteroid.FirmAsteroid.FirmAsteroid;
import Domain.Asteroid.GiftAsteroid.GiftAsteroid;
import Domain.Asteroid.SimpleAsteroid.SimpleAsteroid;
import Domain.Asteroid.SimpleAsteroid.SimpleAsteroidFactory;
import Domain.Asteroid.ExplosiveAsteroid.ExplosiveAsteroid;
import Domain.Ball.*;
import Domain.Paddle.*;
import GameManager.GameManager;

public class GamePanel extends JPanel {
	private Ball ball;
	private Paddle paddle;
	private int screenWidth = 1820;
	private int screenHeight = 840;
	private GameManager gameManager;

	private Point prevPt;
	private int currentAsteroid = -1;
	private Point[] asteroidPoints;
	private SimpleAsteroid[] simpleAsteroids;
	private FirmAsteroid[] firmAsteroids;
	private ExplosiveAsteroid[] explosiveAsteroids;
	private GiftAsteroid[] giftAsteroids;
	
	private int explosionTimer = 0;

	private int simpleAsteroidAmount;
	private int firmAsteroidAmount;
	private int explosiveAsteroidAmount;
	private int giftAsteroidAmount;
	
	private ProtectingAlien protectingAlien;

	KeyListenerHandler kHandler = new KeyListenerHandler();
	
	public GamePanel(GameManager gameManager, Ball ball, Paddle paddle) {
		this.gameManager = gameManager;
		this.ball = ball;
		this.paddle = paddle;
		setBackground(Color.WHITE);
		super.addKeyListener(kHandler);
		ClickListener clickListener = new ClickListener();
		DragListener dragListener= new DragListener();
		this.addMouseListener(clickListener);
		this.addMouseMotionListener(dragListener);

		this.asteroidPoints = new Point[gameManager.getFirmAsteroidNumber()+gameManager.getSimpleAsteroidNumber()+gameManager.getExplosiveAsteroidNumber()+gameManager.getGiftAsteroidNumber()];
	}
	
	private class ClickListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			if(!gameManager.isHasBeenPlay()) {
				for(int i = 0; i<asteroidPoints.length; i++) {
					if((e.getX()>=(int)asteroidPoints[i].x && e.getX()<=(int)asteroidPoints[i].x+50) && (e.getY()>=(int)asteroidPoints[i].y && e.getY()<=(int)asteroidPoints[i].y+50)) {
						prevPt = asteroidPoints[i];
						currentAsteroid = i;
					}
				}
			}
			
		}
	}

	private class DragListener extends MouseMotionAdapter{
		public void mouseDragged(MouseEvent e) {
			if(prevPt != null && currentAsteroid>=simpleAsteroids.length && currentAsteroid<simpleAsteroids.length+firmAsteroids.length) {
				Point currentPt = e.getPoint();
				Point currentPtFixed = new Point(e.getPoint().x - e.getPoint().x%80, e.getPoint().y - e.getPoint().y%80);
				asteroidPoints[currentAsteroid].setLocation(currentPtFixed);
				firmAsteroids[currentAsteroid-simpleAsteroids.length].setXLoc(currentPtFixed.x);
				firmAsteroids[currentAsteroid-simpleAsteroids.length].setYLoc(currentPtFixed.y);
				SimpleMove simpleMove = new SimpleMove(currentPtFixed.x);
				firmAsteroids[currentAsteroid-simpleAsteroids.length].setSimpleMove(simpleMove);
				gameManager.getAsteroids()[currentAsteroid].setXLoc(currentPtFixed.x);
				gameManager.getAsteroids()[currentAsteroid].setYLoc(currentPtFixed.y);
				prevPt = currentPt;
				repaint();
			}

			if(prevPt != null && currentAsteroid<simpleAsteroids.length && currentAsteroid>=0) {
				Point currentPt = e.getPoint();
				Point currentPtFixed = new Point(e.getPoint().x - e.getPoint().x%80, e.getPoint().y - e.getPoint().y%80);
				asteroidPoints[currentAsteroid].setLocation(currentPtFixed);
				simpleAsteroids[currentAsteroid].setXLoc(currentPtFixed.x);
				simpleAsteroids[currentAsteroid].setYLoc(currentPtFixed.y);
				SimpleMove simpleMove = new SimpleMove(currentPtFixed.x);
				simpleAsteroids[currentAsteroid].setSimpleMove(simpleMove);
				gameManager.getAsteroids()[currentAsteroid].setXLoc(currentPtFixed.x);
				gameManager.getAsteroids()[currentAsteroid].setYLoc(currentPtFixed.y);
				prevPt = currentPt;
				repaint();
			}

			if(prevPt != null && currentAsteroid>=simpleAsteroids.length+firmAsteroids.length && currentAsteroid<simpleAsteroids.length+firmAsteroids.length+explosiveAsteroids.length) {
				Point currentPt = e.getPoint();
				Point currentPtFixed = new Point(e.getPoint().x - e.getPoint().x%80, e.getPoint().y - e.getPoint().y%80);
				asteroidPoints[currentAsteroid].setLocation(currentPtFixed);
				explosiveAsteroids[currentAsteroid-simpleAsteroids.length-firmAsteroids.length].setXLoc(currentPtFixed.x);
				explosiveAsteroids[currentAsteroid-simpleAsteroids.length-firmAsteroids.length].setYLoc(currentPtFixed.y);
				CircularMove circularMove = new CircularMove(currentPtFixed.x , currentPtFixed.y);
				explosiveAsteroids[currentAsteroid-simpleAsteroids.length-firmAsteroids.length].setCircularMove(circularMove);
				gameManager.getAsteroids()[currentAsteroid].setXLoc(currentPtFixed.x);
				gameManager.getAsteroids()[currentAsteroid].setYLoc(currentPtFixed.y);
				prevPt = currentPt;
				repaint();
			}

			if(prevPt != null && currentAsteroid>=simpleAsteroids.length+firmAsteroids.length+explosiveAsteroids.length) {
				Point currentPt = e.getPoint();
				Point currentPtFixed = new Point(e.getPoint().x - e.getPoint().x%80, e.getPoint().y - e.getPoint().y%80);
				asteroidPoints[currentAsteroid].setLocation(currentPtFixed);
				giftAsteroids[currentAsteroid-simpleAsteroids.length-firmAsteroids.length-explosiveAsteroids.length].setXLoc(currentPtFixed.x);
				giftAsteroids[currentAsteroid-simpleAsteroids.length-firmAsteroids.length-explosiveAsteroids.length].setYLoc(currentPtFixed.y);
				gameManager.getAsteroids()[currentAsteroid].setXLoc(currentPtFixed.x);
				gameManager.getAsteroids()[currentAsteroid].setYLoc(currentPtFixed.y);
				prevPt = currentPt;
				repaint();
			}
			
			
		}
	}
		
	private class KeyListenerHandler implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			int c = e.getKeyCode();

			if (c == KeyEvent.VK_W) {
				gameManager.setPlay(true);
				gameManager.setHasBeenPlay(true);
				currentAsteroid = -1;
			}

			if (gameManager.isPlay()) {
				if (c == KeyEvent.VK_RIGHT) {
					gameManager.movePaddleRight();
				} else if (c == KeyEvent.VK_LEFT) {
					gameManager.movePaddleLeft();
				}
			}
			
			if (gameManager.isPlay()) {
				if (c == KeyEvent.VK_P) {
					gameManager.setPlay(false);
				}
			}
			if (gameManager.isPlay() == false) {
				if (c == KeyEvent.VK_W) {
					gameManager.setPlay(true);
				}
			}
			
			if (gameManager.isPlay()) {
				if (c == KeyEvent.VK_D) {
					gameManager.rotatePaddleRight();
				} else if (c == KeyEvent.VK_A) {
					gameManager.rotatePaddleLeft();
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {

			int c = e.getKeyCode();

			if (gameManager.isPlay()) {
				if (c == KeyEvent.VK_D) {
					while(gameManager.getPaddle().getAngle() != 0) {
						gameManager.rotatePaddleLeft();
					}
				} else if (c == KeyEvent.VK_A) {
					while(gameManager.getPaddle().getAngle() != 0) {
						gameManager.rotatePaddleRight();
					}
				}
			}

		}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	private int iterateLength = 0;
	private int iterateLength2 = 0;
	private int iterateLength3 = 0;

	private void drawSimpleAsteroids(Graphics g) {
		this.simpleAsteroids = gameManager.getSimpleAsteroids();
		int length = simpleAsteroids.length;
		for (int i = 0; i < length ; i++,iterateLength++,iterateLength2++,iterateLength3++) {
			if(simpleAsteroids[i] != null) {
				asteroidPoints[i] = new Point(simpleAsteroids[i].getXLoc(),simpleAsteroids[i].getYLoc());
				g.drawImage(simpleAsteroids[i].getImage(), asteroidPoints[i].x, asteroidPoints[i].y, simpleAsteroids[i].getLength(), simpleAsteroids[i].getWidth(), this);
			}
		}
	}

	private void drawFirmAsteroids(Graphics g) {
		this.firmAsteroids = gameManager.getFirmAsteroids();
		int length = firmAsteroids.length;
		for (int i = 0; i < length; i++,iterateLength++,iterateLength2++,iterateLength3++) {
			if(firmAsteroids[i] != null) {
				asteroidPoints[iterateLength] = new Point(firmAsteroids[i].getXLoc(),firmAsteroids[i].getYLoc());
				g.drawImage(firmAsteroids[i].getImage(), asteroidPoints[iterateLength].x, asteroidPoints[iterateLength].y, firmAsteroids[i].getRadius(), firmAsteroids[i].getRadius(), this);
			}
		}
		iterateLength = 0;
	}

	private void drawExplosiveAsteroids(Graphics g) {
		this.explosiveAsteroids = gameManager.getExplosiveAsteroids();
		int length = explosiveAsteroids.length;
		for (int i = 0; i< length; i++,iterateLength2++,iterateLength3++) {
			if (explosiveAsteroids[i] != null) {
				asteroidPoints[iterateLength2] = new Point(explosiveAsteroids[i].getXLoc(),explosiveAsteroids[i].getYLoc());
				g.drawImage(explosiveAsteroids[i].getImage(), asteroidPoints[iterateLength2].x, asteroidPoints[iterateLength2].y, explosiveAsteroids[i].getLength(), explosiveAsteroids[i].getWidth(), this);
			}
		}
		iterateLength2 = 0;
	}
	
	private void drawGiftAsteroids(Graphics g) {
		this.giftAsteroids = gameManager.getGiftAsteroids();
		int length = giftAsteroids.length;
		for (int i = 0; i< length; i++,iterateLength3++) {
			if (giftAsteroids[i] != null) {
				asteroidPoints[iterateLength3] = new Point(giftAsteroids[i].getXLoc(),giftAsteroids[i].getYLoc());
				g.drawImage(giftAsteroids[i].getImage(), asteroidPoints[iterateLength3].x, asteroidPoints[iterateLength3].y, giftAsteroids[i].getLength(), giftAsteroids[i].getWidth(), this);
			}
		}
		iterateLength3 = 0;
	}

	private void drawExplosion(Graphics g) {
		Explosion explosion = gameManager.getExplosion();
		if (explosion != null && explosionTimer != 100) {
			g.drawImage(explosion.getImage(), explosion.getXLoc(), explosion.getYLoc(), explosion.getRadius(), explosion.getRadius(), this);
			explosionTimer += 5;
			if (explosionTimer == 100) {
				gameManager.makeExplosionNull();
			}
			explosionTimer = explosionTimer %100;
		}
	}
	
	private void drawProtectingAlien(Graphics g) {
		this.protectingAlien = gameManager.getProtectingAlien();
		if (protectingAlien != null) {
			g.drawImage(protectingAlien.getImage(), protectingAlien.getXLoc(), protectingAlien.getYLoc(), protectingAlien.getLength(), protectingAlien.getWidth(), this);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0,0,screenWidth,10);
		g.fillRect(0,0,10, screenHeight);
		g.fillRect(screenWidth - 10, 0, 10 ,screenHeight);

		g.setColor(new Color(30, 144, 255));
		g.fillRect(10, screenHeight - 10, screenWidth - 20, 10);

		ball.draw(g);
		this.drawSimpleAsteroids(g);
		this.drawFirmAsteroids(g);
		this.drawExplosiveAsteroids(g);
		this.drawExplosion(g);
		this.drawGiftAsteroids(g);
		this.drawProtectingAlien(g);
		paddle.draw(g);
		
		
		
	}
	
	
	public GameManager getGameManager() {
		return this.gameManager;
	}
}
