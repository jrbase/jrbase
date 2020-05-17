package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.RedisClientContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Subscribe Publish
 */
public class RedisChannel {
    private static final Map<String, List<RedisClientContext>> channels = new HashMap<>();

    private static final Map<RedisClientContext, String> redisClientChannel = new HashMap<>();

    public static synchronized void subscribe(String channel, RedisClientContext client) {
        List<RedisClientContext> subscribers = channels.get(channel);
        if (subscribers == null) {
            subscribers = new LinkedList<>();
        }
        subscribers.add(client);
        channels.put(channel, subscribers);
        redisClientChannel.put(client, channel);
    }

    public static synchronized int publishMessage(String channel, String message) {
        List<RedisClientContext> subscribers = channels.get(channel);
        if (subscribers == null || subscribers.size() == 0) {
            return 0;
        }
        for (RedisClientContext subscriber : subscribers) {
            String result = "*3" + "\r\n" + "$" + 7 + "\r\n" + "message" + "\r\n" +
                    "$" + channel.length() + "\r\n" + channel + "\r\n" +
                    "$" + message.length() + "\r\n" + message + "\r\n";
            subscriber.getRedisClient().writeAndFlush(result);
        }
        return subscribers.size();
    }

    public static synchronized void unSubscribe(RedisClientContext redisClientContext) {
        final String channel = redisClientChannel.get(redisClientContext);
        List<RedisClientContext> subscribers = channels.get(channel);
        if (subscribers == null || subscribers.size() == 0) {
            return;
        }
        subscribers.remove(redisClientContext);
        redisClientChannel.remove(redisClientContext);
    }
}
