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
* 'w=1' by default, it's 1 - mean wait for this server
* 'j=false' by default, which stands for journal, represents whether or not we wait for this journal to be written to disk before we continue.


| writer 'w' | journal 'j' |            |
|------------|-------------|------------|
| w=1        | j=false     | but we're not going to wait for the journal to sync. fast, small windows of vulnerability |
| w=1        | j=true      | slow, window of vulnerability is removed |
| w=0        | unacknowledged write | |

Important:
'w=0' We don't recommend this as the way you use the database.

## Network Errors 
[Lecture Video](https://www.youtube.com/watch?v=xWNzCkTCN-M)

Quiz:
What are the reasons why an application may receive an error back even if the write was successful.
[x] The network TCP connection between the application and the server was reset after the server received a write but before a response could be sent.
[x] The MongoDB server terminates between receiving the write and responding to it.
[x] The network fails between the time of the write and the time the client receives a response to the write.

## Introduction to Replication 
[Lecture Video](https://www.youtube.com/watch?v=)

## Replica Set Elections 
[Lecture Video](https://www.youtube.com/watch?v=)

## Write Consistency 
[Lecture Video](https://www.youtube.com/watch?v=)

## Creating a Replica Set 
[Lecture Video](https://www.youtube.com/watch?v=)

## Replica Set Internals 
[Lecture Video](https://www.youtube.com/watch?v=)

## Failover and Rollback 
[Lecture Video](https://www.youtube.com/watch?v=)

## Connecting to a Replica Set from the Java Driver 
[Lecture Video](https://www.youtube.com/watch?v=)

## When Bad Things Happen to Good Nodes 
[Lecture Video](https://www.youtube.com/watch?v=)

## Write Concern Revisited 
[Lecture Video](https://www.youtube.com/watch?v=)

## Read Preferences 
[Lecture Video](https://www.youtube.com/watch?v=)

## Review of Implications of Replication 
[Lecture Video](https://www.youtube.com/watch?v=)

## Introduction to Sharding 
[Lecture Video](https://www.youtube.com/watch?v=)

## Building a Sharded Environment 
[Lecture Video](https://www.youtube.com/watch?v=)

## Implications of Sharding 
[Lecture Video](https://www.youtube.com/watch?v=)

## Sharding + Replication 
[Lecture Video](https://www.youtube.com/watch?v=)

## Choosing a Shard Key
[Lecture Video](https://www.youtube.com/watch?v=)
