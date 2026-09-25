package com.napier.DevOps_Lab;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // "localhost", 27000 အစား Network ထဲမှ Container Name "mongo-dbserver", 27017 သို့ ပြောင်းခြင်း
        MongoClient mongoClient = new MongoClient("mongo-dbserver", 27017);
        MongoDatabase database = mongoClient.getDatabase("mydb");
        MongoCollection<Document> collection = database.getCollection("test");

        Document doc = new Document("name", "Hla Min Phyo")
                .append("class", "DevOps")
                .append("year", "2026")
                .append("result", new Document("CW", 95).append("EX", 85));
        collection.insertOne(doc);

        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
    }
}

