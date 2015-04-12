Week 1: Introduction
====================

# Topics:
Overview, Design Goals, the Mongo Shell, JSON Intro, Installing Tools, Overview of Blog Project. Maven, Spark and Freemarker Intro


## What is MongoDB?

### MongoDB is a non-relational JSON Document store.

This means that it does not support typical relational algebra or tables/columns/rows like your typical relational database. It stores documents in the JSON format.

MongoDB, unlike relational databases, is able to store documents that do not have the same structure.

Features that MongoDB doesn't have:

* Joins
* SQL
* Transactions

## MongoDB Relative to Relational DBs
[Lecture Video](https://www.youtube.com/watch?v=-KIC1LXxcGM)

MongoDB gives you a large depth of functionality while keeping high scalability and performance.

Documents are hierarchical and MongoDB doesn't support atomic transactions.


## Mongo is Schemaless
[Lecture Video](https://www.youtube.com/watch?v=uKB-Hoqs6zI)

In MongoDB, you don't have to alter tables to add columns, etc. Each document can have a different schema.


## JSON Revisited
[Lecture Video](https://www.youtube.com/watch?v=CTffxoSSLqg)

There are only two different data types in JSON: Arrays and Dictionaries.

Arrays are represented by `[` and `]`. Dictionaries are represented by `{` and `}`. These can be combined and nested in any way possible.