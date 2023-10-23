package GameManager;

import Domain.Asteroid.FirmAsteroid.FirmAsteroid;
import Domain.Asteroid.FirmAsteroid.FirmAsteroidFactory;
import Domain.Asteroid.GiftAsteroid.GiftAsteroid;
import Domain.Asteroid.GiftAsteroid.GiftAsteroidFactory;
import Domain.Asteroid.SimpleAsteroid.SimpleAsteroid;
import Domain.Asteroid.SimpleAsteroid.SimpleAsteroidFactory;
import Domain.Ball.Ball;
import Domain.Paddle.Paddle;
import Domain.Alien.ProtectingAlien;
import Domain.Asteroid.Asteroid;
import Domain.Asteroid.Explosion.Explosion;
import Domain.Asteroid.ExplosiveAsteroid.ExplosiveAsteroid;
import Domain.Asteroid.ExplosiveAsteroid.ExplosiveAsteroidFactory;
import Domain.Asteroid.ExplosiveAsteroid.ExplosiveAsteroidMoveable;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;

import Database.DocumentManager;
import Database.MongoJava;

public class GameManager implements Observer {
    private Ball ball;
    private Paddle paddle;
    private boolean play = false;
	private boolean hasBeenPlay = false;

    private SimpleAsteroid[] simpleAsteroids;
	private SimpleAsteroidFactory simpleAsteroidFactory;
	private int simpleAsteroidNumber;
	
	private FirmAsteroid[] firmAsteroids;
	private FirmAsteroidFactory firmAsteroidFactory;
	private int firmAsteroidNumber;

	private ExplosiveAsteroid[] explosiveAsteroids;
	private ExplosiveAsteroidFactory explosiveAsteroidFactory;
	private int explosiveAsteroidNumber;
	
	private GiftAsteroid[] giftAsteroids;
	private GiftAsteroidFactory giftAsteroidFactory;
	private int giftAsteroidNumber;

	private Explosion explosion;
	
	private Asteroid[] asteroids;
	
	private ObjectId saveGameId;
	private MongoDatabase database = MongoJava.getDatabase();
	private DocumentManager docManager;
	
	private ProtectingAlien protectingAlien;
    
	private boolean hasBeenSaved = false;
	
	private ObjectId userId;
	
    public GameManager(Ball ball, Paddle paddle, int simpleAmount, int firmAmount, int explosiveAmount, int giftAmount, ObjectId userId) {
        this.ball = ball;
        this.paddle = paddle;
        this.userId = userId;
        ball.addObserver(this);
        
		this.simpleAsteroidNumber = simpleAmount;
		this.firmAsteroidNumber = firmAmount;
		this.explosiveAsteroidNumber = explosiveAmount;
		this.giftAsteroidNumber = giftAmount;

        this.simpleAsteroids = new SimpleAsteroid[simpleAsteroidNumber];
		this.simpleAsteroidFactory = SimpleAsteroidFactory.getInstance();
		this.initSimpleAsteroids();
		
		this.firmAsteroids = new FirmAsteroid[firmAsteroidNumber];
		this.firmAsteroidFactory = FirmAsteroidFactory.getInstance();
		this.initFirmAsteroids();

		this.explosiveAsteroids = new ExplosiveAsteroid[explosiveAsteroidNumber];
		this.explosiveAsteroidFactory = ExplosiveAsteroidFactory.getInstance();
		this.initExplosiveAsteroids();

		this.giftAsteroids = new GiftAsteroid[giftAsteroidNumber];
		this.giftAsteroidFactory = GiftAsteroidFactory.getInstance();
		this.initGiftAsteroids();
		
		this.asteroids = new Asteroid[simpleAsteroidNumber + firmAsteroidNumber + explosiveAsteroidNumber + giftAsteroidNumber];
			
		placeAsteroidsToRows();
    }
    
