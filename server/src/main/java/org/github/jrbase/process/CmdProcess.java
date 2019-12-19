package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;

public interface CmdProcess {
    default void process(ClientCmd clientCmd){
        requestKV();
        replyClient();
    }
    void requestKV();
    void replyClient();
}
