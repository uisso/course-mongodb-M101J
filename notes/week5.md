Week 5: Aggregation Framework
=============================

# Topics:
Goals, The Use Of The Pipeline, Comparison With SQL Facilities.

## Simple Aggregation Example 
[Lecture Video](https://www.youtube.com/watch?v=DQBXCsjeO5c)

```sql
select manufacturer, count(*), from products where product group by manufacturer.
```

```js
use agg
db.products.aggregate([
    {$group:
     {
	 _id:"$manufacturer",    //set the field '_id' to the field you want to group by
	 num_products:{$sum:1}   //define field 'num_products'
     }
    }
])
```

Quiz:
```js
db.products.aggregate([{
  "$group":{
         "_id":"$category",
         "num_products":{"$sum":1}
   }
}])
```

## The Aggregation Pipeline 
[Lecture Video](https://www.youtube.com/watch?v=AuO8CEkTG6Y)

* Each document in the array parameter to the aggregate function is a stage in the pipeline
* pipeline stages from collection to results: collection -> [$project] -> [$match] -> [$group] -> [$sort] -> result

* Stages (main operators):
	* $project: select relevant fields and reshape doc (in: 1 / out: 1)
	* $match: filters docs; (in: n / out: n-x)
	* $group: aggregates; Reduces the number of docs (in: n / out: n-x)
	* $sort: sorts the docs (in: 1 / out: 1)
	* $skip: skips docs (in: n / out: n-x)
	* $limit: limits returned docs (in: n / out: n-x)
	* $unwind: explodes arrays - Produces a doc for each value in an array-key-field with everything else repeated (in: n / out: n+x)
	
* Stages (plus operators):
	* $out: allow put docs to another collection (in: 1 / out: 1)
	* $redact:
	* $geonear: limit doc by the pipeline stage based on location

* Each stage can exist more than once in a pipeline


## Simple Example Explained 
[Lecture Video](https://www.youtube.com/watch?v=3lEpnMcfpCs)

Quiz:
```js
db.stuff.find()
{ "_id" : ObjectId("50b26f9d80a78af03b5163c8"), "a" : 1, "b" : 1, "c" : 1 }
{ "_id" : ObjectId("50b26fb480a78af03b5163c9"), "a" : 2, "b" : 2, "c" : 1 }
{ "_id" : ObjectId("50b26fbf80a78af03b5163ca"), "a" : 3, "b" : 3, "c" : 1 }
{ "_id" : ObjectId("50b26fcd80a78af03b5163cb"), "a" : 3, "b" : 3, "c" : 2 }
{ "_id" : ObjectId("50b26fd380a78af03b5163cc"), "a" : 3, "b" : 5, "c" : 3 }

db.stuff.aggregate([{$group:{_id:'$c'}}])
```
How many documents will be in the result set from aggregate?
* 3

## Compound Grouping 
[Lecture Video](https://www.youtube.com/watch?v=qTbtax_cKcc)

```sql
select manufacturer, category, count(*) from products group by manufacturer, category.
``` 

* Use a compound id with manufacturer and category
```js
db.products.aggregate([{
  "$group":{
        "_id":{                     //create new key or _id for each doc 
           "maker":"$manufacturer"
           "category":"$category"
        },
        "num_products":{"$sum":1}
  }
}])
```

## Using a document for _id 
[Lecture Video](https://www.youtube.com/watch?v=zoN4cj_XQzY)

## Aggregation Expressions 
[Lecture Video](https://www.youtube.com/watch?v=L4G14MTfTgQ)

These are the expression that you can use aggregation grouping stage of the pipeline [$group] :
* $sun : add one to a key (mySum: {$sum:1}) or sum up keys (sum_prices:{$sum:”$price”})
* $avg, $min, $max : average, minimum or maximum value of a key
* $push : build the arrays
* $addToSet : build the arrays, no duplicate a element - uniquely
* $first : only useful after a sort
* $last : only useful after a sort

## Using $sum 
[Lecture Video](https://www.youtube.com/watch?v=93MSz3uDC1A)

```js
db.products.aggregate([  
   {  
      "$group":{  
         "_id":{  
            "maker":"$manufacturer"
         },
         "sum_prices":{ "$sum":"$prices" }
      }
   }
])
```

Quiz:
```js
db.zips.aggregate([  
   {  
      "$group":{  
         "_id":"$state",
         "population":{ $sum:"$pop" }
      }
   }
])

```

## Using $avg 
[Lecture Video](https://www.youtube.com/watch?v=baIDZ-M5j7w)

```js
db.products.aggregate([  
   {  
      "$group":{  
         "_id":{  
            "category":"$category"
         },
         "avg_prices":{ "$avg":"$prices" }
      }
   }
])
```
Quiz:
```js
db.zips.aggregate([
        {$group:
         {
    	 _id: {
           "state":"$state", "zip":"$zip"
    	 },
    	 avg_pop:{$avg:"$pop"}
         }
        }
    ])
```

## Using $addToSet 
[Lecture Video](https://www.youtube.com/watch?v=YzURaZnKI9s)

* Create arrays for each doc
* $addToSet adds it only if it's not already there.

```js
db.products.aggregate([  
   {  
      "$group":{  
         "_id":{  
            "maker":"$manufacturer"
         },
         "categories":{ "$addToSet":"$category" }
      }
   }
])
```
Quiz:
```js
db.zips.aggregate([  
   {  
      "$group":{  
         "_id":"$city",
         "postal_codes":{ "$addToSet":"$_id" }
      }
   }
])
```

## Using $push 
[Lecture Video](https://www.youtube.com/watch?v=LQcBM-g0ACY)

$push is very similar to $addToSet, except that $push does not guarantee that it adds each item only once. It doesn't look through to make sure it's not already there.

```js
db.products.aggregate([  
   {  
      "$group":{  
         "_id":{  
            "maker":"$manufacturer"
         },
         "categories":{ "$push":"$category" }
      }
   }
])
```

## Using $max and $min 
[Lecture Video](https://www.youtube.com/watch?v=BYoNX4trjOQ)

```js
db.products.aggregate([  
   {  
      "$group":{  
         "_id":{  
            "maker":"$manufacturer"
         },
         "maxprice":{ "$max":"$price" }
      }
   }
])
```

Quiz:
```js
db.zips.aggregate([  
   {  
      "$group":{  
         "_id":"$state",
         "pop":{ "$max":"$pop" }
      }
   }
])
```

## Double $group stages 
[Lecture Video](https://www.youtube.com/watch?v=EIWF9Oxeb8M)

```js
db.grades.aggregate([  
   {  
      '$group':{  
         _id:{  
            class_id:"$class_id",
            student_id:"$student_id"
         },
         'average':{  
            "$avg":"$score"
         }
      }
   },//grouping one
   {  
      '$group':{  
         _id:"$_id.class_id",
         'average':{  
            "$avg":"$average"
         }
      }
   } //grouping two from previous $group stage 
])
```

## Using $project 
[Lecture Video](https://www.youtube.com/watch?v=TbQ2PI5Fib0)

It's a [1:1] stage of the pipeline, so for every document that comes into the project phase, one document will leave the project phase.

You can do things like:
* remove a key: if you don't mention a key, it is not included, except for _id, which must be explicitly suppressed {$project: {_id: 0, ...
* add a new key: also possible to create new subdocuments
* reshape the keys: you could take a key and decide to put it into a subdocument with another key.
* keep keys: {$project: {myKey: 1, ...
* rename keys / use functions: $toUpper, $toLower, $add, $multiply

Simple function of keys:
* $toUpper
* $toLower
* $add : add something to the value
* $multiply : multiply by number

```js
db.products.aggregate([
    {$project:
     {
	 _id:0,
	 'maker': {$toLower:"$manufacturer"},
	 'details': {'category': "$category",
		     'price' : {"$multiply":["$price",10]}
		    },
	 'item':'$name'
     }
    }
])
```

Quiz:
```js
db.zips.aggregate([
    {$project:
     {
     _id:0,                      //remove _id
     'city': {$toLower:"$city"},
     'pop':1,                    //show pop
     'state':1,                  //show state
     'zip':'$_id'                //ref _id
     }
    }
])
```

## Using $match 
[Lecture Video](https://www.youtube.com/watch?v=7RtHG90Hrbw)

$match phase performs a filtering which is an [n:1] operation.
Match will go through each document and see if the document matches your criteria.

There are two reasons why you might want to match.
* pre agg filter
* filter the results

```js
db.zips.aggregate([
    {$match:
     {
	 state:"CA"
     }
    },
    {$group:
     {
	 _id: "$city",
	 population: {$sum:"$pop"},
	 zip_codes: {$addToSet: "$_id"}
     }
    },
    {$project: //reshape the doc above grouped and matched doc
     {
	 _id: 0,
	 city: "$_id",
	 population: 1,
	 zip_codes:1
     }
    }
])
```

One thing to note about $match (and $sort) is that they can use indexes, but only if done at the beginning of the aggregation pipeline.

You can read the documentation [here](http://docs.mongodb.org/manual/core/aggregation-pipeline).

Quiz:
```js
db.zips.aggregate([
    {$match:
     {
	 pop: {$gt:100000}
     }
    }
])
```

## Using $sort 
[Lecture Video](https://www.youtube.com/watch?v=HUEtV7omSb8)

$sort supports both disk and memory bases sorting.
* by default the agg framework will try to sort in memory.
* there is a limit of 100MB for any given pipeline stage

before or after the grouping stage (there are some good reasons!)

```js
db.zips.aggregate([
    {$match:
     {
	 state:"NY"
     }
    },
    {$group:
     {
	 _id: "$city",
	 population: {$sum:"$pop"},
     }
    },
    {$project:
     {
	 _id: 0,
	 city: "$_id",
	 population: 1,
     }
    },
    {$sort:
     {
	 population:-1
     }
    }
])
```

Quiz:
```js
db.zips.aggregate([
    {$sort:
     {
	 state:1, city:1
     }
    }
])
```

## Using $limit and $skip 
[Lecture Video](https://www.youtube.com/watch?v=o5hzYKXUyrU)

So it doesn't make any sense to skip and limit, unless you first sort.
First $skip – then $limit (order of the stages in the pipeline matter)

```js
db.zips.aggregate([
    {$match:
     {
	 state:"NY"
     }
    },
    {$group:
     {
	 _id: "$city",
	 population: {$sum:"$pop"},
     }
    },
    {$project:
     {
	 _id: 0,
	 city: "$_id",
	 population: 1,
     }
    },
    {$sort:
     {
	 population:-1
     }
    },
    {$skip: 10},
    {$limit: 5}
])
```

## Revisiting $first and $last 
[Lecture Video](https://www.youtube.com/watch?v=JOdAnxVAMwc)

$first and $last are group operators.

```js
db.zips.aggregate([
    /* get the population of every city in every state */
    {$group:
     {
	 _id: {state:"$state", city:"$city"},
	 population: {$sum:"$pop"},
     }
    },
     /* sort by state, population */
    {$sort: 
     {"_id.state":1, "population":-1}
    },
    /* group by state, get the first item in each group */
    {$group: 
     {
	 _id:"$_id.state",
	 city: {$first: "$_id.city"},
	 population: {$first:"$population"}
     }
    },
    /* now sort by state again */
    {$sort:
     {"_id":1}
    }
])
```

## Using $unwind 
[Lecture Video](https://www.youtube.com/watch?v=E4aYOQPeQvI)

```js
use agg;
db.items.drop();
db.items.insert({_id:'nail', 'attributes':['hard', 'shiny', 'pointy', 'thin']});
db.items.insert({_id:'hammer', 'attributes':['heavy', 'black', 'blunt']});
db.items.insert({_id:'screwdriver', 'attributes':['long', 'black', 'flat']});
db.items.insert({_id:'rock', 'attributes':['heavy', 'rough', 'roundish']});
db.items.aggregate([{$unwind:"$attributes"}]);

{ "_id" : "nail", "attributes" : "hard" }
{ "_id" : "nail", "attributes" : "shiny" }
{ "_id" : "nail", "attributes" : "pointy" }
{ "_id" : "nail", "attributes" : "thin" }
{ "_id" : "hammer", "attributes" : "heavy" }
{ "_id" : "hammer", "attributes" : "black" }
{ "_id" : "hammer", "attributes" : "blunt" }
{ "_id" : "screwdriver", "attributes" : "long" }
{ "_id" : "screwdriver", "attributes" : "black" }
{ "_id" : "screwdriver", "attributes" : "flat" }
{ "_id" : "rock", "attributes" : "heavy" }
{ "_id" : "rock", "attributes" : "rough" }
{ "_id" : "rock", "attributes" : "roundish" }

```

## $unwind example 
[Lecture Video](https://www.youtube.com/watch?v=U_4Enh2TTp4)

```js
use blog;
db.posts.aggregate([
    /* unwind by tags */
    {"$unwind":"$tags"},
    /* now group by tags, counting each tag */
    {"$group": 
     {"_id":"$tags",
      "count":{$sum:1}
     }
    },
    /* sort by popularity */
    {"$sort":{"count":-1}},
    /* show me the top 10 */
    {"$limit": 10},
    /* change the name of _id to be tag */
    {"$project":
     {_id:0,
      'tag':'$_id',
      'count' : 1
     }
    }
])
```
Quiz:
Reverse the effects of an unwind?
 $push

## Double $unwind 
[Lecture Video](https://www.youtube.com/watch?v=YXGL27217B8)

And therefore create a Cartesian product of the two arrays as well as the rest of the document.

```js
use agg;
db.inventory.drop();
db.inventory.insert({'name':"Polo Shirt", 'sizes':["Small", "Medium", "Large"], 'colors':['navy', 'white', 'orange', 'red']})
db.inventory.insert({'name':"T-Shirt", 'sizes':["Small", "Medium", "Large", "X-Large"], 'colors':['navy', "black",  'orange', 'red']})
db.inventory.insert({'name':"Chino Pants", 'sizes':["32x32", "31x30", "36x32"], 'colors':['navy', 'white', 'orange', 'violet']})
```

```js
> db.inventory.find()
{ "_id" : ObjectId("552ef5ecdf3c683c8c1fd66f"), "name" : "Polo Shirt", "sizes" : [ "Small", "Medium", "Large" ], "colors" : [ "navy", "white", "orange", "red" ] }
{ "_id" : ObjectId("552ef5ecdf3c683c8c1fd670"), "name" : "T-Shirt", "sizes" : [ "Small", "Medium", "Large", "X-Large" ], "colors" : [ "navy", "black", "orange", "red" ] }
{ "_id" : ObjectId("552ef5eddf3c683c8c1fd671"), "name" : "Chino Pants", "sizes" : [ "32x32", "31x30", "36x32" ], "colors" : [ "navy", "white", "orange", "violet" ] }
```

```js
db.inventory.aggregate([
    {$unwind: "$sizes"},
    {$unwind: "$colors"},
    {$group: 
     {
	'_id': {'size':'$sizes', 'color':'$colors'},
	'count' : {'$sum':1}
     }
    }
])
{ "_id" : { "size" : "31x30", "color" : "orange" }, "count" : 1 }
{ "_id" : { "size" : "Medium", "color" : "navy" }, "count" : 2 }
{ "_id" : { "size" : "31x30", "color" : "navy" }, "count" : 1 }
{ "_id" : { "size" : "32x32", "color" : "violet" }, "count" : 1 }
{ "_id" : { "size" : "36x32", "color" : "navy" }, "count" : 1 }
{ "_id" : { "size" : "32x32", "color" : "white" }, "count" : 1 }
{ "_id" : { "size" : "32x32", "color" : "navy" }, "count" : 1 }
{ "_id" : { "size" : "X-Large", "color" : "orange" }, "count" : 1 }
{ "_id" : { "size" : "Medium", "color" : "red" }, "count" : 2 }
{ "_id" : { "size" : "Large", "color" : "orange" }, "count" : 2 }
{ "_id" : { "size" : "X-Large", "color" : "black" }, "count" : 1 }
{ "_id" : { "size" : "X-Large", "color" : "navy" }, "count" : 1 }
{ "_id" : { "size" : "Small", "color" : "black" }, "count" : 1 }
{ "_id" : { "size" : "Large", "color" : "navy" }, "count" : 2 }
{ "_id" : { "size" : "Medium", "color" : "black" }, "count" : 1 }
{ "_id" : { "size" : "Small", "color" : "navy" }, "count" : 2 }
{ "_id" : { "size" : "31x30", "color" : "white" }, "count" : 1 }
{ "_id" : { "size" : "Large", "color" : "white" }, "count" : 1 }
{ "_id" : { "size" : "Large", "color" : "red" }, "count" : 2 }
{ "_id" : { "size" : "36x32", "color" : "orange" }, "count" : 1 }
```

Quiz:
Can you reverse the effects of a double unwind (2 unwinds in a row) in our inventory collection (shown in the lesson ) with the $push operator?
```js
db.inventory.aggregate([
    {$unwind: "$sizes"},
    {$unwind: "$colors"},
    /* create the color array */
    {$group: 
     {
	'_id': {name:"$name",size:"$sizes"},
	 'colors': {$push: "$colors"},
     }
    },
    /* create the size array */
    {$group: 
     {
	'_id': {'name':"$_id.name",
		'colors' : "$colors"},
	 'sizes': {$push: "$_id.size"}
     }
    },
    /* reshape for beauty */
    {$project: 
     {
	 _id:0,
	 "name":"$_id.name",
	 "sizes":1,
	 "colors": "$_id.colors"
     }
    }
])
{ "sizes" : [ "Medium", "Large", "Small" ], "name" : "Polo Shirt", "colors" : [ "navy", "white", "orange", "red" ] }
{ "sizes" : [ "Large", "X-Large", "Small", "Medium" ], "name" : "T-Shirt", "colors" : [ "navy", "black", "orange", "red" ] }
{ "sizes" : [ "36x32", "31x30", "32x32" ], "name" : "Chino Pants", "colors" : [ "navy", "white", "orange", "violet" ] }
```

Simple grouping rerting with $addToSet
```js
db.inventory.aggregate([
    {$unwind: "$sizes"},
    {$unwind: "$colors"},
    {$group: 
     {
	'_id': "$name",
	 'sizes': {$addToSet: "$sizes"},
	 'colors': {$addToSet: "$colors"},
     }
    }
])
{ "_id" : "Chino Pants", "sizes" : [ "36x32", "31x30", "32x32" ], "colors" : [ "violet", "white", "orange", "navy" ] }
{ "_id" : "T-Shirt", "sizes" : [ "X-Large", "Large", "Medium", "Small" ], "colors" : [ "red", "black", "orange", "navy" ] }
{ "_id" : "Polo Shirt", "sizes" : [ "Large", "Medium", "Small" ], "colors" : [ "red", "white", "orange", "navy" ] }
```

## Mapping between SQL and Aggregation 
[Lecture Video]()

## Some Common SQL examples 
[Lecture Video]()

## Limitations of the Aggregation Framework 
[Lecture Video]()

## Aggregation Framework with the Java Driver 
[Lecture Video]()