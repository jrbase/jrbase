package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.RedisClientContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Subscribe Publish
 */
public class RedisChannel {
    private static final Map<String, List<RedisClientContext>> channels = new HashMap<>();
    private static final Map<String, List<RedisClientContext>> pattenChannels = new HashMap<>();

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

    public static synchronized void psubscribe(String patten, RedisClientContext client) {
        List<RedisClientContext> subscribers = pattenChannels.get(patten);
        if (subscribers == null) {
            subscribers = new LinkedList<>();
        }
        subscribers.add(client);
        pattenChannels.put(patten, subscribers);
        redisClientChannel.put(client, patten);


    }

    public static synchronized int publishMessage(String channel, String message) {
        return handlePatten(channel, message) + handleSingleChannel(channel, message);
    }

    private static boolean isPatten(String channel) {
        return channel.contains("*") || channel.contains("?") || (channel.contains("[") && channel.contains("]"));
    }

    private static int handleSingleChannel(String channel, String message) {
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

    private static int handlePatten(String channel, String message) {
        Set<String> matchPatten = pattenChannels.keySet().stream()
                .filter(patten -> {
                    Pattern compile = Pattern.compile(patten);
                    return compile.matcher(channel).find();
                })
                .collect(Collectors.toSet());
        AtomicInteger size = new AtomicInteger();
        matchPatten.forEach(patten -> {
            List<RedisClientContext> subscribers = pattenChannels.get(patten);
            size.addAndGet(subscribers.size());
            for (RedisClientContext subscriber : subscribers) {
                String result = "*4" + "\r\n" + "$" + 8 + "\r\n" + "pmessage" + "\r\n" +
                        "$" + patten.length() + "\r\n" + patten + "\r\n" +
                        "$" + channel.length() + "\r\n" + channel + "\r\n" +
                        "$" + message.length() + "\r\n" + message + "\r\n";
                subscriber.getRedisClient().writeAndFlush(result);
            }
        });

        return size.get();
    }

    public static synchronized void unSubscribe(RedisClientContext redisClientContext) {
        doUnSubscribe(channels, redisClientContext);
    }

    public static synchronized void punSubscribe(RedisClientContext redisClientContext) {
        doUnSubscribe(pattenChannels, redisClientContext);
    }

    private static void doUnSubscribe(Map<String, List<RedisClientContext>> channels, RedisClientContext redisClientContext) {
        final String channel = redisClientChannel.get(redisClientContext);
        List<RedisClientContext> subscribers = channels.get(channel);
        if (subscribers == null || subscribers.size() == 0) {
            return;
        }
        subscribers.remove(redisClientContext);
        redisClientChannel.remove(redisClientContext);
    }

}
