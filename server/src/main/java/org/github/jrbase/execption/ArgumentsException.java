package org.github.jrbase.execption;

import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;

public class ArgumentsException extends Throwable {

    public void handleArgumentsException(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getContext().channel();
        channel.writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
    }

}
