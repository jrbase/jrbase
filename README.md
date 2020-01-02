[![Build Status](https://travis-ci.org/jrbase/jrbase.svg?branch=master)](https://travis-ci.org/jrbase/jrbase)

[![CircleCI](https://circleci.com/gh/jrbase/jrbase.svg?style=svg)](https://circleci.com/gh/jrbase/jrbase)
## What is jrbase?

jrbase is a Distributed NoSQL database like redis.

jrbase implement redis server protocol, powered by [jraft-rheakv](https://github.com/sofastack/sofa-jraft/tree/master/jraft-rheakv) backend.

## Features

### plan supported commands

#### Strings
```
+-----------+-----------------------+-------------------------------------
|  command  |     supported         |        format                         
|           | (Y=yes,N=no, D=doing) |                               
+-----------+----------------+------+-------------------------------------
|    get    |           Y           |     get key         
+-----------+-----------------------+-------------------------------------
|    set    |           Y           | set key value [EX sec|PX ms][NX|XX]                     
+-----------+-----------------------+------------------------------------
|    mget   |           Y           | mget key1 key2 ...                 
+-----------+-----------------------+------------------------------------
|    mset   |           Y           | mset key1 value1 key2 value2 ...    
+-----------+-----------------------+-------------------------------------
|   getbit  |           Y           | getbit key offset                      
+-----------+-----------------------+-------------------------------------
|   setbit  |           Y           | setbit key offset value             
+-----------+-----------------------+-------------------------------------
|    del    |                       | del key1 key2 ...                   
+-----------+-----------------------+-------------------------------------
|    incr   |                       | incr key                            
+-----------+-----------------------+-------------------------------------
|   incrby  |                       | incr key step                       
+-----------+-----------------------+-------------------------------------
|    decr   |                       | decr key                            
+-----------+-----------------------+-------------------------------------
|   decrby  |                       | decrby key step                     
+-----------+-----------------------+-------------------------------------
|   strlen  |                       | strlen key                          
+-----------+-----------------------+-------------------------------------
|  pexpire  |                       | pexpire key int                     
+-----------+-----------------------+-------------------------------------
| pexpireat |                       | pexpireat key timestamp(ms)         
+-----------+-----------------------+-------------------------------------
|   expire  |                       | expire key int                      
+-----------+-----------------------+-------------------------------------
|  expireat |                       | expireat key timestamp(s)           
+-----------+-----------------------+-------------------------------------
|    pttl   |                       | pttl key                            
+-----------+-----------------------+-------------------------------------
|    ttl    |                       | ttl key                             
+-----------+-----------------------+-------------------------------------


Hashes
+-------------------------------------+-----------------------+-------------------------
|  command  |    supported            |               format                          
|           |(Y=yes,N=no, D=doing)    |                                               
+-----------+-------------------------+-------------------------------------------------
|    hset   |         Y               | hset key field1 value1 field2 value2 ...         
+-----------+-------------------------+-------------------------------------------------
|    hget   |         Y               | hget key field1|field2|...                    
+-----------+-------------------------+-------------------------------------------------
|    hlen   |         Y               | hlen key                   
+-----------+-------------------------+-------------------------------------------------

Lists
+------------+-------------------------+-----------------------+-------------------------
|  command   |      supported          |               format                          
|            |  (Y=yes,N=no, D=doing)  |                                               
+------------+-------------------------+-------------------------------------------------
|    lpush   |         Y               | lpush key value1 value2 value3...      
+------------+-------------------------+-------------------------------------------------
|    rpush   |         Y               | rpush key value1 value2 value3...                     
+------------+-------------------------+-------------------------------------------------
|    lpop    |         Y               |  lpop key                 
+------------+-------------------------+-------------------------------------------------
|    rpop    |         Y               |  rpop key                 
+------------+-------------------------+-------------------------------------------------
|   lrange   |         Y               |  lrange key start stop                 
+------------+-------------------------+-------------------------------------------------
|   lindex   |                         |                   
+------------+-------------------------+-------------------------------------------------
|  rpoplpush |                         |                   
+------------+-------------------------+-------------------------------------------------
|  ltrim     |                         |                   
+------------+-------------------------+-------------------------------------------------
|   blpop    |                         |                   
+------------+-------------------------+-------------------------------------------------
| brpoplpush |                         |                   
+------------+-------------------------+-------------------------------------------------


Sets
Sorted Sets

Keys
+------------+-------------------------+-------------------------------------------------
|   type     |                         |     type key              
+------------+-------------------------+-------------------------------------------------

Connection
Geo
Transactions
Streams
Cluster
HyperLogLog
Pub/Sub
Scripting
Server

```
## Architecture


## Others

[use tikv to build distrubuted redis service](https://pingcap.com/blog-cn/use-tikv-to-build-distributed-redis-service/)
