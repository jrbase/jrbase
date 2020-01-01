package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.ArgumentsException;

public interface CmdProcess {
    /**
     * @return get command name
     */
    String getCmdName();

    /*
     * TODO: set async data type meta
     * @see TypeProcess
     */

    /**
     * check arguments
     *
     * @param clientCmd request all param
     * @throws ArgumentsException arguments exception handle
     */
    void checkArguments(ClientCmd clientCmd) throws ArgumentsException;

    /**
     * handle command
     *
     * @param clientCmd request all param
     * @return get handle result
     */
    String process(ClientCmd clientCmd);
}
