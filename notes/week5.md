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
	
* Stages:
	* out: allow put docs to another collection (in: 1 / out: 1)
	* redact:
	* geonear: limit doc by the pipeline stage based on location

* Each stage can exist more than once in a pipeline


## Simple Example Expanded 
[Lecture Video]()

## Compound Grouping 
[Lecture Video]()

## Using a document for _id 
[Lecture Video]()

## Aggregation Expressions 
[Lecture Video]()

## Using $sum 
[Lecture Video]()

## Using $avg 
[Lecture Video]()

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