    public GameManager(Ball ball, Paddle paddle, List<SimpleAsteroid> sList, List<FirmAsteroid> fList, List<ExplosiveAsteroid> eList, List<GiftAsteroid> gList, ObjectId userId) {

    	this.ball = ball;
        this.paddle = paddle;
        this.userId = userId;
        ball.addObserver(this);
        
        this.simpleAsteroidNumber = sList.size();
        this.firmAsteroidNumber = fList.size();
		this.explosiveAsteroidNumber = eList.size();
		this.giftAsteroidNumber = gList.size();
		    
		this.simpleAsteroids = new SimpleAsteroid[simpleAsteroidNumber];
		this.firmAsteroids = new FirmAsteroid[firmAsteroidNumber];
		this.explosiveAsteroids = new ExplosiveAsteroid[explosiveAsteroidNumber];
		this.giftAsteroids = new GiftAsteroid[giftAsteroidNumber];
		
		for(int i = 0; i < simpleAsteroidNumber; i++) {
			this.simpleAsteroids[i] = sList.get(i);
		}
		
		for(int i = 0; i < firmAsteroidNumber; i++) {
			firmAsteroids[i] = fList.get(i);
		}
		
		for(int i = 0; i < explosiveAsteroidNumber; i++) {
			explosiveAsteroids[i] = eList.get(i);
		}
		
		for(int i = 0; i < giftAsteroidNumber; i++) {
			giftAsteroids[i] = gList.get(i);
		}
		
		this.asteroids = new Asteroid[simpleAsteroidNumber + firmAsteroidNumber + explosiveAsteroidNumber + giftAsteroidNumber];
		System.arraycopy(simpleAsteroids, 0, asteroids, 0, simpleAsteroidNumber);
		System.arraycopy(firmAsteroids, 0, asteroids, simpleAsteroidNumber, firmAsteroidNumber);
		System.arraycopy(explosiveAsteroids, 0, asteroids, simpleAsteroidNumber + firmAsteroidNumber, explosiveAsteroidNumber);
		System.arraycopy(giftAsteroids, 0, asteroids, simpleAsteroidNumber + firmAsteroidNumber + explosiveAsteroidNumber, giftAsteroidNumber);
    }

    @Override
    public void update(Observable o, Object arg) {
        play = false;
        ball.reset();
        paddle.reset();
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

	public boolean isHasBeenPlay() {
		return hasBeenPlay;
	}

	public void setHasBeenPlay(boolean hasBeenPlay) {
		this.hasBeenPlay = hasBeenPlay;
	}
	
	public boolean isHasBeenSaved() {
		return this.hasBeenSaved;
	}
	
	public void setHasBeenSaved(boolean hasBeenSaved) {
		this.hasBeenSaved = hasBeenSaved;
	}

    /*
     * Init Asteroids
     */
	
	public void addSavegameToDatabase() {
		docManager = new DocumentManager();
		docManager.setDatabase(database);
		
		if(!this.hasBeenSaved) {
			this.saveGameId = docManager.addSavegame(paddle, ball, userId);
		} else {
			docManager.updateSavegame(paddle, ball, saveGameId);
		}
		
	}
	
	public void saveAsteroidsToDatabase() {
		docManager = new DocumentManager();
		docManager.setDatabase(database);
		
		if(!this.hasBeenSaved) {
			docManager.addAsteroids(asteroids, simpleAsteroidNumber, simpleAsteroidNumber+firmAsteroidNumber, simpleAsteroidNumber+firmAsteroidNumber+explosiveAsteroidNumber, saveGameId);
		} else {
			docManager.updateAsteroids(asteroids, saveGameId);
		}

	}
	
	public void loadAsteroidsFromDatabase() {
		
	}
	
	private void placeAsteroidsToRows() {
		int totalAsteroid = simpleAsteroidNumber + firmAsteroidNumber + explosiveAsteroidNumber + giftAsteroidNumber;
		System.arraycopy(simpleAsteroids, 0, asteroids, 0, simpleAsteroidNumber);
		System.arraycopy(firmAsteroids, 0, asteroids, simpleAsteroidNumber, firmAsteroidNumber);
		System.arraycopy(explosiveAsteroids, 0, asteroids, simpleAsteroidNumber + firmAsteroidNumber, explosiveAsteroidNumber);
		System.arraycopy(giftAsteroids, 0, asteroids, simpleAsteroidNumber + firmAsteroidNumber + explosiveAsteroidNumber, giftAsteroidNumber);
		
		int[][] locs = new int[100][2];
		
		for (int i = 0; i< 5 ;i++ ) {
			for (int j = 0; j < 20; j++) {
				locs[i*20+j][0] = j*90+ 20;
				locs[i*20+j][1] = i*80+ 20;
			}
		}
		
		
		int[] randomAsteroidIndex = RandomListGenerator.generateRandom(totalAsteroid);
		
		for (int i = 0; i < asteroids.length; i++) {
			if (asteroids[randomAsteroidIndex[i]] instanceof ExplosiveAsteroidMoveable) {
				ExplosiveAsteroidMoveable curr = (ExplosiveAsteroidMoveable) asteroids[randomAsteroidIndex[i]];
				curr.setXcenterYCenter(locs[i][0], locs[i][1]);
			}
			asteroids[randomAsteroidIndex[i]].setXLoc(locs[i][0]);
			asteroids[randomAsteroidIndex[i]].setYLoc(locs[i][1]);
		}
		
	}
    
	private void initSimpleAsteroids() {
		
		for (int i = 0;i < simpleAsteroidNumber; i++) {
			simpleAsteroids[i] = (simpleAsteroidFactory.getSimpleAsteroid());
		}
		
	}
	
	private void initFirmAsteroids() {
		
		for (int i = 0;i < firmAsteroidNumber; i++) {
			firmAsteroids[i] = (firmAsteroidFactory.getFirmAsteroid());
		}
		
	}

	private void initExplosiveAsteroids() {
		for (int i = 0;i < explosiveAsteroidNumber; i++) {
			explosiveAsteroids[i] = (explosiveAsteroidFactory.getExplosiveAsteroid());
		}
	}
	
	private void initGiftAsteroids() {
		for (int i = 0; i < giftAsteroidNumber; i++) {
			giftAsteroids[i] = giftAsteroidFactory.getGiftAsteroid(i); // It should be i; For testing change 
		}
	}

    
    /*
     * Move Objects
     */
    
    public void moveBall() {
    	ball.move(paddle.getLocation(), paddle.getAngle());
    }
    
    public void movePaddleRight() {
    	paddle.moveRight();
    }
    
    public void movePaddleLeft() {
    	paddle.moveLeft();
    }

	public void rotatePaddleRight() {
		paddle.rotateRight();
	}

	public void rotatePaddleLeft() {
		paddle.rotateLeft();
	}
    
    /*
     * Move Asteroids
     */
    
    public void moveSimpleAsteroids() {
		simpleAsteroidCollision();
		int length = simpleAsteroids.length;
		for (int i = 0; i < length ; i++) {
			if (simpleAsteroids[i] != null) {
				simpleAsteroids[i].move();
			}
		}
	}
    
    public void moveFirmAsteroids() {
		firmAsteroidCollision();
		int length = firmAsteroids.length;
		for (int i = 0; i < length ; i++) {
			if (firmAsteroids[i] != null) {
				firmAsteroids[i].move();
			}
		}
	}
    
	public void moveExplosiveAsteroids() {
    	explosiveAsteroidCollision();
    	int length = explosiveAsteroids.length;
    	for (int i = 0; i < length; i++) {
    		if (explosiveAsteroids[i] != null) {
    			explosiveAsteroids[i].move();
    		}
    	}
    	
    }
	
	public void moveGiftAsteroids() {
		giftAsteroidCollision();
	}

    /*
     * Collision
     */
    
    private void simpleAsteroidCollision() {
    	int length = simpleAsteroids.length;
		for (int i = 0; i < length ; i++) {
			if (simpleAsteroids[i] != null  &&  new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(simpleAsteroids[i].getXLoc(),simpleAsteroids[i].getYLoc(),simpleAsteroids[i].getLength(),simpleAsteroids[i].getWidth()))) {
				ball.reflectVertical();
				simpleAsteroids[i] = null;
				asteroids[i] = null;
			}
			if(ball.getYLocation()== simpleAsteroids[i].getYLoc()){
				ball.reflectHorizontal();
			}
		}
    }
    
