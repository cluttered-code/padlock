package com.cluttered.code.saas.padlock;

import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.exec.Blocking;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;

/**
 * @author cluttered.code@gmail.com
 */
public class ErrorResponse {

    private final int code;
    private final String phrase;
    private final String message;

    private ErrorResponse(final HttpResponseStatus status, final String message) {
        this.code = status.code();
        this.phrase = status.reasonPhrase();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getMessage() {
        return message;
    }

    public static void renderJsonError(final Context context, final HttpResponseStatus status, final Throwable throwable) {
        renderJsonError(context, status, throwable.getMessage());
    }

    public static void renderJsonError(final Context context, final HttpResponseStatus status, final String message) {
        Blocking.get(() -> new ErrorResponse(status, message))
                .next(error -> context.getResponse().status(status.code()))
                .map(Jackson::json)
                .then(context::render);
    }
}
