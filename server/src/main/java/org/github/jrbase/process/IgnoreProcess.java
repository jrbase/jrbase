package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;

public class IgnoreProcess implements CmdProcess {

    @Override
    public void process(ClientCmd clientCmd) {
        clientCmd.getContext().channel().writeAndFlush("+Command not implement: " + clientCmd.getCmd() + "\r\n");
    }

}
