/* Allkea Solutions
 * Copyright (c) 2015, allkea.com
 */
package com.mongodb.m101j.week2.hw2_3;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

/**
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class Homework2_3 {

    public static void main(String... args) {

        try (MongoClient client = new MongoClient()) {

            MongoDatabase db = client.getDatabase("students");
            MongoCollection<Document> coll = db.getCollection("grades");

            Bson sort = Sorts.ascending("student_id", "score");
            Bson filter = Filters.eq("type", "homework");

            List<Document> all = coll.find(filter).sort(sort).into(new ArrayList<Document>());

            List<ObjectId> toRemoveIds = new ArrayList<ObjectId>();
            for (int i = 0; i < all.size(); i++) {
                if (i % 2 == 0) {
                    Document doc = all.get(i);
                    toRemoveIds.add(doc.getObjectId("_id"));
                }
            }

            if (toRemoveIds.size() != 200) {
                throw new RuntimeException("Number of documents to remove is not correct");
            }

            Bson filterToRemove = Filters.in("_id", toRemoveIds);
            long deleteCount = coll.deleteMany(filterToRemove).getDeletedCount();

            System.out.println(String.format("\nGrades has deleted: %d\n", deleteCount));

            System.out.println(String.format("Count after deleted documents: %d", coll.count()));

        }
    }

}
