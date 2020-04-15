## Home

### Performance
1. async rpc for performance improvement

use java.util.concurrent.CompletableFuture#whenComplete

`redis-benchmark -t get -n 100,000`
16638.94 requests per second => 40128.41 requests per second

`redis-benchmark -t set -n 10,000`
487.85 requests per second => 3042.29 requests per second