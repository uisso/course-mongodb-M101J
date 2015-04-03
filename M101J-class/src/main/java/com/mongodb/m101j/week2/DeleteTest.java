/* Mongodb course - M101J
 * Copyright (c) 2015
 */
package com.mongodb.m101j.week2;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

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
public class DeleteTest {

    public final static void main(String... args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("DeleteTest");
        coll.drop();

        // insert 8 documents, with both _id set to the value of the loop
        // variable
        for (int i = 0; i < 8; i++) {
            coll.insertOne(new Document().append("_id", i));
        }

        coll.deleteMany(gt("_id", 4));

        coll.deleteOne(eq("_id", 5));

        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document cur : all) {
            Helpers.printJson(cur);
        }

    }
}
