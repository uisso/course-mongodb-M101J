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
---

* You can only write to the primary node which replicates asynchronous to secondary nodes
* If you only read from the primary you will have strong consistency (default behaviour)
* You can allow your reads to go to secondaries - you might read stale data and have eventual consistency

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
[Lecture Video]()

## Creating a Replica Set 
[Lecture Video]()

## Replica Set Internals 
[Lecture Video]()

## Failover and Rollback 
[Lecture Video]()

## Connecting to a Replica Set from the Java Driver 
[Lecture Video]()

## When Bad Things Happen to Good Nodes 
[Lecture Video]()

## Write Concern Revisited 
[Lecture Video]()

## Read Preferences 
[Lecture Video]()

## Review of Implications of Replication 
[Lecture Video]()

## Introduction to Sharding 
[Lecture Video]()

## Building a Sharded Environment 
[Lecture Video]()

## Implications of Sharding 
[Lecture Video]()

## Sharding + Replication 
[Lecture Video]()

## Choosing a Shard Key
[Lecture Video]()
