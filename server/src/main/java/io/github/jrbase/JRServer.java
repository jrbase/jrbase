package io.github.jrbase;

import io.github.jrbase.server.JRBaseServer;

public class JRServer {
    private static JRBaseServer jrBaseServer;

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            jrBaseServer = new JRBaseServer();
        } else if (args.length == 1) {
            jrBaseServer = new JRBaseServer(Integer.parseInt(args[0]));
        } else if (args.length == 2) {
            jrBaseServer = new JRBaseServer(args[0], Integer.parseInt(args[1]));
        } else {
            jrBaseServer = new JRBaseServer();
        }
        jrBaseServer.start(null);

    }
}
