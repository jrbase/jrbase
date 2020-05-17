package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.PUBLISH;

/**
 * publish channel message
 * Time complexity: O(N+M) where N is the number of clients subscribed to the receiving channel and M is the total number of subscribed patterns (by any client).
 */
@ServerCommand
public class Publish implements ServerCmdHandler {

    @Override
    public String handle(ClientCmd clientCmd) {
        final String channel = clientCmd.getKey();
        if (clientCmd.getArgLength() != 1) {
            return "-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n";
        }
        final String message = clientCmd.getArgs()[0];
        final int size = RedisChannel.publishMessage(channel, message);
        return ":" + size + "\r\n";
    }

    @Override
    public String getCmdName() {
        return PUBLISH.getCmdName();
    }
}
