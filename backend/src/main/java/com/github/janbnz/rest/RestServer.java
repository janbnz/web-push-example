package com.github.janbnz.rest;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class RestServer extends NanoHTTPD {

    public RestServer() throws IOException {
        super(8690);
        this.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8690/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println(session.getMethod().toString() + " Request received!");

        for (RestAction restAction : ActionRegistry.getActions()) {
            if (session.getMethod().equals(restAction.getMethod()) && session.getUri().equals(restAction.getUri())) {
                return restAction.call(session);
            }
        }
        return super.serve(session);
    }
}