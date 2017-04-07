package com.cluttered.code.saas.padlock.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.MutableHeaders;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class CORSHandler implements Handler {

    @Inject
    private CORSHandler() {
    }

    @Override
    public void handle(final Context context) throws Exception {
        final MutableHeaders headers = context.getResponse().getHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Headers", "authorization, content-type, accept");
        context.next();
    }
}