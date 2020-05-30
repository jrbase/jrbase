package io.github.jrbase.common.zk;

import io.github.jrbase.common.config.RedisConfigurationOption;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ZKServerRegister {

    private CuratorFramework client;
    private ServiceDiscovery<ServiceDetail> serviceDiscovery;
    private RedisConfigurationOption config;

    public ZKServerRegister(RedisConfigurationOption config){
        this.config = config;
    }

    public void register(){

        client = CuratorFrameworkFactory.newClient(this.config.getZookeeper(),
                new ExponentialBackoffRetry(1000, 3));

        client.start();
        try {
            client.blockUntilConnected();

            ServiceInstanceBuilder<ServiceDetail> sib = ServiceInstance.builder();
            sib.address(this.config.getRegisterAddress());
            sib.port(this.config.getPort());
            sib.name(this.config.getAppName());
            sib.payload(new ServiceDetail(this.config.getAppName()));

            ServiceInstance<ServiceDetail> instance = sib.build();

            serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceDetail.class)
                    .client(client)
                    .serializer(new JsonInstanceSerializer<>(ServiceDetail.class))
                    .basePath(ServiceDetail.REGISTER_ROOT_PATH)
                    .build();
            //服务注册
            serviceDiscovery.registerService(instance);
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
            unRegister();
        }

    }

    public void unRegister(){
        try {
            serviceDiscovery.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        RedisConfigurationOption config = new RedisConfigurationOption();
        ZKServerRegister zkServerRegister = new ZKServerRegister(config);
        zkServerRegister.register();
        TimeUnit.SECONDS.sleep(10);
        zkServerRegister.unRegister();

    }
}
