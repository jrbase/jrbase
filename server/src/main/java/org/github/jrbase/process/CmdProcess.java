package org.github.jrbase.process;

import org.github.jrbase.dataType.ClientCmd;
import org.github.jrbase.execption.ArgumentsException;

public interface CmdProcess {
    void process(ClientCmd clientCmd) throws ArgumentsException;
}
