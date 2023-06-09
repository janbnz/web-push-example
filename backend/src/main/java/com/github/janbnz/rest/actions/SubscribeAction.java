package com.github.janbnz.rest.actions;

import com.github.janbnz.WebPush;
import com.github.janbnz.rest.RestAction;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

public class SubscribeAction extends RestAction {

    public SubscribeAction() {
        super(NanoHTTPD.Method.POST, "/subscribe");
    }

    @Override
    public NanoHTTPD.Response call(NanoHTTPD.IHTTPSession session) {
        final JSONObject body = this.getBody(session);

        final JSONObject subscriptionData = new JSONObject(body.getString("data"));
        WebPush.push("Hello world!", subscriptionData);

        return getResponse(new JSONObject().put("response", "Subscribed"), NanoHTTPD.Response.Status.ACCEPTED);
    }
}