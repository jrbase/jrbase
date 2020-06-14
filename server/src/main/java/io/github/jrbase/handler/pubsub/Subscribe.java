package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.jrbase.dataType.ServerCmd.SUBSCRIBE;

/**
 * subscribe channel [channel ...]
 * Time complexity: O(N) where N is the number of channels to subscribe to.
 */
@ServerCommand
public class Subscribe implements ServerCmdHandler {

    @Override
    public String handle(ClientCmd clientCmd) {
        List<String> channels = new ArrayList<>();
        final String channel = clientCmd.getKey();
        channels.add(channel);
        channels.addAll(Arrays.asList(clientCmd.getArgs()));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < channels.size(); i++) {
            final String ch = channels.get(i);
            result.append("*").append(3).append("\r\n")
                    .append("$").append(9).append("\r\n").append("subscribe").append("\r\n")
                    .append("$").append(ch.length()).append("\r\n").append(ch).append("\r\n")
                    .append(":").append(i + 1).append("\r\n");
            RedisChannel.subscribe(ch, clientCmd.getRedisClientContext());
        }
        return result.toString();
    }

    @Override
    public String getCmdName() {
        return SUBSCRIBE.getCmdName();
    }
}
