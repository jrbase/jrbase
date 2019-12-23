package org.github.jrbase.manager;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.*;
import org.github.jrbase.proxyRheakv.rheakv.Client;

import java.util.HashMap;
import java.util.Map;


public class CmdManager {

    private CmdManager() {
    }

    private final static Client client = new Client();

    public static Client getClient() {
        return client;
    }

    private static Map<Cmd, CmdProcess> cmdProcessManager = new HashMap<>();

    static {
        client.init();

        registerCmdProcess(Cmd.SET, new SetProcess());
        registerCmdProcess(Cmd.MSET, new MSetProcess());
        registerCmdProcess(Cmd.GET, new GetProcess());
        registerCmdProcess(Cmd.MGET, new MGetProcess());
        registerCmdProcess(Cmd.HSET, new HSetProcess());
        registerCmdProcess(Cmd.HGET, new HGetProcess());

        registerCmdProcess(Cmd.OTHER, new IgnoreProcess());
    }

    private static void registerCmdProcess(Cmd cmd, CmdProcess cmdProcess) {
        cmdProcessManager.put(cmd, cmdProcess);
    }

    public static void process(ClientCmd clientCmd) {
        final CmdProcess cmdProcess = clientCmdToCmdProcess(clientCmd);
        // handle key is empty besides
        if (clientCmd.getKey().isEmpty() && !(cmdProcess instanceof IgnoreProcess)) {
            clientCmd.getContext().channel().writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
            return;
        }
        cmdProcess.process(clientCmd);

    }

    public static CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        final Cmd cmd = Cmd.get(clientCmd.getCmd());
        return cmdProcessManager.get(cmd);
    }

    public static void shutdown() {
        client.shutdown();
    }
}
