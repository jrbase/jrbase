package org.github.jrbase.proxyRheakv.rheakv

import com.alipay.sofa.jraft.rhea.options.RegionRouteTableOptions
import spock.lang.Specification

class ClientTest extends Specification {
    def "Init"() {
        Client client = new Client()
        when:
        Long regionId = -1L
        List<RegionRouteTableOptions> regionRouteTableOptionsList = client.getRegionRouteTableOptions(regionId)
        then:
        regionRouteTableOptionsList.toString() == '[RegionRouteTableOptions{regionId=-1, startKey=\'null\', startKeyBytes=null, endKey=\'null\', endKeyBytes=null, initialServerList=\'127.0.0.1:8181,127.0.0.1:8182,127.0.0.1:8183\'}]'
        when:
        def pdOpts = client.getPlacementDriverOptions(regionRouteTableOptionsList)
        then:
        pdOpts.toString() == 'PlacementDriverOptions{fake=true, cliOptions=null, pdRpcOptions=null, pdGroupId=\'null\', regionRouteTableOptionsList=[RegionRouteTableOptions{regionId=-1, startKey=\'null\', startKeyBytes=null, endKey=\'null\', endKeyBytes=null, initialServerList=\'127.0.0.1:8181,127.0.0.1:8182,127.0.0.1:8183\'}], initialServerList=\'null\', initialPdServerList=\'null\'}'
        when:
        def opts = client.getRheaKVStoreOptions(pdOpts)
        then:
        opts.toString() == 'RheaKVStoreOptions{clusterId=0, clusterName=\'rhea_example\', placementDriverOptions=PlacementDriverOptions{fake=true, cliOptions=null, pdRpcOptions=null, pdGroupId=\'null\', regionRouteTableOptionsList=[RegionRouteTableOptions{regionId=-1, startKey=\'null\', startKeyBytes=null, endKey=\'null\', endKeyBytes=null, initialServerList=\'127.0.0.1:8181,127.0.0.1:8182,127.0.0.1:8183\'}], initialServerList=\'null\', initialPdServerList=\'null\'}, storeEngineOptions=null, initialServerList=\'null\', onlyLeaderRead=true, rpcOptions=RpcOptions{callbackExecutorCorePoolSize=16, callbackExecutorMaximumPoolSize=32, callbackExecutorQueueCapacity=512, rpcTimeoutMillis=5000}, failoverRetries=2, futureTimeoutMillis=5000, useParallelKVExecutor=true, batchingOptions=BatchingOptions{allowBatching=true, batchSize=100, bufSize=8192, maxWriteBytes=32768, maxReadBytes=1024}}'
    }
}
