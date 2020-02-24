package org.github.jrbase.handler;

import org.github.jrbase.dataType.ClientCmd;

/**
 * add server command must implement ServerCmdHandler
 *
 * @see org.github.jrbase.handler.annotation
 * * getCmdName is key
 * * instance of class is value
 */
public interface ServerCmdHandler {

    String handle(ClientCmd clientCmd);

    String getCmdName();
}
