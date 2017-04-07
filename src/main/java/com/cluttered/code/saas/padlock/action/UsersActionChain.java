package com.cluttered.code.saas.padlock.action;

import com.cluttered.code.saas.padlock.db.dto.UserDto;
import com.cluttered.code.saas.padlock.model.Credential;
import com.cluttered.code.saas.padlock.model.User;
import com.cluttered.code.saas.padlock.service.PasswordService;
import com.cluttered.code.saas.padlock.service.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.exec.Blocking;
import ratpack.func.Action;
import ratpack.func.Pair;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;

import java.util.UUID;

import static com.cluttered.code.saas.padlock.ErrorResponse.renderJsonError;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static ratpack.jackson.Jackson.fromJson;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class UsersActionChain implements Action<Chain> {

    private final UserService userService;
    private final PasswordService passwordService;

    @Inject
    private UsersActionChain(final UserService userService, final PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public void execute(final Chain chain) throws Exception {
        chain.path("users", context -> context
                .byMethod(action -> action
                        .get(() -> this.listAll(context))
                        .post(() -> this.insert(context))))
                .path("users/:id", context -> context
                        .byMethod(action -> action
                                .get(() -> this.findById(context))
                                .put(() -> this.update(context))
                                .delete(() -> this.deleteById(context))));
    }

    private void listAll(final Context context) {
        userService.listAll()
                .map(Jackson::json)
                .then(context::render);
    }

    private void insert(final Context context) {
        context.parse(Jackson.fromJson(Credential.class))
                .flatRight(userService::insert)
                .route(credentialUserDtoPair -> credentialUserDtoPair.getRight() == null,
                        credentialUserDtoPair -> renderJsonError(context, CONFLICT, "unable to create user"))
                .flatMap(passwordService::insert)
                .route(userDtoCountPair -> userDtoCountPair.getRight() != 1,
                        userDtoCountPair -> renderJsonError(context, UNPROCESSABLE_ENTITY, "unable to store password"))
                .map(Pair::getLeft)
                .next(createdUser -> {
                    context.getResponse().status(CREATED.code());
                    context.getResponse().getHeaders().set("location", "/users/" + createdUser.getId());
                })
                .map(Jackson::json)
                .then(context::render);
    }

    private void findById(final Context context) {
        final UUID uuid = getIdFromPath(context);
        userService.findById(uuid)
                .onNull(() -> renderJsonError(context, NOT_FOUND, "user not found"))
                .map(Jackson::json)
                .then(context::render);
    }

    private void update(final Context context) {
        final UUID uuid = getIdFromPath(context);
        context.parse(fromJson(User.class))
                .route(inputUser -> !uuid.equals(inputUser.getId()),
                        inputUser -> renderJsonError(context, UNPROCESSABLE_ENTITY, "cannot change user id"))
                .map(UserDto::fromUser)
                .flatMap(userService::update)
                .onNull(() -> renderJsonError(context, UNPROCESSABLE_ENTITY, "unable to update user"))
                .map(Jackson::json)
                .then(context::render);

    }

    private void deleteById(final Context context) {
        Blocking.get(() -> getIdFromPath(context))
                .flatRight(userService::deleteById)
                .route(uuidIntegerPair -> uuidIntegerPair.getRight() == 0,
                        userCount -> renderJsonError(context, NOT_FOUND, "user not found"))
                .flatMap(passwordService::delete)
                .then(deleteCount -> context.getResponse().status(NO_CONTENT.code()).send());
    }

    private UUID getIdFromPath(final Context context) {
        final String uuidToken = context.getPathTokens().get("id");
        return UUID.fromString(uuidToken);
    }
}