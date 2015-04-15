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
         "sum_prices":{  
            "$sum":"$prices"
         }
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
         "population":{  
            $sum:"$pop"
         }
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
         "avg_prices":{  
            "$avg":"$prices"
         }
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
[Lecture Video]()

## Using $push 
[Lecture Video]()

## Using $max and $min 
[Lecture Video]()

## Double $group stages 
[Lecture Video]()

## Using $project 
[Lecture Video]()

## Using $match 
[Lecture Video]()

## Using $sort 
[Lecture Video]()

## Using $limit and $skip 
[Lecture Video]()

## Revisiting $first and $last 
[Lecture Video]()

## Using $unwind 
[Lecture Video]()

## $unwind example 
[Lecture Video]()

## Double $unwind 
[Lecture Video]()

## Mapping between SQL and Aggregation 
[Lecture Video]()

## Some Common SQL examples 
[Lecture Video]()

## Limitations of the Aggregation Framework 
[Lecture Video]()

## Aggregation Framework with the Java Driver 
[Lecture Video]()