package com.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import static com.mongodb.client.model.Filters.*;

public class App 
{
    public static void main( String[] args )
    {
    //	lessonOne();
    //	lessonTwo();
    	lessonThree();
    }
    
    private static void lessonOne() 
    {
    	MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
        MongoClient client = new MongoClient(new ServerAddress(), options );
        
        MongoDatabase db = client.getDatabase("m101");
        
        MongoCollection<Document> coll = db.getCollection("hw1");
        
        System.out.print(coll.find().first());
        
        client.close();
    }
    private static void lessonTwo() 
    {
    	Document document = new Document().append("str", "Hello Mongo");
    	
    	String str = document.getString("str");
    	
    	System.out.println(str);
    	
    	Document document2 = new Document("_id", "user1").append("interests", Arrays.asList("baseball", "auto racing"));
    	
    	System.out.println(document2.get("interests"));
    //	printJson();
    }
    private static void lessonThree()
    {
    	MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("things");
        
        coll.drop();
        
        Document gus = new Document().append("name", "Gustavo").append("age", 42);
        
        Document cam = new Document().append("name", "Camila").append("age", 40);
        
        coll.insertMany(Arrays.asList(gus, cam));
        
        //retrieving documents
        Document first = coll.find().first();
        System.out.println(first);
        
        List<Document> many = coll.find().into(new ArrayList<Document>());
        for (Document cur : many) {
        	System.out.println(cur);
        }
        
        MongoCursor<Document> cursor = coll.find().iterator();
        try {
        	while (cursor.hasNext()){
        		Document cur = cursor.next();
        		System.out.println(cur);
        	}
        } finally {
        	cursor.close();
        }
        
        long count = coll.count();
        System.out.println(count);
        
        //using filter
        //Bson filter = new Document("name", "Gustavo").append("age", new Document ("$gt", 39));
        Bson filter = and(eq("name", "Gustavo"), gt("age", 39));
        	//can use filter on find() and on count()
    	System.out.println("found " + coll.count(filter) + " Gustavos");
    	
    	//update docs
        coll.updateOne(eq("name", "Gustavo"), new Document("$set", new Document ("age", 41)), 
        		new UpdateOptions().upsert(true));
    	
    	
    }
    
    
}
