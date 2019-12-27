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
package org.github.jrbase.proxyRheakv.rheakv;

import java.io.File;

/**
 * @author jiachun.fjc
 */
public class Configs {

    public static String DB_PATH            = "rhea_db" + File.separator;

    public static String RAFT_DATA_PATH     = "raft_data" + File.separator;

    //    public static String ALL_NODE_ADDRESSES = "172.16.159.1:8181,172.16.159.128:8182,172.16.159.1:8183";
    public static String ALL_NODE_ADDRESSES = "127.0.0.1:8181,127.0.0.1:8182,127.0.0.1:8183";

    public static String CLUSTER_NAME       = "rhea_example";
}
