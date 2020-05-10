package io.github.jrbase.server;

import io.github.jrbase.config.RedisConfigurationOption;
import io.github.jrbase.handler.CmdHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final CmdHandler cmdHandler;

    public ServerInitializer(RedisConfigurationOption redisConfigurationOption) {
        this.cmdHandler = CmdHandler.newSingleInstance(redisConfigurationOption);
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then business logic.
        pipeline.addLast(new ServerHandler(cmdHandler));
    }
}
