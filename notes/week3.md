Week 3: Schema design
=====================

# Topics:
Patterns, Case Studies & Tradeoffs

## MongoDB Schema Design
[Lecture Video](https://www.youtube.com/watch?v=itnfWjMnQ4A)

* Which data is used together; which data is read; which data is written all the time -> Make the schema matching to the data access patterns of your application
* Mongo has no joins / no foreign key constraints -> but can embed Documents (Pre-Join)
* MongoDB does not support transactions -> but atomic operations on ONE document, so instead of transactions, you have three options
	- Restructure data to live in one document
	- Implement transaction in Software
	- Tolerate a little bit of inconsistency

## Relational Normalization 
[Lecture Video](https://www.youtube.com/watch?v=GX__f2s4hd8)
 
## Mongo Design for Blog 
[Lecture Video](https://www.youtube.com/watch?v=PRylEHH5t84)
 
## Alternative Schema for Blog 
[Lecture Video](https://www.youtube.com/watch?v=ZvqNWVWB2-o)
 
## Living Without Constraints 
[Lecture Video](https://www.youtube.com/watch?v=YFRMkDPaams)
 
## Living Without Transactions 
[Lecture Video](https://www.youtube.com/watch?v=FfRr3qjRfww)
 
## One to One Relations 
[Lecture Video](https://www.youtube.com/watch?v=cCsfon0vUlQ)

* EITHER two collections were one document point to the document in the other collection by _id
* OR embed one document in the other
* Decision driven by
	- Frequency of access; Are the documents read together – do you want to pull everything into memory
	- Are the documents written together
	- Document max size: 16 MB
	- Do you need atomicity
 
## One to Many Relations 
[Lecture Video](https://www.youtube.com/watch?v=EIaP1KbVkUc)

* Two collections – linking from “many collection” to _id of “one collection”
* If it is really one-to-few: possible to have “one collection” and embed “few document”
 
## Many to Many Relations 
[Lecture Video](https://www.youtube.com/watch?v=fEYYjZ7zEHc)

* It often really is a few-to-few relation, e.g. authors-books
* EITHER have two collections and add an array of book-ids in author document or vice versa – depends on access pattern
* (OR embed book in author document but this might lead to inconsistency as one book might be duplicated – also not a good idea if you need to store one item before the other exits, e.g. studentteacher)
 
## Multikeys 
[Lecture Video](https://www.youtube.com/watch?v=KtIY4Q1tUao)
 
## Benefits of Embedding 
[Lecture Video](https://www.youtube.com/watch?v=XIN0Dqht08Q)
 
## Trees 
[Lecture Video](https://www.youtube.com/watch?v=lIjXyQklGWY)
 
## When to Denormalize 
[Lecture Video](https://www.youtube.com/watch?v=jDZ-HFoJ0vg)
 
## What is an ODM?
[Lecture Video](https://www.youtube.com/watch?t=15&v=pfp7sCEjWJY)

* Object Document Mapper - Morphia


## GridFS - Blobs
* > 16MB
* Files collection and Chunks collection. MongoDB spits Blob into chunks of 16MB and stores them in the Chunks collection. Each chunk has a file_id pointing to the _id of its file document.

```java
GridFS videos = new GridFS(db, “videos”);
GridFSInputFile video = videos.createFile(inputStream, “video.mp4”);
BasicDBObject metadata = …
video.setMetaData(metadata);
video.save();
…
GridFSDBFile myVideo = videos.findOne(new BasicDBObject(“filename”, “video.mp4”)));
myVideo.writeTo(outputStream)
```