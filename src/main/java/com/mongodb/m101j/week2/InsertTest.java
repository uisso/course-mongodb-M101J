package com.mongodb.m101j.week2;

import java.net.UnknownHostException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class InsertTest {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("school");
        MongoCollection<Document> coll = db.getCollection("people");

        Document doc = new Document("name", "Andrew Erlichson")//
                .append("company", "10gen");

        coll.insertOne(doc); // first insert
        doc.remove("_id"); // remove the _id key
        coll.insertOne(doc); // second insert

    }
}
