package io.github.jrbase.execption;

import io.github.jrbase.dataType.ClientCmd;
import io.netty.channel.Channel;

public class ArgumentsException extends Throwable {

    public void handleArgumentsException(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
    }

}
