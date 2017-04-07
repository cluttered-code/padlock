package com.cluttered.code.saas.padlock.action;

import com.cluttered.code.saas.padlock.model.Credential;
import com.cluttered.code.saas.padlock.service.PasswordService;
import com.cluttered.code.saas.padlock.service.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;

import static com.cluttered.code.saas.padlock.ErrorResponse.renderJsonError;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static ratpack.jackson.Jackson.fromJson;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class LoginActionChain implements Action<Chain> {

    private final UserService userService;
    private final PasswordService passwordService;

    @Inject
    private LoginActionChain(final UserService userService, final PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public void execute(final Chain chain) throws Exception {
        chain.path("login", context -> context
                .byMethod(action -> action
                        .post(() -> this.login(context))
                )
        );
    }

    private void login(final Context context) {
        context.parse(Jackson.fromJson(Credential.class))
                .flatRight(userService::findByCredential)
                .flatMap(passwordService::validate)
                .route(isValid -> !isValid,
                        isValid -> renderJsonError(context, UNAUTHORIZED, "invalid credentials"))
                .map(Jackson::json)
                .then(context::render);
    }
}
