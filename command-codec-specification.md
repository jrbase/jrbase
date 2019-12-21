## command codec specification

###  think

client first split string to command key value
value can be (embstr,int,raw), list(), set, sort set 
 
### string

```
set 
get
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
























