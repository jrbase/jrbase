package org.github.jrbase.handler;

import org.github.jrbase.dataType.ClientCmd;

public interface ServerCmdHandler {
    String handle(ClientCmd clientCmd);
}
