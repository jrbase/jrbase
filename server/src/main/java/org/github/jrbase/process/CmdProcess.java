package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.MyKVException;

import java.util.concurrent.ExecutionException;

public interface CmdProcess {
    void process(ClientCmd clientCmd) throws MyKVException;
    void requestKVAndReplyClient() throws MyKVException;

}
