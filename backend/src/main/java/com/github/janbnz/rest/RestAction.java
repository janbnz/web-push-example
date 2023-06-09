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

    public RestAction(NanoHTTPD.Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public abstract NanoHTTPD.Response call(NanoHTTPD.IHTTPSession session);

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

    public static NanoHTTPD.Response getResponse(JSONObject data, NanoHTTPD.Response.Status status) {
        return getResponse(data.toString(), status);
    }

    public static NanoHTTPD.Response getResponse(String data, NanoHTTPD.Response.Status status) {
        return getResponse(data, status, null);
    }

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