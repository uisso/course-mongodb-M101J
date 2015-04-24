Week 6: Application Engineering
===============================

# Topics:
Drivers, Impact Of Replication And Sharding On Design And Development.


## Write Concern 
[Lecture Video](https://www.youtube.com/watch?v=oRDYNWCYnGo)

* Journal is a log of every single thing that the database processes.
* And when you do a write to the database, an update, it also writes to this journal.
* Journal in memory as well, when does the journal get written back to disk?
	Because that is when the data is really considered to be persistent.
* `w=1` by default, it's 1 - mean wait for this server
* `j=false` by default, which stands for journal, represents whether or not we wait for this journal to be written to disk before we continue.


| writer `w` | journal `j` |            |
|------------|-------------|------------|
| w=1        | j=false     | but we're not going to wait for the journal to sync. fast, small windows of vulnerability |
| w=1        | j=true      | slow, window of vulnerability is removed |
| w=0        | unacknowledged write | |

Important:
`w=0` We don't recommend this as the way you use the database.

## Network Errors 
[Lecture Video](https://www.youtube.com/watch?v=xWNzCkTCN-M)

Quiz:

What are the reasons why an application may receive an error back even if the write was successful.
* The network TCP connection between the application and the server was reset after the server received a write but before a response could be sent.
* The MongoDB server terminates between receiving the write and responding to it.
* The network fails between the time of the write and the time the client receives a response to the write.

## Introduction to Replication 
[Lecture Video](https://www.youtube.com/watch?v=f1WTYGORU3w)

* availability
* fault tolerance

Quiz:

What is the minimum original number of nodes needed to assure the election of a new Primary if a node goes down?
* 3

## Replica Set Elections 
[Lecture Video](https://www.youtube.com/watch?v=WFXSVHO78bQ)

Type of replica set nodes:
* `regular`: primary and secondary nodes, most normal type of node. `Can participate in elections`
* `arbiter`: exist for `voting` purposes, e.g. if you have an even number of regular nodes it can ensure a majority in an election - Allows you to have only two regular nodes. `Can participate in elections`
* `delayed/regular`: are disaster recovery nodes - Can be set to be whatever time behind the regular nodes. `It can't be elected to be a primary node`. (priority `p=0`) `Can’t participate in elections`
* `hidden`: a node that is often used for analytics, `It can’t become the primary`. (priority `p=0`) `Can participate in elections`

If the primary goes down the secondaries elect a new primary and the drivers will automatically connects to the new primary

## Write Consistency 
[Lecture Video](https://www.youtube.com/watch?v=Oqf_Eza-s1M)

* You can only write to the primary node which replicates asynchronous to secondary nodes
* If you only read from the primary you will have strong consistency (default behaviour)
* You can allow your reads to go to secondaries - you might read stale data and have eventual consistency
* Eventual consistency means that eventually, you'll be able to read what you wrote, but there's no guarantee that you'll be able to read it in any particular time frame.

Quiz:

During the time when failover is occurring, can writes successfully complete?
* Yes

## Creating a Replica Set 
[Lecture Video](https://www.youtube.com/watch?v=flCFVFBRsKI)

* Start a replication set: ```mongod -replSet m101 --logpath "1.log" --dbpath /data/rs1 --fork```

create_replica_set.sh
```sh
#!/usr/bin/env bash

mkdir -p /data/rs1 /data/rs2 /data/rs3
mongod --replSet m101 --logpath "1.log" --dbpath /data/rs1 --port 27017 --oplogSize 64 --fork --smallfiles
mongod --replSet m101 --logpath "2.log" --dbpath /data/rs2 --port 27018 --oplogSize 64 --smallfiles --fork
mongod --replSet m101 --logpath "3.log" --dbpath /data/rs3 --port 27019 --oplogSize 64 --smallfiles --fork
```
* Register replica set nodes in the mongo shell:

init_replica.js
```javascript
config = { _id: "m101", members:[
          { _id : 0, host : "localhost:27017"},
          { _id : 1, host : "localhost:27018"},
          { _id : 2, host : "localhost:27019"} ]
};

rs.initiate(config);
rs.status();
```

```sh
mongo --port 27018
...
rs.initiate(config) // Initializes the replica set - can’t be executed on a node which can’t become primary
rs.status()         // Gives you the status information about the replica set
rs.slaveOk()        // If issued on a secondary node it allows you to read from this secondary node
rs.isMaster()       // Tells you if you are the primary node
rs.stepDown()       // Forces primary node to step down as a primary node
```

## Replica Set Internals 
[Lecture Video](https://www.youtube.com/watch?v=6GbrJmxCEl0)

* Replication is done via a capped collection called `oplog.rs` in the “local” database
* Secondaries ask the primary for any items since a certain timestamp
* That the secondaries are constantly reading the ```oplog``` of the primary. It's true that the oplog entries originally come from the primary, but secondaries can sync from another secondary, as long as at least there is a chain of oplog syncs that lead back to the primary.

Quiz:

Which of the following statements are true about replication. Check all that apply.
* Replication supports mixed-mode storage engines. For examples, a `mmapv1` primary and  `wiredTiger` secondary.
* A copy of the oplog is kept on both the primary and secondary servers.
* The oplog is implemented as a capped collection.

## Failover and Rollback 
[Lecture Video](https://www.youtube.com/watch?v=IW1oW_Adlt0)

* When the primary dies `failover` a secondary which becomes elected as a new primary which does not have the latest entries from the old primaries oplog
* When the former primary node comes back up as a secondary node it will request the oplog data from the new primary and `rollback` the writes the current primary does not have and write them to a `rollback` file which can be applied manually
* If the oplog of the new primary has looped during the time the old primary was down the entire dataset will be copied from the new primary
* The risk of losing data due to a rollback can be avoided by waiting till the majority of the nodes have the data - set the write concern w=majority

> Notes:
>While it is true that a replica set will never `rollback` a write if it was performed with `w=majority` and that write successfully replicated to a majority of nodes, it is possible that a write performed with `w=majority` gets `rolled back`. Here is the scenario:
> * You do write with `w=majority` and a `failover` over occurs after the write has committed to the primary but before replication completes.
> * You will likely see an exception at the client. 
> * An election occurs and a new primary is elected. 
> * When the original primary comes back up, it will `rollback` the committed write. However, from your application's standpoint, that write never completed, so that's ok.

Quiz:

What happens if a node comes back up as a secondary after a period of being offline and the oplog has looped on the primary?
* The entire dataset will be copied from the primary.

## Connecting to a Replica Set from the Java Driver 
[Lecture Video](https://www.youtube.com/watch?v=701LZygtnK0)

Provide a seed list to the MongoClient instance
```java
MongoClient client = new MongoClient(
	Arrays.asList(
		new ServerAddress(“localhost”, 27017),
		new ServerAddress(“localhost”, 27018),
		new ServerAddress(“localhost”, 27019),
		...
	)
);
```


Quiz:

If you leave a replica set node out of the seedlist within the driver, what will happen?
* The missing node will be discovered as long as you list at least one valid node.

## When Bad Things Happen to Good Nodes 
[Lecture Video](https://www.youtube.com/watch?v=FyS8Rr6RacQ)

Will work even if the primary is not part of the seed list. The Java Client starts a background thread which
pings all nodes from the seed list and all discovered nodes to find out which one is the primary

Handle exceptions with MongoException - `MongoSocketReadException`: Prematurely reached end of stream
```java
   ...
   try{
      collection.insertOne(...);
   } catch (MongoException e){
   }
   ...
```
> Notes:
> In this case, the insert is idempotent because it contains a specific _id, and that field has a unique index. Inserts > that don't involve a uniquely indexed field are not idempotent.

## Write Concern Revisited 
[Lecture Video](https://www.youtube.com/watch?v=5VyXyccjS3k)

* `w` parameter determines how many nodes you wait for before you move on when you do an insert. And again, this is accomplished by calling `get last error` by the drivers with `w` set.
* `w[n]` - will wait for `n` node to acknowledge the write.
* `j=true` or `j=1` - will wait for the primary to write it all the way to disk. 
* `wtimeout=milliseconds` - indicate how long you willing to wait for your writes to be acknowledge by the secondaries
* `w`, `j` and `wtimeout`, collectively, are called write concern.
* You can use it three different places:
	* connection
	* collection inside the drivers
	* replica set
* `w=majority` - is the one that will wait for the majority of the nodes to replicate. (It's is enough to avoid the rollback case.)

```py
import pymongo

read_pref = pymongo.read_preferences.ReadPreference.SECONDARY

c = pymongo.MongoClient(host="mongodb://localhost:27017",
                        replicaSet="rs1",
                        w=3, wtimeout=10000, j=True, 
                        read_preference=read_pref)

db = c.m101
people = db.people

print "inserting"
people.insert({"name":"Andrew Erlichson", "favorite_color":"blue"})
print "inserting"
people.insert({"name":"Richard Krueter", "favorite_color":"red"})
print "inserting"
people.insert({"name":"Dwight Merriman", "favorite_color":"green"})
```

> Notes:
> Write concern (w) value can be set at client, database or collection level within PyMongo. When you call MongoClient, you get a connection to the driver, but behind the scenes, PyMongo connects to multiple nodes of the replica set. The w value can be set at the client level. Andrew says that the w concern can be set at the connection level; he really means client level. It's also important to note that wtimeout is the amount of time that the database will wait for replication before returning an error on the driver, but that even if the database returns an error due to wtimeout, the write will not be unwound at the primary and may complete at the secondaries. Hence, writes that return errors to the client due to wtimeout may in fact succeed, but writes that return success, do in fact succeed. Finally, the video shows the use of an insert command in PyMongo. That call is deprecated and it should have been insert_one.

Quiz:

If you set w=1 and j=1, is it possible to wind up rolling back a committed write to the primary on failover?
* Yes

> The primary goes down before it propagates the right to the secondary
> The secondary comes back, the secondary becomes primary.
> Then when the original primary returns, he's going to roll himself back because he's a head of the other primary.
> He's not going to send that one.
> He'll put that into a file.

## Read Preferences 
[Lecture Video](https://www.youtube.com/watch?v=mhHaS4ZWzZE)

By default, MongoDB reads and writes both go to the primary.

Nevertheless, if you would like to read from secondaries, in MongoDB, we do allow that.

`read preference` - you always have to write to the primary, but you can read from the secondaries. There are several different options for that:
* `primary` - all reads are send to the primary; (default to guarantee strict consistency)
* `primary preferred` - sends reads to primary or to a secondary if primary is down;
	* `rs.slaveOk` - you're saying it's OK to send read to the secondary.
* `secondary` - send reads to randomly selected secondaries, but not to the primary; (eventually consistent read)
* `secondary preferred` - send reads to secondaries or to the primary if there's no secondary available;
* `nearest` - send reads to returned nodes secondary or primary from results the closest in terms of ping time `15 milliseconds`.
	* `tag set` - you can mark certain nodes as being part of a certain data center. If you're in New York, you want your reads to go to the New York data center.
	
```py
import pymongo
import time

read_pref = pymongo.read_preferences.ReadPreference.SECONDARY

c = pymongo.MongoClient(host=["mongodb://localhost:27017",
                              "mongodb://localhost:27018",
                              "mongodb://localhost:27019"],
                              read_preference=read_pref)

db = c.m101
things = db.things

for i in range(1000):
    doc = things.find_one({'_id':i})
    print "Found doc ", doc
    time.sleep(.1)
```

`ReadPreference.SECONDARY` is still going to be robust within the face of the `AutoReconnect`.

Pymongo API docs can be found [here](http://api.mongodb.org/python/current/api/pymongo).

Quiz:

You can configure your applications via the drivers to read from secondary nodes within a replica set. What are the reasons that you might not want to do that? Check all that apply.
* If your write traffic is significantly greater than your read traffic, you may overwhelm the secondary, which must process all the writes as well as the reads. Replication lag can result.
* You may not read what you previously wrote to MongoDB.
* If the secondary hardware has insufficient memory to keep the read working set in memory, directing reads to it will likely slow it down.

## Review of Implications of Replication 
[Lecture Video](https://www.youtube.com/watch?v=K5ISnvYKQFQ)

* `Seed lists`, when you're using the drivers, which are primarily responsible for [INAUDIBLE] you to a new node during fail over, after a new primary is elected, drivers need to know about at least one member of the replica set.

* `Write concern`, we're in this distributed environment, waiting for some number of nodes to acknowledge your writes through the `w` parameter, the `j` parameter, which lets you wait or not wait for the `primary` node to commit that write to disk. 
And also the `wtimeout` parameter, which is how long you're going to wait to see that your write replicated to other members of the `replica set`.

* `Read Preferences`, there's multiple nodes for you to potentially read from, you have to decide whether or not you want to read from your primary, which is the default, most obvious, and preferred thing to do, or whether you want to take your reads from your secondaries.
And if you're going to take your read from your secondary, the application has to be ready to use data that's potentially stale with respect to what was written.

* `Errors can happen`, because of:
	* transient situations like `fail over` occurring 
	* there are network errors that occur
	* there's actually errors in terms of violating the unique key constraints, or other syntactic things.
	
Quiz:

If you set `w=4` on a connection and there are only three nodes in the replica set, how long will you wait in PyMongo for a response from an insert if you don't set a timeout?
* More than five minutes

## Introduction to Sharding 
[Lecture Video](https://www.youtube.com/watch?v=_GfDqa1qRl0)

* Enables horizontal scalability
* Shards are typically itself replica sets
* `mongos` is the sharding router which distributes data to the individual shards
* The application (and also the mongo shell) connects to `mongos` instead of `mongod`
* There can be multiple mongos - mongos typically runs on the same server as the application
* If a mongos goes down the application will connect to a different one – similar to replica sets
* `shard_key` determines to which shard a document goes
* Sharding is at database level – but you can define if you want to shared or not shard a specific collection
* Config servers (which are mongod) keep track of where the shards are – in production you typically use 3 of them

> At 2:55 I say that we use a range-based approach to distributing data across shards. That was right when this lecture was recorded but no longer. As of MongoDB 2.4, we also offer hash-based sharding, which offers a more even distribution of data as a function of shard key, at the expense of worse performance for range-based queries. For more information, see the documentation at [sharding-introduction](http://docs.mongodb.org/manual/core/sharding-introduction)

Quiz:

If the shard key is not include in a find operation and there are 4 shards, each one a replica set with 3 nodes, how many nodes will see the find operation?
* 4

> The answer is 4. Since the `shard_key` is not included in the find operation, mongos has to send the query to all 4 of the shards. Each shard has 3 replica-set members, but only one member of each replica set (the primary, by default) is required to handle the find.

## Building a Sharded Environment 
[Lecture Video](https://www.youtube.com/watch?v=dn45G2yw20A)

Set up two shards each a replica set of three mongod nodes:
[init_sharded_env.sh](https://university.mongodb.com/static/10gen_2015_M101J_March/handouts/init_sharded_env.24825a3cb9f2.sh)

* Set up shard as a replication set:
```sh
mongod --replSet s0 --logpath "s0-r0.log" --dbpath /data/shard0/rs0 --port 37017 --fork --shardsvr --smallfiles
```

* Set up config server:
```sh
mongod --logpath "cfg-a.log" --dbpath /data/config/config-a --port 57040 --fork --configsvr --smallfiles
```

* Set up mongos router with information about the config servers:
	* `mongos` now listens on the standard port (default `mongod`port)
```sh
mongos --logpath "mongos-1.log" --configdb localhost:57040,localhost:57041,localhost:57042 --fork
```

* On the mongo shell – tell the config servers (via the mongos) about the shards:
```sh
db.adminCommand( { addshard : "s0/"+"localhost:37017" } );
```

* enable sharding on test DB
```sh 
db.adminCommand({enableSharding: "school"})
```

* shard collection `stundents` with the shard key `student_id`
```sh
db.adminCommand({shardCollection: "school.students", key: {student_id:1}});
```

* `sh.help()` - Will display all the shard commands available in the mongo shell, e.g. `sh.status()`


create_scores3.js
```javascript
db=db.getSiblingDB("school");
types = ['exam', 'quiz', 'homework', 'homework'];
// 10,000 students
for (i = 0; i < 10000; i++) {

    // take 10 classes
    for (class_counter = 0; class_counter < 10; class_counter ++) {
	scores = []
	    // and each class has 4 grades
	    for (j = 0; j < 4; j++) {
		scores.push({'type':types[j],'score':Math.random()*100});
	    }

	// there are 500 different classes that they can take
	class_id = Math.floor(Math.random()*501); // get a class id between 0 and 500

	record = {'student_id':i, 'scores':scores, 'class_id':class_id};
	db.students.insert(record);

    }

}
```

Quiz:

If you want to build a production system with two shards, each one a replica set with three nodes, how may mongod processes must you start?
* 9

> `six` from two replica sets, each one with three nodes in the replica set.
> `two` to replica sets, each one with three nodes in the replica set.
> `one` to config servers.

## Implications of Sharding 
[Lecture Video](https://www.youtube.com/watch?v=ig278F60gRA)

* Each document needs the `shard key`
* The `shard key` is immutable
* Needs an index on first element of the `shard key` (can be compound but not multi-key)
* On an update you need to specify the `shard key` or `specify multi: true`
* A find with no `shard key` will go to all shards (be expensive!)
* The key used in most queries should be the `shard key`
* You can’t have a unique index unless it is part of/starts with the `shard key`

Quiz:

Suppose you wanted to shard the zip code collection after importing it. You want to shard on zip code. What index would be required to allow MongoDB to shard on zip code?
* An index on zip or a non-multi-key index that starts with zip.

## Sharding + Replication 
[Lecture Video](https://www.youtube.com/watch?v=gkUCUbM0oEg)

* Sharding and replication are almost always done together.
* `mongos` can connect to members of the replica set.
* `Write concerns` are still important in a sharded setup

Quiz:

Suppose you want to run multiple mongos routers for redundancy. What level of the stack will assure that you can failover to a different mongos from within your application?
* drivers

> because the drivers are what's attached mongos.
> `mongos` go up and down, so they can't be the ones responsible for that.
> `config servers` are in charge of figuring out where the chunks are for the sharding.

## Choosing a Shard Key
[Lecture Video](https://www.youtube.com/watch?v=8q2GB3QSBSI)

* Sufficient cardinality (variety of values)
* Avoid `monotonically increasing keys` to avoid `hotspotting` in writing (e.g. order_id, order_date)
* Compound sharding key is possible

Quiz:

You are building a facebook competitor called footbook that will be a mobile social network of feet. You have decided that your primary data structure for posts to the wall will look like this:

```javascript
{
     'username':'toeguy',
     'posttime':ISODate("2012-12-02T23:12:23Z"),
     "randomthought": "I am looking at my feet right now",
     'visible_to':['friends','family', 'walkers']
}
```

* Choosing posttime as the shard key will cause hotspotting as time progresses.
> The posttime will cause hotspotting because it's monotonically increasing.
* Choosing username as the shard key will distribute posts to the wall well across the shards.
> The username should be posts well across the shards.
* Choosing visible_to as a shard key is illegal.
> Because, it can index that is going to be on the shard or the starting part of the shard key and visible_to will acquire a multi-key index and that's illegal.

- Choosing posttime as the shard key suffers from low cardinality.
> Posttimes are going to be very varied, lots of different values. They are monotonically increasing. It's going to cause hotspotting on the inserts.