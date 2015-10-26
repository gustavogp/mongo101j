package com.mongodb;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App 
{
    public static void main( String[] args )
    {
    	MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
        MongoClient client = new MongoClient(new ServerAddress(), options );
        
        MongoDatabase db = client.getDatabase("m101");
        
        MongoCollection<Document> coll = db.getCollection("hw1");
        
        coll.find();
        
        client.close();
    }
}
