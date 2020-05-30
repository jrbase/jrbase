package io.github.jrbase.common.zk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jrbase.common.config.RedisConfigurationOption;
import io.github.jrbase.common.utils.ShutdownHookThread;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * find server then record a map and watch if someone server is changed
 */
public class ZooKeeperClient {
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RedisConfigurationOption config = new RedisConfigurationOption();

        CuratorFramework client = CuratorFrameworkFactory.newClient(config.getZookeeper(),
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();



        ServiceDiscovery<ServiceDetail> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceDetail.class)
                .client(client)
                .basePath(ServiceDetail.REGISTER_ROOT_PATH)
                .build();
        serviceDiscovery.start();

        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(()->{
            try {
                serviceDiscovery.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.close();
            countDownLatch.countDown();
        }));

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
            /*Collection<ServiceInstance<ServiceDetail>> services = serviceDiscovery.queryForInstances(config.getAppName());
            for (ServiceInstance<ServiceDetail> service : services) {
                System.out.println(service.getPayload().getAppName());
                System.out.println(service.getAddress() + "\t" + service.getPort());
                System.out.println("---------------------");
            }*/

        });


        countDownLatch.await();
        System.out.println("close");

    }

    private static void removeServer(PathChildrenCacheEvent event) {

    }

    private static void updateServer(PathChildrenCacheEvent event) {

    }

    private static void addServer(PathChildrenCacheEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            ServiceInstance service = mapper.readValue(new String(event.getData().getData()), ServiceInstance.class);
            System.out.println(service.getAddress() + "\t" + service.getPort());
            System.out.println("---------------------");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}
