package UI;

import javax.swing.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import Database.DocumentManager;
import Database.MongoJava;
import Domain.Asteroid.Asteroid;
import Domain.Asteroid.ExplosiveAsteroid.ExplosiveAsteroid;
import Domain.Asteroid.FirmAsteroid.FirmAsteroid;
import Domain.Asteroid.GiftAsteroid.GiftAsteroid;
import Domain.Asteroid.SimpleAsteroid.SimpleAsteroid;
import Domain.Ball.Ball;
import Domain.Paddle.Paddle;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import GameManager.GameManager;
import static com.mongodb.client.model.Filters.eq;

public class AsteroidCountPanel extends JPanel implements ActionListener{

    private JLabel simpleLabel;
    private JLabel firmLabel;
    private JLabel explosiveLabel;
    private JLabel giftLabel;
    private JTextField simpleTextField;
    private JTextField firmTextField;
    private JTextField explosiveTextField;
    private JTextField giftTextField;
    private JButton submitButton;
    private JLabel emptyLabel;
    private JButton loadButton;
    private JComboBox saveGameIdField;
    
    private MongoDatabase database = MongoJava.getDatabase();
    private MongoCursor cursor;
    
    private List<SimpleAsteroid> simpleAsteroidList;
    private List<FirmAsteroid> firmAsteroidList;
    private List<ExplosiveAsteroid> explosiveAsteroidList;
    private List<GiftAsteroid> giftAsteroidList;
    
    private ObjectId userId;

    public AsteroidCountPanel(ObjectId userId) {
    	
    	this.userId = userId;
    	
    	MongoCollection coll1 = database.getCollection("Savegame");
    	MongoCollection coll2 = database.getCollection("Asteroids");
    	
        GridLayout gl = new GridLayout(6,2,20,20);
        this.setLayout(gl);

        simpleLabel = new JLabel("Simple Asteroids: ", SwingConstants.CENTER);
        firmLabel = new JLabel("Firm Asteroids: ",SwingConstants.CENTER);
        explosiveLabel = new JLabel("Explosive Asteroids: ",SwingConstants.CENTER);
        giftLabel = new JLabel("Gift Asteroids: ",SwingConstants.CENTER);
        simpleTextField = new JTextField();
        firmTextField = new JTextField();
        explosiveTextField = new JTextField();
        giftTextField = new JTextField();
        submitButton = new JButton("Submit");
        emptyLabel = new JLabel();
        loadButton = new JButton("Load");
        saveGameIdField = new JComboBox();
        
        FindIterable<Document> iterable = coll1.find();

        MongoCursor<Document> cursor = iterable.iterator();
        
        while(cursor.hasNext()) {
        	Document o = cursor.next();
        	if(o.get("UserId").equals(userId)) {
        		saveGameIdField.addItem(o.get("_id"));
        	}
        }

        add(simpleLabel);
        add(simpleTextField);
        add(firmLabel);
        add(firmTextField);
        add(explosiveLabel);
        add(explosiveTextField);
        add(giftLabel);
        add(giftTextField);
        add(submitButton);
        add(emptyLabel);
        add(loadButton);
        add(saveGameIdField);

        submitButton.addActionListener(this);
        loadButton.addActionListener(this);
    }

    public void setUserId(ObjectId e) {
    	this.userId = e;
    }
    
