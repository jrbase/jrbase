package org.github.jrbase.manager;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.MyKVException;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.GetProcess;
import org.github.jrbase.process.IgnoreProcess;
import org.github.jrbase.process.SetProcess;
import org.github.jrbase.proxyRheakv.rheakv.Client;


public class CmdManager {

    private final static Client client = new Client();

    public static Client getClient() {
        return client;
    }

    static {
        client.init();
    }

    public static void process(ClientCmd clientCmd) {

        final CmdProcess cmdProcess = clientCmdToCmdProcess(clientCmd);
        try {
            cmdProcess.process(clientCmd);
        } catch (MyKVException ignore) {
            clientCmd.getContext().channel().writeAndFlush("-rhea kv is not started or shutdown\r\n");
        }

    }

    public static CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        final Cmd cmd = Cmd.get(clientCmd.getCmd());
        CmdProcess cmdProcess;
        switch (cmd) {
            case SET: {
                cmdProcess = new SetProcess();
                break;
            }
            case GET: {
                cmdProcess = new GetProcess();
                break;
            }
            default:
                cmdProcess = new IgnoreProcess();
        }
        return cmdProcess;
    }

    public static void shutdown() {
        client.shutdown();
    }
}
