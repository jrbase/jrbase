package org.github.jrbase.manager;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.execption.ArgumentsException;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.IgnoreProcess;
import org.github.jrbase.process.TypeProcess;
import org.github.jrbase.process.hash.HGetProcess;
import org.github.jrbase.process.hash.HLenProcess;
import org.github.jrbase.process.hash.HSetProcess;
import org.github.jrbase.process.list.LPopProcess;
import org.github.jrbase.process.list.LPushProcess;
import org.github.jrbase.process.string.*;
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
        //Strings
        registerCmdProcess(Cmd.SET, new SetProcess());
        registerCmdProcess(Cmd.GET, new GetProcess());

        registerCmdProcess(Cmd.MSET, new MSetProcess());
        registerCmdProcess(Cmd.MGET, new MGetProcess());

        registerCmdProcess(Cmd.GETBIT, new GetBitProcess());
        registerCmdProcess(Cmd.SETBIT, new SetBitProcess());

        //Hashes
        registerCmdProcess(Cmd.HSET, new HSetProcess());
        registerCmdProcess(Cmd.HGET, new HGetProcess());
        registerCmdProcess(Cmd.HLEN, new HLenProcess());

        //Lists
        registerCmdProcess(Cmd.LPUSH, new LPushProcess());
        registerCmdProcess(Cmd.LPOP, new LPopProcess());


        //Keys
        registerCmdProcess(Cmd.TYPE, new TypeProcess());

        //others
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

            cmdProcess.checkArguments(clientCmd);

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
