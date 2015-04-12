Week 4: Performance
===================

# Topics:
Using Indexes, Monitoring And Understanding Performance. Performance In Sharded Environments

## Storage Engines: Introduction
[Lecture Video](https://www.youtube.com/watch?v=YvK7I9fYpK4)

## Storage Engines: MMAPv1 
[Lecture Video](https://www.youtube.com/watch?v=os3591KviNM)
 
## Storage Engines: WiredTiger 
[Lecture Video](https://www.youtube.com/watch?v=aNsugW7r3mM)
 
## Indexes 
[Lecture Video](https://www.youtube.com/watch?v=U3iWPF5jP-g)
 
## Creating Indexes 
[Lecture Video](https://www.youtube.com/watch?v=xi2gtzZez6Q)

* db.[collection].ensureIndex({student_id:1})
* 1=ascending, -1=descending // important for sorting not so much for searching
* Sorting can also use a reverse index if the sort criteria are exactly the reverse of an (simple or compound) index
* db.[collection].ensureIndex({student_id:1,class:-1})
* General rule: A Query where one term demands an exact match and another specifies a range requires a compound index where the range key comes second
 
## Discovering (and Deleting) Indexes 
[Lecture Video](https://www.youtube.com/watch?v=dX49IcmTrGA)

* We want indexes to be in memory. Find out the index size: db.[col].stats() or db.[col].totalIndexSize()
* db.system.indexes.find() // finds all indexes of the current db
* db.[collection].getIndexes() // all indexes of collection
* db.[collection].dropIndex({student_id:1})
 
## Multikey Indexes 
[Lecture Video](https://www.youtube.com/watch?v=_NGwn_X82Dw)
 
## Dot Notation and Multikey 
[Lecture Video](https://www.youtube.com/watch?v=wT0_ktAZbBg)

* A multi key index is an index on an array field of a document, e.g. a student document has array of teacher-ids. One can add a multi key index on the teachers-array, which indexes all of the values in the array for all the documents.
* Multi key indices are one of the reason that linking works so well in MongoDB
* It is not possible to have a compound index with two array (multi key) fields
 
## Index Creation Option, Unique 
[Lecture Video](https://www.youtube.com/watch?v=D-Ra5TEaaL4)

* db.[collection].ensureIndex({student_id:1}, {unique: true}) // dropDups: true -> dangerous
 
## Index Creation, Sparse 
[Lecture Video](https://www.youtube.com/watch?v=ZznHByqtTMA)

* Missing index key in documents map to null // unique key not possible because multiple nulls are not allowed
* Sparse indexes only index documents that have a key set for the key being indexed {unique: true, sparse: true}
* On a sorted find the non-indexed documents will not be found when the sparse index is used for the sort
 
## Index Creation, Background 
[Lecture Video](https://www.youtube.com/watch?v=AchmKNj2qhw)

* By default index creation is done in the foreground which is fast but blocking all other writers to the same DB. Background index creation {background: true} will be slow but it will not block the writers
 
## Using Explain 
[Lecture Video](https://www.youtube.com/watch?v=r5YeICVzDjQ)

* db.[collection].find({…}).explain()
 
## Explain: Verbosity 
[Lecture Video](https://www.youtube.com/watch?v=WxXVun6bZ20)

* db.[collection].find({…}).explain(true) //shows all possible plans
* db.[collection].find({…}).hint({a:1, b:1}) // use specified index
* db.[collection].find({…}).hint({$natural:1}) // use no index
* In Java:
	.find(query).hint(“IndexName”) OR
	.find(query).hint(new Document(a, 1).append(b, 1))
 
## Covered Queries 
[Lecture Video](https://www.youtube.com/watch?v=QyV79jsSJ9Y)
 
## When is an Index Used? 
[Lecture Video](https://www.youtube.com/watch?v=JyQlxDc549c)

* $gt, $lt, $ne, $nin, $not($exists) might be inefficient even if an index is used because still many index items (indexed documents) need to be scanned - may be a good idea to use a hint to use a diff. index
* $regex can only use an index if it is stemmed on the left side, e.g. /^abc/
 
## How Large is Your Index? 
[Lecture Video](https://www.youtube.com/watch?v=wjA0eo_lihg)
 
## Number of Index Entries 
[Lecture Video](https://www.youtube.com/watch?v=xiujksUfzUA)
 
## Geospatial Indexes 
[Lecture Video](https://www.youtube.com/watch?v=UKUDYqNVL6I)

* db.[collection].ensureIndex({location: ‘2d’, type: 1}) // Compound index on location (uses 2d-index) and ascending type
 
## Geospatial Spherical 
[Lecture Video](https://www.youtube.com/watch?v=pULU4DVsUWQ)
 
## Text Indexes 
[Lecture Video](https://www.youtube.com/watch?v=nLau5Fx9LC8)
 
## Efficiency of Index Use 
[Lecture Video](https://www.youtube.com/watch?v=JJmIf0pn100)
 
## Efficiency of Index Use Example 
[Lecture Video](https://www.youtube.com/watch?v=g032EW67SRA)

* At about 3:13, Shannon mentions that MongoDB can walk the index backward in order to sort on the final_grade field. While true given that we are sorting on only this field, if we want to sort on multiple fields, the direction of each field on which we want to sort in a query must be the same as the direction of each field specified in the index. So if we want to sort using something like db.collection.find( { a: 75 } ).sort( { a: 1, b: -1 } ), we must specify the index using the same directions, e.g., db.collection.createIndex( { a: 1, b: -1 } ).
 
## Logging Slow Queries 
[Lecture Video](https://www.youtube.com/watch?v=aWuvC-O7Qkk)
 
## Profiling 
[Lecture Video](https://www.youtube.com/watch?t=12&v=pN1Yhrup9-I)

* MongoDB logs slow queries (>100ms) by default into the logfile
* Use pofiler:
* mongod --profile 1 --slowms 10 // logs all queries taking longer than 10ms to system.profile collection
* Levels: 0=off (default) 1=log slow queries 2=log all queries (general debugging feature for dev.)
* Mongo shell: db.getProfilingLevel() db.getProfilingStatus() db.setProfilingLevel(level, slowms)
* mongod --notablescan option: Set notablescan = true on your dev or test machine to find operations that require a table scan

You can change de configuration in mongod.conf
 
## Mongostat 
[Lecture Video](https://www.youtube.com/watch?v=E2aDTSes3Wc)

* mongostat // shows inserts, queries, updates, deletes, … per second
*  idx miss % = index which could not be accessed in memory
 
## Mongotop 
[Lecture Video](https://www.youtube.com/watch?v=D9YLXgy7NYo)

* mongotop 3 // runs every 3 seconds showing you in which collection how much time (read, write, total) is spent
 
## Sharding Overview
[Lecture Video](https://www.youtube.com/watch?v=BDxT-VZdYqc)
 