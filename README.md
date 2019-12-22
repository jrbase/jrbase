## What is jrbase?

jrbase is a Distributed NoSQL database like redis.

jrbase implement redis server protocol, powered by [jraft-rheakv](https://github.com/sofastack/sofa-jraft/tree/master/jraft-rheakv) backend.

## Features

### plan supported commands

#### Strings
```
+-----------+-------------------------------------+---------------------------------------
|  command  |               format                |    supported (Y=yes,N=no, D=doing)
+-----------+-------------------------------------+---------------------------------------
|    get    | get key                             |    Y
+-----------+-------------------------------------+
|    mget   | mget key1 key2 ...                  |    Y
+-----------+-------------------------------------+
|    set    | set key value [EX sec|PX ms][NX|XX] |    D
+-----------+-------------------------------------+
|    mset   | mset key1 value1 key2 value2 ...    |
+-----------+-------------------------------------+
|   getbit  | getbit key offset                   |    
+-----------+-------------------------------------+
|   setbit  | setbit key offset value             |
+-----------+-------------------------------------+
|    del    | del key1 key2 ...                   |
+-----------+-------------------------------------+
|    incr   | incr key                            |
+-----------+-------------------------------------+
|   incrby  | incr key step                       |
+-----------+-------------------------------------+
|    decr   | decr key                            |
+-----------+-------------------------------------+
|   decrby  | decrby key step                     |
+-----------+-------------------------------------+
|   strlen  | strlen key                          |
+-----------+-------------------------------------+
|  pexpire  | pexpire key int                     |
+-----------+-------------------------------------+
| pexpireat | pexpireat key timestamp(ms)         |
+-----------+-------------------------------------+
|   expire  | expire key int                      |
+-----------+-------------------------------------+
|  expireat | expireat key timestamp(s)           |
+-----------+-------------------------------------+
|    pttl   | pttl key                            |
+-----------+-------------------------------------+
|    ttl    | ttl key                             |
+-----------+-------------------------------------+


Hashes
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