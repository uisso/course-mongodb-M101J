/* Mongodb course - M101J
 * Copyright (c) 2015
 */
package com.mongodb.m101j.week3.hw3_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * TODO Add element description, main objective and relations
 *
 * @author wilson
 *
 */
public class Homework3_1 {

    public static void main(String... args) {

        try (MongoClient client = new MongoClient()) {

            MongoDatabase db = client.getDatabase("school");
            MongoCollection<Document> coll = db.getCollection("students");

            // XXX here nice query from mongo's console :]
            //@formatter:off
            /*
            db.students.aggregate(
                    { '$unwind': '$scores'},
                    { '$group': { _id : {'id':'$_id', 'name': '$name', 'type': '$scores.type', 'score': '$scores.score'} } },
                    { '$match': { '_id.type': 'homework'}},
                    { '$sort': { '_id.id':1, '_id.score':1}}
            ).pretty()
             */
            //@formatter:on

            // XXX here another nice query, more simple without grouping!
            //@formatter:off
            /*
            db.students.aggregate(
                    {$unwind:'$scores'},
                    {$match:{'scores.type':'homework'}},
                    {$sort:{'_id':1, 'scores.score':-1}}
            ).pretty()
             */
            //@formatter:on

            Bson unwind = new Document("$unwind", "$scores");
            Bson match = new Document("$match", new Document("scores.type", "homework"));
            Bson sort = new Document("$sort", new Document().append("_id", 1).append("scores.score", 1));

            List<Bson> pipeline = Arrays.asList(unwind, match, sort);

            List<Document> all = coll.aggregate(pipeline).into(new ArrayList<Document>());

            List<Document> toUpdateDocs = new ArrayList<Document>();
            for (int i = 0; i < all.size(); i++) {
                if (i % 2 == 0) {
                    Document doc = all.get(i);
                    System.out.println(doc);
                    toUpdateDocs.add(doc);
                }
            }

            if (toUpdateDocs.size() != 200) {
                throw new RuntimeException("Number of documents to remove is not correct");
            }

            for (Document doc : toUpdateDocs) {
                Bson filter = Filters.eq("_id", doc.get("_id"));
                Bson score = (Bson) doc.get("scores");
                Bson update = new Document("$pull", new Document("scores", score));
                coll.updateOne(filter, update);
            }
            System.out.println(String.format("Count after deleted documents: %d", coll.count()));

            Document check = coll.find(Filters.eq("_id", 137)).first();
            if (check == null) {
                throw new RuntimeException("No mathc document 137 from collection");
            }

            List<Document> scores = (List<Document>) check.get("scores");
            if (scores.size() != 3) {
                throw new RuntimeException("you are not on the right track!");
            }

            Document score = (Document) scores.get(2);
            if (!score.getDouble("score").equals(89.5950384993947D)) {
                throw new RuntimeException("you are not on the right track!");
            }

            // TODO get your answers at mongodb's console
            //@formatter:off
            /*
             db.students.aggregate( 
                 { '$unwind' : '$scores' } , 
                 { '$group' : { '_id' : '$_id' , 'average' : { $avg : '$scores.score' } } } , 
                 { '$sort' : { 'average' : -1 } } , 
                 { '$limit' : 1 } 
             )
             */


        }
    }

}
