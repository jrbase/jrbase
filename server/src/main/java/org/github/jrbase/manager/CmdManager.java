package org.github.jrbase.manager;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.dataType.Cmd;
import org.github.jrbase.process.CmdProcess;
import org.github.jrbase.process.GetProcess;
import org.github.jrbase.process.SetProcess;

public class CmdManager {
    public static void process(ClientCmd clientCmd) {
        final Cmd cmd = Cmd.get(clientCmd.getCmd());
        CmdProcess cmdProcess = null;
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
                throw new RuntimeException("error cmd");
        }
        //
        cmdProcess.process(clientCmd);
        cmdProcess.requestKV();
        cmdProcess.replyClient();


    }
}
