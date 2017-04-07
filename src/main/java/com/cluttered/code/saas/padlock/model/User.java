package com.cluttered.code.saas.padlock.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
public class User {

    private final UUID id;
    private final String email;

    @JsonCreator
    public User(@JsonProperty("id") final UUID id,
                @JsonProperty("email") final String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
