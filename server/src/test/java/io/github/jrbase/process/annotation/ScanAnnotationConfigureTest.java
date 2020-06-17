package io.github.jrbase.process.annotation;

import io.github.jrbase.factory.CommandAbstractFactory;
import io.github.jrbase.handler.ServerCmdHandler;
import io.github.jrbase.process.CmdProcess;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScanAnnotationConfigureTest {


    @Test
    public void testKeyCommand() {

        ScanAnnotationConfigure<CmdProcess> scanAnnotationConfigure = CommandAbstractFactory.newProcessAnnotationConfigure();

        CmdProcess cmdProcess = scanAnnotationConfigure.get("get");
        CmdProcess cmdProcess2 = scanAnnotationConfigure.get("get");
        assertEquals(cmdProcess, cmdProcess2);
        CmdProcess cmdProcess3 = scanAnnotationConfigure.get("hget");
        assertEquals("hget", cmdProcess3.getCmdName());
        assertEquals(23, scanAnnotationConfigure.size());
    }

    @Test
    public void testServerCommand() {
        ScanAnnotationConfigure<ServerCmdHandler> scanAnnotationConfigure =
                CommandAbstractFactory.newHandlerAnnotation();

        ServerCmdHandler serverCmdHandler = scanAnnotationConfigure.get("ping");
        assertEquals("ping", serverCmdHandler.getCmdName());
        assertEquals(8, scanAnnotationConfigure.size());

        ServerCmdHandler serverCmdHandler2 = scanAnnotationConfigure.get("ping");

        assertEquals(serverCmdHandler, serverCmdHandler2);
    }

}
