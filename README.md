[![Build Status](https://travis-ci.org/jrbase/jrbase.svg?branch=master)](https://travis-ci.org/jrbase/jrbase)
[![codecov](https://codecov.io/gh/jrbase/jrbase/branch/master/graph/badge.svg)](https://codecov.io/gh/jrbase/jrbase)
[![CircleCI](https://circleci.com/gh/jrbase/jrbase.svg?style=svg)](https://circleci.com/gh/jrbase/jrbase)
## What is jrbase?

`jrbase` is a distributed NoSQL database similar to Redis.

`jrbase` implement redis server protocol, powered by `custom kv server` or [jraft-rheakv](https://github.com/sofastack/sofa-jraft/tree/master/jraft-rheakv) backend.

## Architecture
![Architecture](./wiki/images/architecture.png)
![Architecture](./wiki/images/architecture2.png)

### Plan supported commands

```
Strings
+-----------+-----------------------+-------------------------------------
|  command  |     supported         |        format                         
|           | (Y=yes,N=no, D=doing) |                               
+-----------+----------------+------+-------------------------------------
|    get    |           Y           | get key         
+-----------+-----------------------+-------------------------------------
|    set    |           D           | set key value [EX sec|PX ms][NX|XX]                     
+-----------+-----------------------+------------------------------------
|    mget   |           Y           | MGET key [key ...]              
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
+-----------+-------------------------+-----------------------+-------------------------
|  command  |     supported           |               format                          
|           |(  Y=yes,N=no, D=doing)  |                                               
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
|    lpop    |         Y               | lpop key                 
+------------+-------------------------+-------------------------------------------------
|    rpop    |         Y               | rpop key                 
+------------+-------------------------+-------------------------------------------------
|   lrange   |         Y               | lrange key start stop                 
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
+-------------+-------------------------+-----------------------+-------------------------
|  command    |        supported        |               format                          
|             |  (Y=yes,N=no, D=doing)  |                                               
+-------------+-------------------------+-------------------------------------------------
|    sadd     |           Y             | sadd key member [member ...]        
+-------------+-------------------------+-------------------------------------------------
|    spop     |           Y             | spop key [count]                  
+-------------+-------------------------+-------------------------------------------------
|  scard      |           Y             | hget key                   
+-------------+-------------------------+-------------------------------------------------
| smembers    |                         | smembers key                   
+-------------+-------------------------+-------------------------------------------------
| sismember   |                         | sismembers key mmember      
+-------------+-------------------------+-------------------------------------------------
|srandmember  |                         | srandmembers key [count]                  
+-------------+-------------------------+-------------------------------------------------
|   sdiff     |                         | sdiff key [key ...]                
+-------------+-------------------------+-------------------------------------------------

Sorted Sets
+------------------+-------------------------+-----------------------+-------------------------
|  command         |        supported        |               format                          
|                  |  (Y=yes,N=no, D=doing)  |                                               
+------------------+-------------------------+-------------------------------------------------
|      ZADD        |           D             | ZADD key [NX|XX] [CH] [INCR] score member [score member ...]   
+------------------+-------------------------+-------------------------------------------------
|    ZRANGE        |                         | ZRANGE key start stop [WITHSCORES]  
+------------------+-------------------------+-------------------------------------------------
|    ZREVRANGE     |                         | ZREVRANGE key start stop [WITHSCORES]    
+------------------+-------------------------+-------------------------------------------------
|   ZRANGEBYSCORE  |                         | ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]     
+------------------+-------------------------+-------------------------------------------------
| ZREMRANGEBYSCORE |                         | ZREMRANGEBYSCORE key min max                  
+------------------+-------------------------+-------------------------------------------------
|      ZRANK       |                         | ZRANK key [count]                  
+------------------+-------------------------+-------------------------------------------------
|   ZRANGEBYLEX    |                         | ZRANGEBYLEX key min max [LIMIT offset count]   
+------------------+-------------------------+-------------------------------------------------
|     ZINCRBY      |                         | ZINCRBY key increment member       
+------------------+-------------------------+-------------------------------------------------
|      ZREM        |                         | ZREM key member [member ...]                
+------------------+-------------------------+-------------------------------------------------
|    ZINTERSTORE   |                         | smembers key                           
+------------------+-------------------------+-------------------------------------------------
|    ZLEXCOUNT     |                         | smembers key                       
+------------------+-------------------------+-------------------------------------------------
|     ZPOPMAX      |                         | ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]                
+------------------+-------------------------+-------------------------------------------------
|     ZPOPMIN      |                         | ZPOPMIN key [count]                  
+------------------+-------------------------+-------------------------------------------------
| ZREMRANGEBYLEX   |                         | ZREMRANGEBYLEX key min max                  
+------------------+-------------------------+-------------------------------------------------
| ZREMRANGEBYRANK  |                         | ZREMRANGEBYRANK key start stop                 
+------------------+-------------------------+-------------------------------------------------
| ZREVRANGEBYLEX   |                         | ZREVRANGEBYLEX key max min [LIMIT offset count]                
+------------------+-------------------------+-------------------------------------------------
| ZREVRANGEBYSCORE |                         | ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]                
+------------------+-------------------------+-------------------------------------------------
|     ZREVRANK     |                         | ZREVRANK key member                
+------------------+-------------------------+-------------------------------------------------
|     ZSCAN        |                         | ZSCAN key cursor [MATCH pattern] [COUNT count]               
+------------------+-------------------------+-------------------------------------------------
|      ZSCORE      |                         | ZSCORE key member               
+------------------+-------------------------+-------------------------------------------------
|  ZUNIONSTORE     |                         | ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]                 


Keys
+-------------+-------------------------+-----------------------+-------------------------
|  command    |    supported            |               format                          
|             |(Y=yes,N=no, D=doing)    |                                               
+-------------+-------------------------+-------------------------------------------------
|   del       |                         | del key              
+-------------+-------------------------+-------------------------------------------------
|   type      |                         | type key              
+-------------+-------------------------+-------------------------------------------------


Geo
+-------------+-------------------------+-----------------------+-------------------------
|  command    |    supported            |               format                          
|             |(Y=yes,N=no, D=doing)    |                                               
+-------------+-------------------------+-------------------------------------------------
|   geoadd    |                         | geoadd key longitude latitude member [longitude latitude member ...]        
+-------------+-------------------------+-------------------------------------------------
|   geopos    |                         | geopos key  member [member ...]            
+-------------+-------------------------+-------------------------------------------------
|   geodist   |                         | geodist key member1 member2 [m|km|ft|mi]         
+-------------+-------------------------+-------------------------------------------------
|   geohash   |                         | geohash key member [member ...]       
+-------------+-------------------------+-------------------------------------------------
| georadius   |                         | georadius key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC] [STORE key] [STOREDIST key]         
+-------------+-------------------------+-------------------------------------------------
| georadiusbymember |                   | georadiusbymember key member radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC] [STORE key] [STOREDIST key]
+-------------+-------------------------+-------------------------------------------------


Keys
+-------------+-------------------------+-----------------------+-------------------------
|  command    |    supported            |               format                          
|             |(Y=yes,N=no, D=doing)    |                                               
+-------------+-------------------------+-------------------------------------------------
|   del       |                         | del key [key ...]             
+-------------+-------------------------+-------------------------------------------------
|   type      |                         | type key              
+-------------+-------------------------+-------------------------------------------------
|   exists    |                         | exists key [key ...]           
+-------------+-------------------------+-------------------------------------------------
|   keys      |                         | keys pattern           
+-------------+-------------------------+-------------------------------------------------


connection
+-------------+-------------------------+-----------------------+-------------------------
|  command    |    supported            |               format                          
|             |(Y=yes,N=no, D=doing)    |                                               
+-------------+-------------------------+-------------------------------------------------
|   auth      |           Y             |     auth password             
+-------------+-------------------------+-------------------------------------------------
|   echo      |           Y             |     echo message       | Bulk string reply     
+-------------+-------------------------+-------------------------------------------------
|   ping      |           Y             |     ping [message]             
+-------------+-------------------------+-------------------------------------------------
|   quit      |                         |     quit           
+-------------+-------------------------+-------------------------------------------------
|   select    |                         |     select index          
+-------------+-------------------------+-------------------------------------------------
|   swapdb    |                         |     swapdb index1 index2            
+-------------+-------------------------+-------------------------------------------------


Server
+-------------+-------------------------+---------------------------------+---------------
|  command    |    supported            |               format            |              
|             |(Y=yes,N=no, D=doing)    |                                 |    return value         
+-------------+-------------------------+-------------------------------------------------
|   flushall  |                         |     flushall [async]            |  Simple string reply
+-------------+-------------------------+-------------------------------------------------
|   flushdb   |                         |     flushdb [async]             |  Simple string reply
+-------------+-------------------------+-------------------------------------------------
|   info      |                         |     info [section]              |  Bulk string reply
+-------------+-------------------------+-------------------------------------------------
|   command   |                         |     command -          
+-------------+-------------------------+-------------------------------------------------
|   command   |                         |     command  count         
+-------------+-------------------------+-------------------------------------------------


Transactions
Streams
Cluster
HyperLogLog
Pub/Sub
Scripting


```


## Related Links
[redis commands](https://redis.io/commands/)

[A persistent key-value store for fast storage environments](https://rocksdb.org/)

[The Raft Consensus Algorithm](https://raft.github.io/)

[6.824 Schedule: Spring 2020](https://pdos.csail.mit.edu/6.824/schedule.html)

[CMU 15-721](https://15721.courses.cs.cmu.edu/spring2020/)

[use tikv to build distrubuted redis service](https://pingcap.com/blog-cn/use-tikv-to-build-distributed-redis-service/)

[reflections](https://code.google.com/archive/p/reflections/)

