package io.github.jrbase.handler.pubsub;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.handler.annotation.ServerCommand;

import static io.github.jrbase.dataType.ServerCmd.SUBSCRIBE;

/**
 * subscribe channel [channel ...]
 * Time complexity: O(N) where N is the number of channels to subscribe to.
 */
@ServerCommand
public class Subscribe extends AbstractSubscribe {

    @Override
    public String handle(ClientCmd clientCmd) {
        return super.handle(clientCmd);
    }

    @Override
    void subscribe(String channel, ClientCmd clientCmd) {
        RedisChannel.subscribe(channel, clientCmd.getRedisClientContext());
    }

    @Override
    public String getCmdName() {
        return SUBSCRIBE.getCmdName();
    }
}
