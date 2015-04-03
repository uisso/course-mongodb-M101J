package com.mongodb.m101j.week2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.m101j.util.Helpers;

public class FindTest {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("findTest");

        // insert 10 documents with a random integer as the value of field "x"
        for (int i = 0; i < 10; i++) {
            coll.insertOne(new Document("x", i));
        }

        System.out.println("Find one:");
        Document first = coll.find().first();
        Helpers.printJson(first);

        System.out.println("\nFind all with into: ");
        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document cur : all) {
            Helpers.printJson(cur);
        }

        System.out.println("\nFind all with iteration: ");
        MongoCursor<Document> cursor = coll.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document cur = (Document) cursor.next();
                Helpers.printJson(cur);
            }
        } finally {
            cursor.close();
        }

        System.out.println("\nCount:");
        long count = coll.count();
        System.out.println(count);
    }
}
