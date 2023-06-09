package com.github.janbnz.rest;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static fi.iki.elonen.NanoHTTPD.MIME_HTML;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public abstract class RestAction {

    private final NanoHTTPD.Method method;
    private final String uri;

    /**
     * Constructs a RestAction with the specified method and URI.
     *
     * @param method the HTTP method of the action
     * @param uri    the URI of the action
     */
    public RestAction(NanoHTTPD.Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    /**
     * Handles the HTTP request and returns the response.
     *
     * @param session the HTTP session representing the incoming request
     * @return the response to the request
     */
    public abstract NanoHTTPD.Response call(NanoHTTPD.IHTTPSession session);

    /**
     * Retrieves the body of the HTTP request as a JSON object.
     *
     * @param session the HTTP session representing the incoming request
     * @return the JSON object representing the request body, or null if an error occurs
     */
    public JSONObject getBody(NanoHTTPD.IHTTPSession session) {
        final HashMap<String, String> map = new HashMap<>();
        try {
            session.parseBody(map);
        } catch (IOException | NanoHTTPD.ResponseException ex) {
            ex.printStackTrace();
            return null;
        }

        return new JSONObject(map.get("postData"));
    }

    /**
     * Creates a Response object with the specified data and status.
     *
     * @param data   the data to include in the response body
     * @param status the HTTP status of the response
     * @return the created NanoHTTPD Response object
     */
    public static NanoHTTPD.Response getResponse(JSONObject data, NanoHTTPD.Response.Status status) {
        return getResponse(data.toString(), status);
    }

    /**
     * Creates a Response object with the specified data and status.
     *
     * @param data   the data to include in the response body
     * @param status the HTTP status of the response
     * @return the created NanoHTTPD Response object
     */
    public static NanoHTTPD.Response getResponse(String data, NanoHTTPD.Response.Status status) {
        return getResponse(data, status, null);
    }

    /**
     * Creates a Response object with the specified data, status, and redirect URL.
     *
     * @param data     the data to include in the response body
     * @param status   the HTTP status of the response
     * @param redirect the URL to redirect to, or null if no redirect is needed
     * @return the created NanoHTTPD Response object
     */
    public static NanoHTTPD.Response getResponse(String data, NanoHTTPD.Response.Status status, String redirect) {
        NanoHTTPD.Response resp = redirect == null ? newFixedLengthResponse(data) : newFixedLengthResponse(NanoHTTPD.Response.Status.REDIRECT, MIME_HTML, "");
        resp.setStatus(status);

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Max-Age", "3628800");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization");

        if (redirect != null) {
            resp.addHeader("Location", redirect);
        }
        resp.setChunkedTransfer(true);
        return resp;
    }

    public NanoHTTPD.Method getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }
}