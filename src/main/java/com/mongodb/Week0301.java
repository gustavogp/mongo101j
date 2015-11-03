package com.mongodb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class Week0301 {

	public static void main(String[] args) {
		sortDelete2();
	}
	
	public static void sortDelete2(){
		MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("school");
        MongoCollection<Document> coll = db.getCollection("students");
        
        System.out.println(coll.count());
        
        MongoCursor<Document> studentsCursor = coll.find().iterator();
        try {
        	while (studentsCursor.hasNext()){
        		Document student = studentsCursor.next();
        		List<Document> scores = (List<Document>) student.get("scores");
        		List<Document> newScores = new ArrayList<Document>();
        		List<Document> hwScores = new ArrayList<Document>();
        		Iterator<Document> scoresIte = scores.iterator();
        		//add homeworks to a separate List
        		while(scoresIte.hasNext()) {
        			Document scoreDoc = scoresIte.next();
        			if (scoreDoc.get("type").toString().equalsIgnoreCase("homework")) {
        				hwScores.add(scoreDoc);
        			} else {
        				newScores.add(scoreDoc);
        			}
        		}
        		
        		System.out.println("hwScores: " + hwScores.size());
        		System.out.println("newScores temp: " + newScores.size());
        		
        		//find the homework with highest grade and add it to newScores
        		double tgs = -1;
        		Document thisGoes = null;
        		for(Document hwS : hwScores) {
        			double test = hwS.getDouble("score");
        			if(test > tgs){
        				tgs = test;
        				thisGoes = hwS;
        			}
        		}
        		newScores.add(thisGoes);
        		
        		System.out.println("newScores final: " + newScores.size());
        		
        		//update the scores of this student
        		coll.updateOne(eq("_id", student.get("_id")), 
        				new Document("$set", new Document("scores", newScores)));
        		
        		System.out.println(student.get("scores"));
        		
        	}
        } finally {
        	studentsCursor.close();
        }
        
	}

}
