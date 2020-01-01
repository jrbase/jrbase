package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;

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
     * @return true correct argument, false error argument
     */
    boolean isCorrectArguments(ClientCmd clientCmd);

    /**
     * handle command
     *
     * @param clientCmd request all param
     * @return get handle result
     */
    String process(ClientCmd clientCmd);
}
