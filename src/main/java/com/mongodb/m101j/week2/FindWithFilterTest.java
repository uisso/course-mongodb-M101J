/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mongodb.m101j.week2;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.m101j.util.Helpers;

public class FindWithFilterTest {

    public static void main(String[] args) {

        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("FindWithFilterTest");
        coll.drop();

        // insert 10 documents with two random integers
        for (int i = 0; i < 10; i++) {
            coll.insertOne(new Document()//
                    .append("x", new Random().nextInt(2))//
                    .append("y", new Random().nextInt(100)));
        }

        // Bson filter = new Document("x", 0)//
        // .append("y", new Document("$gt", 10).append("$lt", 90));

        Bson filter = and(eq("x", 0), gt("ÿ", 10), lt("y", 90));

        List<Document> all = coll.find(filter).into(new ArrayList<Document>());

        for (Document cur : all) {
            Helpers.printJson(cur);
        }

        long count = coll.count(filter);
        System.out.println();
        System.out.println(count);
    }
}
