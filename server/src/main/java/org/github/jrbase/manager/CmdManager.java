package org.github.jrbase.manager;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.process.*;
import org.github.jrbase.proxyRheakv.rheakv.Client;

import java.util.HashMap;
import java.util.Map;

import static org.github.jrbase.utils.Tools.checkKey;


public class CmdManager {

    private CmdManager() {
    }

    private final static Client client = new Client();

    public static Client getClient() {
        return client;
    }

    public static RheaKVStore getRheaKVStore() {
        return client.getRheaKVStore();
    }

    private static Map<Cmd, CmdProcess> cmdProcessManager = new HashMap<>();

    static {
        client.init();

        registerCmdProcess(Cmd.SET, new SetProcess());
        registerCmdProcess(Cmd.GET, new GetProcess());

        registerCmdProcess(Cmd.MSET, new MSetProcess());
        registerCmdProcess(Cmd.MGET, new MGetProcess());

        registerCmdProcess(Cmd.HSET, new HSetProcess());
        registerCmdProcess(Cmd.HGET, new HGetProcess());
        registerCmdProcess(Cmd.HLEN, new HLenProcess());

        registerCmdProcess(Cmd.GETBIT, new GetBitProcess());
        registerCmdProcess(Cmd.SETBIT, new SetBitProcess());

        registerCmdProcess(Cmd.OTHER, new IgnoreProcess());
    }

    private static void registerCmdProcess(Cmd cmd, CmdProcess cmdProcess) {
        cmdProcessManager.put(cmd, cmdProcess);
    }

    public static void process(ClientCmd clientCmd) {
        final CmdProcess cmdProcess = clientCmdToCmdProcess(clientCmd);
        try {
            checkKey(clientCmd.getKey());

            final RheaKVStore rheaKVStore = CmdManager.getRheaKVStore();
            clientCmd.setRheaKVStore(rheaKVStore);

            final String message = cmdProcess.process(clientCmd);
            clientCmd.getContext().channel().writeAndFlush(message);
        } catch (ArgumentsException argumentsException) {
            argumentsException.handleArgumentsException(clientCmd);
        }

    }

    public static CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        final Cmd cmd = Cmd.get(clientCmd.getCmd());
        return cmdProcessManager.get(cmd);
    }

    public static void shutdown() {
        client.shutdown();
    }
}
