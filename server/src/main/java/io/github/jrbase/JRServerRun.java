package io.github.jrbase;

import io.github.jrbase.server.JRBaseServer;

public class JRServerRun {

    public static void main(String[] args) {
        JRBaseServer jrBaseServer = new JRBaseServer();
        jrBaseServer.start(args);
    }
}
