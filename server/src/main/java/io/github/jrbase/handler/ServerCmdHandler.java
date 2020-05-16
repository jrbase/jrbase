package io.github.jrbase.handler;

import io.github.jrbase.dataType.ClientCmd;

/**
 * add server command must implement ServerCmdHandler
 *
 * @see io.github.jrbase.handler.annotation
 * * getCmdName is key
 * * instance of class is value
 */
public interface ServerCmdHandler {

    String handle(ClientCmd clientCmd);

    String getCmdName();
}
