## Home

### Performance
1. async rpc for performance improvement

use java.util.concurrent.CompletableFuture#whenComplete

`redis-benchmark -t get -n 100,000`
16638.94 requests per second => 40128.41 requests per second

`redis-benchmark -t set -n 10,000`
487.85 requests per second => 3042.29 requests per second

2. Replace jraftKV Server with Memory
`redis-benchmark -t get -n 100,000`
40128.41 requests per second => 127713.92 requests per second
`redis-benchmark -t set -n 10,000`
3042.29 requests per second => 126742.72 requests per second


### -r can get random keys
`redis-benchmark -t get -n 100000 -r 9999999999`
128369.71 requests per second
`redis-benchmark -t set -n 100000 -r 9999999999`
121065.38 requests per second

## debug
gradle test --debug-jvm