    private void firmAsteroidCollision() {
    	int length = firmAsteroids.length;
		for (int i = 0; i < length ; i++) {
			if (firmAsteroids[i] != null  &&  new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(firmAsteroids[i].getXLoc(),firmAsteroids[i].getYLoc(),firmAsteroids[i].getRadius(),firmAsteroids[i].getRadius()))) {
				ball.reflectVertical();
				if (firmAsteroids[i].hit()) {
					firmAsteroids[i] = null;
					asteroids[simpleAsteroidNumber + i] =  null;
					if(ball.getYLocation()== firmAsteroids[i].getYLoc()){
						ball.reflectHorizontal();
					}
				}
				
			}
		}
    }

	private void explosiveAsteroidCollision() {
    	int length = explosiveAsteroids.length;
    	for (int i = 0; i < length ; i++) {
			if (explosiveAsteroids[i] != null  &&  new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(explosiveAsteroids[i].getXLoc(),explosiveAsteroids[i].getYLoc(),explosiveAsteroids[i].getLength(),explosiveAsteroids[i].getWidth()))) {
				ball.reflectVertical();
				this.explosion = new Explosion(explosiveAsteroids[i].getXLoc(), explosiveAsteroids[i].getYLoc());
				explosiveAsteroids[i] = null;
				asteroids[firmAsteroidNumber + simpleAsteroidNumber + i] = null;
				if(ball.getYLocation()== explosiveAsteroids[i].getYLoc()){
					ball.reflectHorizontal();
				}
				explosion();
				
			}
		}
    }
	
	private void giftAsteroidCollision() {
		int length = giftAsteroids.length;
		for (int i = 0; i < length ; i++) {
			if (giftAsteroids[i] != null  &&  new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(giftAsteroids[i].getXLoc(),giftAsteroids[i].getYLoc(),giftAsteroids[i].getLength(),giftAsteroids[i].getWidth()))) {
				ball.reflectVertical();
				callAlien(giftAsteroids[i].getGift());
				giftAsteroids[i] = null;
				asteroids[explosiveAsteroidNumber + firmAsteroidNumber + simpleAsteroidNumber + i] = null;
				if(ball.getYLocation()== giftAsteroids[i].getYLoc()){
					ball.reflectHorizontal();
				}
			}
		}
	}
	
	/*
	 * Call Alien
	 */
	
	private void callAlien(String alienType) {
		System.out.println(alienType);
		if (alienType.equals("Protecting Alien")) {
			protectingAlien = new ProtectingAlien(500, 400);
		}
	}
	
	public void performProtectingAlien() {
		protectingAlienCollision();
		if (protectingAlien != null) {
			this.protectingAlien.perform();
		}
	}
	
	private void protectingAlienCollision() {
		if (protectingAlien != null && ball.getYLocation() > protectingAlien.getXLoc() && 
				new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(protectingAlien.getXLoc(),protectingAlien.getYLoc(), protectingAlien.getLength(), protectingAlien.getWidth()))) {
			ball.reflectVertical();
			this.protectingAlien = null;
		}
		else if (protectingAlien != null && ball.getYLocation() <= protectingAlien.getXLoc() && 
				new Rectangle(ball.getXLocation(),  ball.getYLocation(), (int) ball.getRadius(), (int) ball.getRadius()).intersects( new Rectangle(protectingAlien.getXLoc(),protectingAlien.getYLoc(), protectingAlien.getLength(), protectingAlien.getWidth()))) {
			ball.reflectVertical();
		}
	}
	
	
    /*
     * Explosion
     */
    
    private void explosion() {
    	int firmAssteroidLength = firmAsteroids.length;
		for (int i = 0; i < firmAssteroidLength ; i++) {
			if (firmAsteroids[i] != null  &&  new Rectangle(explosion.getXLoc(),  explosion.getYLoc(), (int) explosion.getRadius(), (int) explosion.getRadius()).intersects( new Rectangle(firmAsteroids[i].getXLoc(),firmAsteroids[i].getYLoc(),firmAsteroids[i].getRadius(),firmAsteroids[i].getRadius()))) {
				firmAsteroids[i] = null;
			}
		}
		
		int simpleAsteroidLength = simpleAsteroids.length;
		for (int i = 0; i < simpleAsteroidLength ; i++) {
			if (simpleAsteroids[i] != null  &&  new Rectangle(explosion.getXLoc(),  explosion.getYLoc(), (int) explosion.getRadius(), (int) explosion.getRadius()).intersects( new Rectangle(simpleAsteroids[i].getXLoc(),simpleAsteroids[i].getYLoc(),simpleAsteroids[i].getLength(),simpleAsteroids[i].getWidth()))) {
				simpleAsteroids[i] = null;
			}
		}
		
    }
    
    public void makeExplosionNull() {
    	this.explosion = null;
    }

    
    /*
     * Getter And Setter
     */
    
    public SimpleAsteroid[] getSimpleAsteroids() {
    	return this.simpleAsteroids;
    }
    public FirmAsteroid[] getFirmAsteroids() {
    	return this.firmAsteroids;
    }

	public ExplosiveAsteroid[] getExplosiveAsteroids() {
    	return this.explosiveAsteroids;
    }
	
	public GiftAsteroid[] getGiftAsteroids() {
    	return this.giftAsteroids;
    }
	
	public Explosion getExplosion() {
    	return explosion;
    }

	public int getSimpleAsteroidNumber() {
		return simpleAsteroidNumber;
	}

	public int getFirmAsteroidNumber() {
		return firmAsteroidNumber;
	}

	public int getExplosiveAsteroidNumber() {
		return explosiveAsteroidNumber;
	}

	public int getGiftAsteroidNumber() {
		return giftAsteroidNumber;
	}

	public void setSimpleAsteroidNumber(int simpleAsteroidNumber) {
		this.simpleAsteroidNumber = simpleAsteroidNumber;
	}

	public void setFirmAsteroidNumber(int firmAsteroidNumber) {
		this.firmAsteroidNumber = firmAsteroidNumber;
	}

	public void setExplosiveAsteroidNumber(int explosiveAsteroidNumber) {
		this.explosiveAsteroidNumber = explosiveAsteroidNumber;
	}

	public void setGiftAsteroidNumber(int giftAsteroidNumber) {
		this.giftAsteroidNumber = giftAsteroidNumber;
	}

	public Paddle getPaddle() {
		return this.paddle;
	}
	
	public Asteroid[] getAsteroids() {
		return this.asteroids;
	}
	
	public ProtectingAlien getProtectingAlien() {
		return this.protectingAlien;
	}
}
