package com.github.lg198.cnotes.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PacketHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request base, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


        base.setHandled(true);
    }
}
