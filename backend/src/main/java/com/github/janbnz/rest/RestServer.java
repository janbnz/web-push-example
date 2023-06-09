package com.github.janbnz.rest;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

/**
 * The RestServer class extends NanoHTTPD to create a simple HTTP server.
 * It listens on port 8690 and handles incoming requests.
 */
public class RestServer extends NanoHTTPD {

    /**
     * Creates a new RestServer instance and starts the server.
     *
     * @throws IOException if an I/O error occurs while starting the server
     */
    public RestServer() throws IOException {
        super(8690);
        this.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8690/ \n");
    }

    /**
     * Handles incoming HTTP requests and routes them to the appropriate RestAction.
     *
     * @param session the HTTP session representing the incoming request
     * @return the response to the incoming request
     */
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