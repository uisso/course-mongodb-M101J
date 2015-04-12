Week 2: CRUD
============

# Topics:
Mongo Shell, Query Operators, Update Operators and a Few Commands

## Crud and the Mongo Shell
[Lecture Video](https://www.youtube.com/watch?v=C7LinMC2o5o)

In MongoDB, the CRUD operations are:

* Create: `insert`
* Read: `find`
* Update: `update`
* Delete: `remove`

MongoDB's CRUD operations exist as methods/functions in programming language APIs, not as a separate language. You should manipulate documents in the database using objects in your programming language.

## Secrets of the Mongo Shell
[Lecture Video](https://www.youtube.com/watch?v=IIIzjPp-IRE)

Mongo shell is an interactive Javascript interpreter that allows you to connect to MongoDB and manipulate its data. This means you can execute Javascript inside the shell:

```js
> for (i = 0; i < 3; i++) print("Hello!");
Hello!
Hello!
Hello!
```
It also has tab completions for Javascript and other commands. For example, typing `pri` and pressing the Tab key, it will complete the word `print`, a common Javascript function.

## BSON Introduced
[Lecture Video](https://www.youtube.com/watch?v=K3J6WvDW-Hc)

MongoDB uses BSON to represent data in the database. The specification for BSON is give at [bsonspec.org](http://bsonspec.org).

## Mongo shell, inserting docs
[Lecture Video](https://www.youtube.com/watch?v=qqfVxGLIrLg)

```bash
> doc = { "name" : "Smith", "age" : 30, "profession" : "hacker" }
{ "name" : "Smith", "age" : 30, "profession" : "hacker" }
> db
test
> db.people.insert(doc)
> db.people.find()
{ "_id" : ObjectId("525db09800fa8276d3d083d4"), "name" : "Smith", "age" : 30, "profession" : "hacker" }
> db.people.insert({ "name" : "Jones", "age" : 35, "profession" : "baker" })
> db.people.find()
{ "_id" : ObjectId("525db09800fa8276d3d083d4"), "name" : "Smith", "age" : 30, "profession" : "hacker" }
{ "_id" : ObjectId("525db17400fa8276d3d083d5"), "name" : "Jones", "age" : 35, "profession" : "baker" }
```

* db.[collection].insert({...})
* The collection unique ID field is called “_id” and can be provided. If not provided an ObjectID will be
* generated based on the time, machine, process-id and process dependent counter.
* “_id” does not have to be a scalar value – it can be a document, e.g. _id : {a:1, b:’ronald’}

## Mongo shell, introduction to findOne
[Lecture Video](https://www.youtube.com/watch?v=w9V0fJsDwbQ)

```bash
> db.people.findOne()
{
  "_id" : ObjectId("525db09800fa8276d3d083d4"),
  "name" : "Smith",
  "age" : 30,
  "profession" : "hacker"
}
> db.people.findOne({ "name" : "Jones" })
{
  "_id" : ObjectId("525db17400fa8276d3d083d5"),
  "name" : "Jones",
  "age" : 35,
  "profession" : "baker"
}
> db.people.findOne({ "name" : "Jones" }, { "name" : true, "_id" : false })
{ "name" : "Jones" }
> db.people.findOne({ "name" : "Jones" }, { "name" : true })
{ "_id" : ObjectId("525db17400fa8276d3d083d5"), "name" : "Jones" }
```

* db.[collection].findOne({...}, {field1 : true, ...}).pretty() //no argument will find all docs

## Mongo shell, introduction to find
[Lecture Video](https://www.youtube.com/watch?v=8kKfFK6a0Ak)

* db.[collection].find({...}, {field1 : true, ...}).pretty() //no argument will find all docs
* db.[collection].count({...})

## Mongo shell, querying using field selection
[Lecture Video](https://www.youtube.com/watch?v=UIg86QjSoyY)

You can use the find method in different ways. See below:

```bash
> db.scores.find( { student: 19 } );
> db.scores.find( { student: 19, type: "essay"} );

// Limit the type returned from the find method
> db.scores.find({ student: 19, type: "essay" }, { score: true, _id: false });
```

## Querying using $gt and $lt
[Lecture Video](https://www.youtube.com/watch?v=FHLrz4VGzkg)

```
// Retrieve documents that have a score greater than 95.
> db.scores.find({ score : { $gt : 95 } });

// Retrieve documents that have a score greater than
// 40 and less or equal to 70, with the type essay
> db.scores.find({ score : { $gt: 40, $lte : 70 }, type: "essay" });
```
* ranges: {myField: {$gt: 100, $lt: 10}} $gte, $lte -> Can be applied to numbers and strings (ASCII)

## Inequalities on strings
[Lecture Video](https://www.youtube.com/watch?v=imCCKOevU3c)
* $not - negates result of other operation or regular expression query
* tags: {$ne: “gardening”} // works on keys pointing to single values or arrays – inefficient – can’t use indexes

## Using regexes, $exists, $type 
[Lecture Video](https://www.youtube.com/watch?v=lI-jhqYf1JY)

* regex: {myField: {$regex: “a$”}}
* {myField: {$exists: true}} //checks if particular key exists in document
* {myField: {$type: 2}} // 2=String as defined in BSON spec

## Using $or 
[Lecture Video](https://www.youtube.com/watch?v=BW5ElNCRZps)

* {$or: [{...}, {...}, ...]}

## Using $and 
[Lecture Video](https://www.youtube.com/watch?v=oIkSajy8NLw)

* {$and: [{...}, {...}, ...]}

## Querying Inside Arrays 
[Lecture Video](https://www.youtube.com/watch?v=jvEqwW75Bus)

* {myArrayField: “test”} -> Will find any documents where the array contains the value “test”
* {myArrayField.0 : “test”} -> Value at particular position within array
* {myArrayField: {$size: 3}} -> array with three elements

## Using $in and $all 
[Lecture Video](https://www.youtube.com/watch?v=QU2NrkviORE)

* operator: {myField: {$in: [“one”, “two”, ...]}} $nin
* array-operator: {myArrayField: {$all: [“one”, “two”, ...]}} -> array contains all given values in any order

## Queries with Dot Notation 
[Lecture Video](https://www.youtube.com/watch?v=NrjFECIfwqk)

* {“myField.mySubfield” : “test”} // Dot-Notation needs to be put in “”
* {“myArrayField.0.mySubfield” : “test”} // stipulate zeroth element of array
* {“myArrayField.mySubfield” : “test”} // search in any of the array elements
* {myArrayField : {$elemMatch: { mySubfield : “test”, mySubfield2 : “test2”}}} // restrict multiple conditions to same subdocument of array field

## Querying, Cursors 
[Lecture Video](https://www.youtube.com/watch?v=3jA6iFSEJOI)

* myCursor = db.[collection].find(); null; // append null as not to print out the cursor immediately
* myCursor.hasNext() myCursor.next()
* myCursor.skip(2).limit(5).sort({name : -1}); null; // modifies the query executed on the server

## Counting Results 
[Lecture Video](https://www.youtube.com/watch?v=eKD5bVmNQMI)

* db.[collection].count({...})

## Wholesale Updating of a Document 
[Lecture Video](https://www.youtube.com/watch?v=g7Fi1xXsuvU)

* db.[collection].update({ myQuery }, {myField: “newValue”, ... }) // replaces the existing document

## Using the $set Command 
[Lecture Video](https://www.youtube.com/watch?v=XyhNjs2pNVc)

* db.[collection].update({ myQuery }, {$set : {myField: “newValue”}}) //> Create or update myField
* db.[collection].update({ myQuery }, {$inc : {age: 1}}) // increment a value for age
* db.[collection].update({ myQuery }, {$set : {“myArray.2”: “x”}}) // Set 3. position of Array

## Using the $unset Command 
[Lecture Video](https://www.youtube.com/watch?v=LpErz8jLW0I)

* db.[collection].update({ myQuery }, {$unset : {myField: 1}})

## Using $push, $pop, $pull, $pushAll, $pullAll, $addToSet 
[Lecture Video](https://www.youtube.com/watch?v=GOn0EWKDQoY)

* db.[collection].update({ myQuery }, {$push : {myArray: “y”}})
* db.[collection].update({ myQuery }, {$pop : {myArray: 1}}) // pop right-most
* db.[collection].update({ myQuery }, {$pop : {myArray: -1}}) // pop left-most
* db.[collection].update({ myQuery }, {$pull : {myArray: “c”}}) // remove value “c”
* db.[collection].update({ myQuery }, {$pushAll : {myArray: [“a”, “b”, “c”]}})
* db.[collection].update({ myQuery }, {$pullAll : {myArray: [“a”, “b”, “c”]}})
* db.[collection].update({ myQuery }, {$addToSet : {myArray: “y”}}) //will only add if does not exist yet

## Upserts 
[Lecture Video](https://www.youtube.com/watch?v=Dy2p8k3EZs4)

* db.[collection].update({ myQuery }, {$set : {myField: “newValue”}}, {upsert: true}) // Create or update document specified by { myQuery } with myField

## Multi-update 
[Lecture Video](https://www.youtube.com/watch?v=2GNNdUmDL-4)

* db.[collection].update({}, {$set: {title: “Dr.”}}, {multi: true}) // {} matches all documents

## Removing Data
[Lecture Video](https://www.youtube.com/watch?v=5K0t1dU8IJY) 

* db.[collection].remove({...}) // no argument will remove all documents from collection – not isolated
* db.[collection].drop() // faster but drops collection including indexes

# Reference:
[Mongodb Operators](http://docs.mongodb.org/manual/reference/operator)