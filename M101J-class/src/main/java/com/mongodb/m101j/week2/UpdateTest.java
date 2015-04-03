/* Mongodb course - M101J
 * Copyright (c) 2015
 */
package com.mongodb.m101j.week2;

import static com.mongodb.client.model.Filters.gte;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.m101j.util.Helpers;

/**
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class UpdateTest {

    public final static void main(String... args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("UpdateTest");
        coll.drop();

        // insert 8 documents, with both _id and x set to the value of the loop
        // variable
        for (int i = 0; i < 8; i++) {
            coll.insertOne(new Document()//
                    .append("_id", i)//
                    .append("x", i));//
        }

        // coll.replaceOne(eq("x", 5), new Document("_id", 5).append("x",
        // 20).append("update", true));

        // coll.updateOne(eq("_id", 9), new Document("$set", new Document("x",
        // 20)), new UpdateOptions().upsert(true));

        coll.updateMany(gte("_id", 5), new Document("$inc", new Document("x", 1)));

        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document cur : all) {
            Helpers.printJson(cur);
        }

    }
}
