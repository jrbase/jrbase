## command codec specification

### strings

```
+-----------+-------------------------------------+
|  command  |               format                |
+-----------+-------------------------------------+
|    get    | get key                             |
+-----------+-------------------------------------+
|    set    | set key value [EX sec|PX ms][NX|XX] | 
+-----------+-------------------------------------+
|   getbit  | getbit key offset                   |
+-----------+-------------------------------------+
|   setbit  | setbit key offset value             |
+-----------+-------------------------------------+
|    del    | del key1 key2 ...                   |
+-----------+-------------------------------------+
|    mget   | mget key1 key2 ...                  |
+-----------+-------------------------------------+
|    mset   | mset key1 value1 key2 value2 ...    |
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
```

### list:
```
LPUSH
RPUSH 
LPOP
RPOP
LINDEX
LLEN
```
###   ziplist  hashtable
```
HSET
HGET
HEXISTS
HDEL
HLEN
HGETALL
```

###  intset  hashtable
```
SADD
SPOP
```

### ziplist skiplist  with score

```
ZADD
ZCARD
ZCOUNT
ZRANGE
ZREVRANGE
ZRANK
ZREVRANK
ZREM
ZSCORE
```
eg.

ZADD price 8.5 apple 5.0 banana 6.0 cherry

## Replication
## Partitioning
## Transactions
























