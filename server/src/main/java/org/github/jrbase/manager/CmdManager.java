package org.github.jrbase.manager;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.IgnoreProcess;
import org.github.jrbase.process.TypeProcess;
import org.github.jrbase.process.hash.HGetProcess;
import org.github.jrbase.process.hash.HLenProcess;
import org.github.jrbase.process.hash.HSetProcess;
import org.github.jrbase.process.list.*;
import org.github.jrbase.process.sets.SAddProcess;
import org.github.jrbase.process.sets.SCardProcess;
import org.github.jrbase.process.sets.SPopProcess;
import org.github.jrbase.process.string.*;
import org.github.jrbase.proxyRheakv.rheakv.Client;

import java.util.HashMap;
import java.util.Map;

import static org.github.jrbase.utils.Tools.isCorrectKey;


public class CmdManager {

    private CmdManager() {
        throw new UnsupportedOperationException();
    }

    private static Client client = null;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
            client.init();
        }
        return client;
    }

    public static RheaKVStore getRheaKVStore() {
        return getClient().getRheaKVStore();
    }

    private static Map<String, CmdProcess> cmdProcessManager = new HashMap<>();

    public static Map<String, CmdProcess> getCmdProcessManager() {
        return cmdProcessManager;
    }

    static {
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
        registerCmdProcess(Cmd.LRANGE, new LRangeProcess());
        registerCmdProcess(Cmd.RPUSH, new RPushProcess());
        registerCmdProcess(Cmd.RPOP, new RPopProcess());

        //Sets
        registerCmdProcess(Cmd.SADD, new SAddProcess());
        registerCmdProcess(Cmd.SPOP, new SPopProcess());
        registerCmdProcess(Cmd.SCARD, new SCardProcess());


        //Keys
        registerCmdProcess(Cmd.TYPE, new TypeProcess());

        //others
        registerCmdProcess(Cmd.OTHER, new IgnoreProcess());
    }

    private static void registerCmdProcess(Cmd cmd, CmdProcess cmdProcess) {
        cmdProcessManager.put(cmd.getCmdName(), cmdProcess);
    }

    public static void process(ClientCmd clientCmd) {
        CmdProcess cmdProcess = clientCmdToCmdProcess(clientCmd);
        if (cmdProcess == null) {
            cmdProcess = cmdProcessManager.get(Cmd.OTHER.getCmdName());
        }
        // check key
        if (!isCorrectKey(clientCmd.getKey())) {
            sendWrongArgumentMessage(clientCmd);
            return;
        }
        // check arguments
        if (!cmdProcess.isCorrectArguments(clientCmd)) {
            sendWrongArgumentMessage(clientCmd);
            return;
        }

        final String message = cmdProcess.process(clientCmd);
        clientCmd.getChannel().writeAndFlush(message);
    }

    static void sendWrongArgumentMessage(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
    }

    public static CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        return cmdProcessManager.get(clientCmd.getCmd());
    }

    public static void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }
}
