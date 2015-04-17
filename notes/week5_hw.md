Week 5: Aggregation Framework (HOMEWORKS)
=========================================

# HOMEWORK 5.1 (HANDS ON)

```js
db.posts.aggregate([ 
	{$unwind:'$comments'},
	{$group: {'_id':'$comments'}},
	{$group: {_id:'$_id.author', count:{$sum:1}}},
	{$sort: {count:-1}}
])
```

# HOMEWORK 5.2 (HANDS ON)

```js
db.zips.aggregate([
	{$match: {state: {$in: ["CA", "NY"]}}},
	{$group: {_id: {city: "$city", state: "$state"}, pop: {$sum: "$pop"}}},
	{$match: {pop: {$gt: 25000}}},
	{$group: {_id: null, avg: {$avg: "$pop"}}}
])
```

# HOMEWORK 5.3 (HANDS ON)

```js
db.grades.aggregate([
	{"$unwind": "$scores"},
	{"$match": {"scores.type": {"$ne": "quiz"}}},
	{"$group": {"_id": {"student": "$student_id", "class": "$class_id"}, "std_avg": {"$avg": "$scores.score"}} },
	{"$group": {"_id": "$_id.class", "class_avg":{"$avg": "$std_avg"}} },
	{"$sort":  {"class_avg": -1}}
])
```

# HOMEWORK 5.4

```js
db.zips.aggregate([
	{"$project": 
	   {
	    "first_char": {"$substr" : ["$city",0,1]},
	    "city":1,
	    "pop":1,
	    "state":1
	   }    
	},
	{"$match": {"first_char": {"$regex":"[0-9]"}}},
	{"$group": {"_id": null, "sum": {"$sum": "$pop"}}}
])
```