/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jrbase.proxyRheakv.rheakv;

import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.PlacementDriverOptions;
import com.alipay.sofa.jraft.rhea.options.RegionRouteTableOptions;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rhea.options.configured.MultiRegionRouteTableOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.PlacementDriverOptionsConfigured;
import com.alipay.sofa.jraft.rhea.options.configured.RheaKVStoreOptionsConfigured;

import java.util.List;

/**
 * @author jiachun.fjc
 */
public class Client {

    private final RheaKVStore backendProxy = new DefaultRheaKVStore();

    public void init() {
        Long regionId = -1L;
        final List<RegionRouteTableOptions> regionRouteTableOptionsList = getRegionRouteTableOptions(regionId);
        final PlacementDriverOptions pdOpts = getPlacementDriverOptions(regionRouteTableOptionsList);
        final RheaKVStoreOptions opts = getRheaKVStoreOptions(pdOpts);
        backendProxy.init(opts);
    }

    List<RegionRouteTableOptions> getRegionRouteTableOptions(Long regionId) {
        return MultiRegionRouteTableOptionsConfigured
                .newConfigured()
                .withInitialServerList(regionId, Configs.ALL_NODE_ADDRESSES)
                .config();
    }

    PlacementDriverOptions getPlacementDriverOptions(List<RegionRouteTableOptions> regionRouteTableOptionsList) {
        return PlacementDriverOptionsConfigured.newConfigured()
                .withFake(true)
                .withRegionRouteTableOptionsList(regionRouteTableOptionsList)
                .config();
    }

    RheaKVStoreOptions getRheaKVStoreOptions(PlacementDriverOptions pdOpts) {
        return RheaKVStoreOptionsConfigured.newConfigured()
                .withClusterName(Configs.CLUSTER_NAME)
                .withPlacementDriverOptions(pdOpts)
                .config();
    }

    public void shutdown() {
        this.backendProxy.shutdown();
    }

    public RheaKVStore getRheaKVStore() {
        return backendProxy;
    }
}
