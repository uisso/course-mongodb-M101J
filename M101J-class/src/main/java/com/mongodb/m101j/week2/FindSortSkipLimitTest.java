package com.mongodb.m101j.week2;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.m101j.util.Helpers;

/**
 * 
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class FindSortSkipLimitTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("FindSortSkipLimitTest");
        coll.drop();

        // insert 100 documents with two random integers
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                coll.insertOne(new Document("i", i).append("j", j));
            }
        }

        Bson projection = fields(include("i", "j"), excludeId());

        // Bson sort = new Document("i", 1).append("j", -1);
        Bson sort = orderBy(ascending("i"), descending("j"));

        List<Document> all = coll.find()//
                .projection(projection)//
                .sort(sort)//
                .skip(20)//
                .limit(50)//
                .into(new ArrayList<Document>());

        for (Document cur : all) {
            Helpers.printJson(cur);
        }
    }
}
