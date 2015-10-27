package com.mongodb;

import java.io.StringWriter;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class HelloApp2 {

	public static void main(String[] args) {
		final Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(HelloApp2.class, "/");
		
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("course");
		final MongoCollection<Document> coll = database.getCollection("hello");
		
		coll.drop();
		
		coll.insertOne(new Document("name", "fala Gus").append("title", "o caba"));
		
		Spark.get(new Route("/") {
			@Override
			public Object handle(Request request, Response response) {
				StringWriter writer = new StringWriter();
				try {
					Template helloTemplate = configuration.getTemplate("hello.ftl");
					
					Document doc = coll.find().first();
					
					helloTemplate.process(doc, writer);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return writer;
			}

		});
		//client.close();

	}
}
