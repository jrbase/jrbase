package org.github.jrbase.manager;

import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import io.netty.channel.Channel;
import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.annotation.ScanAnnotationConfigure;
import org.github.jrbase.proxyRheakv.rheakv.Client;

import static org.github.jrbase.utils.Tools.isCorrectKey;


public class CmdManager {

    private static Client client = new Client();
    private static ScanAnnotationConfigure scanAnnotationConfigure = ScanAnnotationConfigure.newSingleInstance();
    private static CmdManager singleInstance;

    private CmdManager() {
    }

    public static CmdManager newSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new CmdManager();
            client.init();
        }
        return singleInstance;
    }

    public RheaKVStore getRheaKVStore() {
        return client.getRheaKVStore();
    }

    public void process(ClientCmd clientCmd) {
        CmdProcess cmdProcess;
        cmdProcess = clientCmdToCmdProcess(clientCmd);
        if (cmdProcess == null) {
            cmdProcess = scanAnnotationConfigure.get(Cmd.OTHER.getCmdName());
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
        if (!message.isEmpty()) {
            clientCmd.getChannel().writeAndFlush(message);
        }
    }

    static void sendWrongArgumentMessage(ClientCmd clientCmd) {
        final Channel channel = clientCmd.getChannel();
        channel.writeAndFlush("-ERR wrong number of arguments for '" + clientCmd.getCmd() + "' command\r\n");
    }

    public CmdProcess clientCmdToCmdProcess(ClientCmd clientCmd) {
        return scanAnnotationConfigure.get(clientCmd.getCmd());
    }

    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }
}
