package Database;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import Domain.Asteroid.Asteroid;
import Domain.Ball.Ball;
import Domain.Paddle.Paddle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class DocumentManager {

	private MongoDatabase database = null;
	
	public String registerLoginData(String username, String password) {

		MongoCollection collection = database.getCollection("LoginData");
		
		FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.iterator();
        
        while(cursor.hasNext()) {
        	Document currentUser = cursor.next();
        	if(currentUser.get("Username").equals(username)) {
        		return "A user with this username already exists";
        	}
        }
        
		Document loginData = new Document("Username", username)
				.append("Password", password);
		collection.insertOne(loginData);
		
		return "Registered successfully";
	}
	
	public ObjectId userLogin(String username, String password) {
		MongoCollection collection = database.getCollection("LoginData");
		
		FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.iterator();
        
        while(cursor.hasNext()) {
        	Document currentUser = cursor.next();
        	if(currentUser.get("Username").equals(username)) {
        		if(currentUser.get("Password").equals(password)) {
        			return (ObjectId) currentUser.get("_id");
        		}
        	}
        }
        return null;
	}
	
	public ObjectId addSavegame(Paddle paddle, Ball ball, ObjectId userId) {
		MongoCollection collection = database.getCollection("Savegame");
		Document saveGameDoc = new Document("PaddleLocation", paddle.getLocation())
				.append("BallXLocation", ball.getXLocation())
				.append("BallYLocation", ball.getYLocation())
				.append("UserId", userId);
		collection.insertOne(saveGameDoc);
		
		return (ObjectId)saveGameDoc.get( "_id" );
	}
	
	public void updateSavegame(Paddle paddle, Ball ball, ObjectId saveGameId) {
		MongoCollection collection = database.getCollection("Savegame");
		collection.deleteOne(Filters.eq("_id", saveGameId));
		collection.insertOne(new Document("_id", saveGameId)
				.append("PaddleLocation", paddle.getLocation())
				.append("BallXLocation", ball.getXLocation())
				.append("BallYLocation", ball.getYLocation())
		);
				
	}
	
	public void addAsteroids(Asteroid[] asteroids, int firmIndex, int explosiveIndex, int giftIndex, ObjectId saveGameId) {
		MongoCollection collection = database.getCollection("Asteroids");
		List documents = new ArrayList();
		for(int i = 0; i < asteroids.length; i++) {
			if(asteroids[i] != null) {
				if(i < firmIndex) {
					documents.add(new Document("xLocation", asteroids[i].getXLoc())
							.append("yLocation", asteroids[i].getYLoc())
							.append("AsteroidType", "Simple")
							.append("Savegame", saveGameId)
							.append("AsteroidIndex", i)
						);
				} else if (i >= firmIndex && i < explosiveIndex) {
					documents.add(new Document("xLocation", asteroids[i].getXLoc())
							.append("yLocation", asteroids[i].getYLoc())
							.append("AsteroidType", "Firm")
							.append("Savegame", saveGameId)
							.append("AsteroidIndex", i)
						);
				} else if (i >= explosiveIndex && i < giftIndex) {
					documents.add(new Document("xLocation", asteroids[i].getXLoc())
							.append("yLocation", asteroids[i].getYLoc())
							.append("AsteroidType", "Explosive")
							.append("Savegame", saveGameId)
							.append("AsteroidIndex", i)
						);
				} else {
					documents.add(new Document("xLocation", asteroids[i].getXLoc())
							.append("yLocation", asteroids[i].getYLoc())
							.append("AsteroidType", "Gift")
							.append("Savegame", saveGameId)
							.append("AsteroidIndex", i)
						);
				}
			}
			
		}
		
		collection.insertMany(documents);
	}
	
	public void updateAsteroids(Asteroid[] asteroids, ObjectId saveGameId) {
		MongoCollection collection = database.getCollection("Asteroids");
		
		for(int i = 0; i < asteroids.length; i++) {
			if(asteroids[i] == null) {
				collection.deleteOne(Filters.and(Filters.eq("AsteroidIndex", i), (Filters.eq("Savegame", saveGameId))));
			} else {
				Document newAsteroidParameter = new Document("xLocation", asteroids[i].getXLoc())
						.append("yLocation", asteroids[i].getYLoc());
				
				Document newAsteroid = new Document();
				newAsteroid.append("$set", newAsteroidParameter);
				
				Document oldAsteroid = new Document("AsteroidIndex", i)
						.append("Savegame", saveGameId);
				
				
			
				collection.findOneAndUpdate(oldAsteroid, newAsteroid);
			}
		}
	}
	
	
	public void setDatabase(MongoDatabase database) {
		this.database = database;
	} 

}
