Week 4: Performance (HOMEWORKS)
===============================

> If you see this before you get your homeworks!?
> You are only being dishonest to yourself!

## HOMEWORK 4.4

```sh
$ mongoimport -d m101 -c profile < sysprofile.json
```

Mongo schell
```sh
use m101
db.profile.find({ns:'school2.students'}).sort({millis:-1}).limit(1).pretty()
```

Result:
"millis" : 15820