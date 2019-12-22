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
|   getbit  |                       | getbit key offset                      
+-----------+-----------------------+-------------------------------------
|   setbit  |                       | setbit key offset value             
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
+----------------------+--------------+-----------------------+-------------------------
|  command  |    supported            |               format                          
|           |(Y=yes,N=no, D=doing)    |                                               
+-----------+-------------------------+-------------------------------------------------
|    hset   |         Y               | hset key field1 value1 field2 value2 ...         
+-----------+-------------------------+-------------------------------------------------
|    hget   |         Y               | hget key field1|field2|                    
+-----------+-------------------------+-------------------------------------------------

Lists
Sets
Sorted Sets
Keys
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