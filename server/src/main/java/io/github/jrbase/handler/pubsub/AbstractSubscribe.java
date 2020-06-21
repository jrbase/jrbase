package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubscribe implements ServerCmdHandler {

    // Template pattern
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
            subscribe(ch, clientCmd);
        }
        return result.toString();
    }

    /**
     * subscribe and psubscribe
     *
     * @param channel   channel
     * @param clientCmd clientCmd
     */
    abstract void subscribe(String channel, ClientCmd clientCmd);
}