    public ObjectId getUserId() {
    	return this.userId;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

    	if(e.getSource() == submitButton) {
    		
    		int simpleAmount = Integer.parseInt(simpleTextField.getText());
            int firmAmount = Integer.parseInt(firmTextField.getText());
            int explosiveAmount = Integer.parseInt(explosiveTextField.getText());
            int giftAmount = Integer.parseInt(giftTextField.getText());
            
            SwingUtilities.getWindowAncestor(this).dispose();

            Ball ball = new Ball();
            Paddle paddle = new Paddle();
            GameManager gameManager = new GameManager(ball,paddle,simpleAmount,firmAmount,explosiveAmount,giftAmount,userId);
    		GameWindow gameWindow = new GameWindow(gameManager,ball,paddle);
    		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		gameWindow.setFocusable(false);
    		gameWindow.setResizable(false);
    		gameWindow.setSize(1820, 1040);

            gameWindow.setVisible(true);
            
    	} else if(e.getSource() == loadButton) {
    		
    		this.simpleAsteroidList = new ArrayList<SimpleAsteroid>();
    	    this.firmAsteroidList = new ArrayList<FirmAsteroid>();
    	    this.explosiveAsteroidList = new ArrayList<ExplosiveAsteroid>();
    	    this.giftAsteroidList = new ArrayList<GiftAsteroid>();
    		
    		MongoCollection coll1 = database.getCollection("Savegame");
        	MongoCollection coll2 = database.getCollection("Asteroids");
        	
    		Object chosenSavegameId = saveGameIdField.getSelectedItem();
    		
            FindIterable<Document> iterable2 = coll1.find();
            MongoCursor<Document> cursor2 = iterable2.iterator();
            
            Document chosenSavegame = cursor2.next();
            while(cursor2.hasNext() && !chosenSavegame.get("_id").equals(chosenSavegameId)) {
            	chosenSavegame = cursor2.next();
            }
            
            FindIterable<Document> iterable3 = coll2.find();
            MongoCursor<Document> cursor3 = iterable3.iterator();
                        
            while(cursor3.hasNext()) {
            	Document currentAsteroid = cursor3.next();
            	if(currentAsteroid.get("AsteroidType").equals("Simple") && currentAsteroid.get("Savegame").equals(chosenSavegameId)) {
            		simpleAsteroidList.add(new SimpleAsteroid((int) currentAsteroid.get("xLocation"), (int) currentAsteroid.get("yLocation"), false, null));
            	} else if (currentAsteroid.get("AsteroidType").equals("Firm") && currentAsteroid.get("Savegame").equals(chosenSavegameId)) {
            		firmAsteroidList.add(new FirmAsteroid((int) currentAsteroid.get("xLocation"), (int) currentAsteroid.get("yLocation"), false, 35, null));
            	} else if (currentAsteroid.get("AsteroidType").equals("Explosive") && currentAsteroid.get("Savegame").equals(chosenSavegameId)) {
            		explosiveAsteroidList.add(new ExplosiveAsteroid((int) currentAsteroid.get("xLocation"), (int) currentAsteroid.get("yLocation"), false, (int) currentAsteroid.get("xLocation")+30, (int) currentAsteroid.get("yLocation")+30,null));
            	} else if (currentAsteroid.get("AsteroidType").equals("Gift") && currentAsteroid.get("Savegame").equals(chosenSavegameId)) {
            		giftAsteroidList.add(new GiftAsteroid((int) currentAsteroid.get("xLocation"), (int) currentAsteroid.get("yLocation"), false, null));
            	}
            }

    		Ball ball = new Ball();
    		Paddle paddle = new Paddle();
    		ball.setXLocation((int) chosenSavegame.get("BallXLocation"));
    		ball.setYLocation((int) chosenSavegame.get("BallYLocation"));
    		paddle.setLocation((int) chosenSavegame.get("PaddleLocation"));
    		
    		
    		
    		SwingUtilities.getWindowAncestor(this).dispose();
    
    		GameManager gameManager = new GameManager(ball,paddle,simpleAsteroidList,firmAsteroidList,explosiveAsteroidList,giftAsteroidList,userId);
    		GameWindow gameWindow = new GameWindow(gameManager,ball,paddle);
    		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		gameWindow.setFocusable(false);
    		gameWindow.setResizable(false);
    		gameWindow.setSize(1820, 1040);
    		
    		gameWindow.setVisible(true);
    	}
        

    }

}
