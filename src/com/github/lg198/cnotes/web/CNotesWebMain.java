package com.github.lg198.cnotes.web;

import org.eclipse.jetty.server.Server;

public class CNotesWebMain {

    public static void main(String[] args) throws Exception {
        Server server = new Server(42356);
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
