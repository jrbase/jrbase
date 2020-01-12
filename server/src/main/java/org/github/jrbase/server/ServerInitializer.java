package org.github.jrbase.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.github.jrbase.config.RedisConfigurationOption;
import org.github.jrbase.thread.MyThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private RedisConfigurationOption redisConfigurationOption;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
            5L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(), new MyThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


    public ServerInitializer(RedisConfigurationOption redisConfigurationOption) {
        this.redisConfigurationOption = redisConfigurationOption;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // and then business logic.
        pipeline.addLast(new ServerHandler(redisConfigurationOption, threadPoolExecutor));
    }
}
