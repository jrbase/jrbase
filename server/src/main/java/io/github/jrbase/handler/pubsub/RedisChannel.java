package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.RedisClientContext;

import java.util.*;
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
        List<RedisClientContext> pattenSubscribers = handlePatten(channel);
        List<RedisClientContext> subscribers = channels.get(channel);
        if (subscribers != null) {
            pattenSubscribers.addAll(subscribers);
        }
        for (RedisClientContext subscriber : pattenSubscribers) {
            String result = "*3" + "\r\n" + "$" + 7 + "\r\n" + "message" + "\r\n" +
                    "$" + channel.length() + "\r\n" + channel + "\r\n" +
                    "$" + message.length() + "\r\n" + message + "\r\n";
            subscriber.getRedisClient().writeAndFlush(result);
        }
        return pattenSubscribers.size();
    }

    private static List<RedisClientContext> handlePatten(String channel) {
        Set<String> matchPatten = pattenChannels.keySet().stream()
                .filter(patten -> {
                    Pattern compile = Pattern.compile(patten);
                    return compile.matcher(channel).find();
                })
                .collect(Collectors.toSet());
        List<RedisClientContext> result = new ArrayList<>();
        matchPatten.forEach(item -> {
            List<RedisClientContext> redisClientContexts = pattenChannels.get(item);
            result.addAll(redisClientContexts);
        });
        return result;
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
