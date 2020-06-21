package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.ServerCmd;
import io.github.jrbase.handler.annotation.ServerCommand;

/**
 * Time complexity: O(N) where N is the number of patterns the client is already subscribed to.
 * h?llo subscribes to hello, hallo and hxllo
 * h*llo subscribes to hllo and heeeello
 * h[ae]llo subscribes to hello and hallo, but not hillo
 */
@ServerCommand
public class PSubscribe extends AbstractSubscribe {

    @Override
    public String handle(ClientCmd clientCmd) {
        return super.handle(clientCmd);
    }

    @Override
    void subscribe(String channel, ClientCmd clientCmd) {
        RedisChannel.psubscribe(channel, clientCmd.getRedisClientContext());
    }

    @Override
    public String getCmdName() {
        return ServerCmd.PSUBSCRIBE.getCmdName();
    }
}
