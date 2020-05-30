package io.github.jrbase.common.zk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jrbase.common.config.RedisConfigurationOption;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ZKServerRegister {

    private Map<String, RedisConfigurationOption> router = new HashMap<>();

    private CuratorFramework client;
    private ServiceDiscovery<ServiceDetail> serviceDiscovery;
    private RedisConfigurationOption config;

    public ZKServerRegister(RedisConfigurationOption config) {
        this.config = config;
    }

    public void register() {

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

        try {
            addEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addEvent() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/" + ServiceDetail.REGISTER_ROOT_PATH + "/" + config.getAppName(), true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener((client1, event) -> {
            System.out.println("事件类型：" + event.getType() + "；操作节点：" + event.getData().getPath());
            switch (event.getType()) {
                case CHILD_ADDED: {
                    addServer(event);
                    break;
                }
                case CHILD_UPDATED: {
                    updateServer(event);
                    break;
                }
                case CHILD_REMOVED: {
                    removeServer(event);
                    break;
                }
                default: {

                }
            }

        });
    }

    private void removeServer(PathChildrenCacheEvent event) {
        router.remove(event.getData().getPath());
    }

    private void updateServer(PathChildrenCacheEvent event) {
        RedisConfigurationOption config = router.get(event.getData().getPath());
        if (config != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                ServiceInstance service = mapper.readValue(new String(event.getData().getData()), ServiceInstance.class);
                config.setBind(service.getAddress());
                config.setPort(service.getPort());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            addServer(event);
        }
    }

    private void addServer(PathChildrenCacheEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ServiceInstance service = mapper.readValue(new String(event.getData().getData()), ServiceInstance.class);
            System.out.println(service.getAddress() + "\t" + service.getPort());
            router.put(event.getData().getPath(), new RedisConfigurationOption(service.getAddress(), service.getPort()));
            System.out.println("---------------------");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    public void unRegister() {
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
